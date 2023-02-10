package dev.bogwalk.batch3

import dev.bogwalk.util.combinatorics.combinations
import dev.bogwalk.util.combinatorics.product
import kotlin.math.pow
import dev.bogwalk.util.maths.gcd

/**
 * Problem 33: Digit Cancelling Fractions
 *
 * https://projecteuler.net/problem=33
 *
 * Goal: Find every non-trivial fraction where the numerator is less than the denominator (both
 * have N-digits) and the value of the reduced fraction (by cancelling K digits from num. &
 * denom.) is equal to the original fraction.
 *
 * Constraints: 2 <= N <= 4 & 1 <= K <= N-1
 *
 * Non-Trivial Fraction: Satisfies goal's conditions, e.g. 49/98 = 4/8.
 *
 * Trivial Fraction: Fractions with trailing zeroes in both numerator and denominator that allow
 * cancellation, e.g. 30/50 = 3/5.
 *
 * e.g.: N = 2, K = 1
 *       non-trivials = {16/64, 19/95, 26/65, 49/98}
 *       reduced-equivalents = {1/4, 1/5, 2/5, 4/8}
*/

class DigitCancellingFractions {
    /**
     * Brute iteration through all numerators and denominators with the expected amount of
     * digits, & following constraints specified in problem above.
     *
     * SPEED (WORSE for PE problem) 2.67s for N = 4, K = 1
     *
     * @return List of Pair(numerator, denominator) sorted by numerator.
     */
    fun findNonTrivialsBrute(n: Int, k: Int = 1): List<Pair<Int, Int>> {
        val nonTrivials = mutableListOf<Pair<Int, Int>>()
        val minN = ((10.0).pow(n - 1) + 1).toInt()
        val maxD = (10.0).pow(n).toInt()
        for (num in minN until (maxD / 2)) {
            for (denom in (num + 1) until maxD) {
                if (denom % 10 == 0) continue
                if (isReducedEquivalent(n, num, denom, k)) {
                    nonTrivials.add(num to denom)
                }
            }
        }
        return nonTrivials
    }

    /**
     * Naive method checks if a reduced fraction is equivalent to its original fraction.
     */
    fun isReducedEquivalent(
        digits: Int,
        numerator: Int,
        denominator: Int,
        toCancel: Int
    ): Boolean {
        val nMod = (10.0).pow(toCancel).toInt()
        val dMod = (10.0).pow(digits - toCancel).toInt()
        if (numerator % nMod == denominator / dMod) {
            val ogFraction = 1.0 * numerator / denominator
            val reduced = 1.0 * (numerator / nMod) / (denominator % dMod)
            return ogFraction == reduced
        }
        return false
    }

    /**
     * Project Euler specific implementation that requires all non-trivial fractions that have 2
     * digits (pre-cancellation of 1 digit) to be found.
     *
     * @return the denominator of the product of the fractions given in their lowest common terms.
     */
    fun productOfNonTrivials(): Int {
        val (numerators, denominators) = findNonTrivials(2, k=1).unzip()
        val nProd = numerators.fold(1) { acc, n -> acc * n }
        val dProd= denominators.fold(1) { acc, n -> acc * n }
        return dProd / gcd(nProd.toLong(), dProd.toLong()).toInt()
    }

    /**
     * Solution limits loops by pre-cancelling possible digits, rather than checking each
     * iteration to see if cancelled digits match. This pre-reduces all numerators &
     * denominators, which reduces iteration by power of 10.
     *
     * Loop nesting is based on numerator < denominator & cancelled < cancelledMax.
     *
     * This order of solutions is based on the combination equation:
     *
     *      (n10^k + c) / (c10^k + d) = n/d
     *
     * which reduces to:
     *
     *      n(10^k -1)(c - d) = c(d - n)
     *
     * SPEED (BETTER for PE problem) 63.15ms for N = 4, K = 1
     *
     * @return List of Pair(numerator, denominator) sorted by numerator.
     */
    fun findNonTrivials(n: Int, k: Int = 1): List<Pair<Int, Int>> {
        val nonTrivials = mutableListOf<Pair<Int, Int>>()
        val cancelledMin = (10.0).pow(k - 1).toInt()
        val cancelledMax = (10.0).pow(k).toInt()
        val reducedMin = (10.0).pow(n - k - 1).toInt()
        val reducedMax = (10.0).pow(n - k).toInt()
        for (cancelled in cancelledMin until cancelledMax) {
            for (d2 in (reducedMin + 1) until reducedMax) {
                for (n2 in reducedMin until d2) {
                    val numerator = n2 * cancelledMax + cancelled
                    val denominator = cancelled * reducedMax + d2
                    if (n2 * denominator == numerator * d2) {
                        nonTrivials.add(numerator to denominator)
                    }
                }
            }
        }
        return nonTrivials.sortedBy { fraction -> fraction.first }
    }

    /**
     * HackerRank specific implementation that includes extra restrictions that are not clearly
     * specified on the problem page:
     *
     *  - The digits cancelled from the numerator and denominator can be in any order.
     *  e.g. 1306/6530 == 10/50 and 6483/8644 == 3/5.
     *
     *  - Zeroes should not be cancelled, but leading zeroes are allowed as they will be read as
     *  if removed.
     *  e.g. 4808/8414 == 08/14 == 8/14 and 490/980 == 40/80.
     *
     *  - Pre-cancelled fractions must only be counted once, even if the cancelled digits can be
     *  removed in different ways with the same output.
     *  e.g. 1616/6464 == 161/644 == 116/464.
     *
     * SPEED (WORSE for HR problem) 336.14s for N = 4, K = 1
     *
     * @return Pair of (sum of numerators, sum of denominators).
     */
    fun sumOfNonTrivialsBrute(n: Int, k: Int): Pair<Int, Int> {
        var nSum = 0
        var dSum = 0
        val minNumerator = ((10.0).pow(n - 1) + 2).toInt()
        val maxDenominator = (10.0).pow(n).toInt()
        for (numerator in minNumerator..(maxDenominator - 2)) {
            val nS = numerator.toString()
            val cancelCombos = combinations(nS.replace("0", "").toList(), k)
            outerDLoop@for (denominator in (numerator + 1) until maxDenominator) {
                val ogFraction = 1.0 * numerator / denominator
                for (combo in cancelCombos) {
                    val nPost = getCancelledCombos(nS, combo)
                    val dPost = getCancelledCombos(denominator.toString(), combo)
                    // denominator did not contain all digits to cancel
                    if (dPost.isEmpty()) continue
                    innerNLoop@for (n2 in nPost) {
                        if (n2 == 0) continue@innerNLoop
                        innerDLoop@for (d2 in dPost) {
                            // avoid division by zero error
                            if (d2 == 0) continue@innerDLoop
                            if (ogFraction == 1.0 * n2 / d2) {
                                nSum += numerator
                                dSum += denominator
                                // avoid duplicating numerator with this denominator
                                continue@outerDLoop
                            }
                        }
                    }
                }
            }
        }
        return nSum to dSum
    }

    /**
     * Finds all combinations for digits cancelled from a number based on the indices of the
     * digits to be cancelled. Ensures no combinations have duplicate digits or duplicate integer
     * outputs.
     *
     * @return set of post-cancellation integers.
     */
    fun getCancelledCombos(num: String, combo: List<Char>): Set<Int> {
        val k = combo.size
        val original = num.indices.toSet()
        // e.g. num = "9919" with cancelCombo = ('9','9')
        // indices = ((0,1,3), (0,1,3))
        val indices: Array<List<Int>> = Array(combo.size) { ch ->
            num.indices.filter { i ->
                num[i] == combo[ch]
            }
        }
        // product().toSet() returns {0,1}, {0,3}, {1,0}, {1, 3}... with {0,0},etc reduced to {0}
        return product(*indices)
            .map { it.toSet() }
            .filter { it.size == k } // remove combos reduced due to duplicate indices
            .map { comboSet ->
                // e.g. {0,1,2,3} - {0,1} = {2,3}
                (original - comboSet)
                    .joinToString("") { num.slice(it..it) }
                    .toInt() // above identical to "9919" becoming 19 post-cancellation
            }.toSet()
    }

    /**
     * HackerRank specific implementation with extra restrictions, as detailed in the above brute
     * force solution.
     *
     * This solution has been optimised by only looping through possible numerators & the
     * cancellation combos they allow. Rather than loop through denominators, gcd() is used to
     * assess reductive equivalence based on the following:
     *
     *      n_og / d_og = n_r / d_r, and
     *
     *      n_r = n_og / gcd(n_og, d_og)
     *
     *      d_r = d_og / gcd(n_og, d_og)
     *
     * SPEED (BETTER for HR problem) 507.98ms for N = 4, K = 1
     *
     * @return Pair of (sum of numerators, sum of denominators).
     */
    fun sumOfNonTrivialsGCD(n: Int, k: Int): Pair<Int, Int> {
        var nSum = 0
        var dSum = 0
        val minNumerator = (10.0).pow(n - 1).toInt()
        val maxDenominator = (10.0).pow(n).toInt()
        val maxReduced = (10.0).pow(n - k).toInt()
        for (numerator in minNumerator..(maxDenominator - 2)) {
            val nS = numerator.toString()
            val cancelCombos = combinations(nS.replace("0", "").toList(), k)
            // avoid denominator duplicates with same numerator
            val denominatorsUsed = mutableListOf<Int>()
            for (combo in cancelCombos) {
                // get all integers possible post-cancellation of k digits
                val nPost = getCancelledCombos(nS, combo)
                for (n2 in nPost) {
                    if (n2 == 0) continue
                    var d = numerator
                    var nr = n2
                    var i = 1
                    while (true) {
                        i++
                        val g = gcd(d.toLong(), nr.toLong()).toInt()
                        d = d / g * i
                        nr = nr / g * i
                        if (d <= numerator) continue
                        if (nr >= maxReduced || d >= maxDenominator) break
                        val dPost = getCancelledCombos(d.toString(), combo)
                        // denominator did not contain all digits to cancel
                        if (dPost.isEmpty()) continue
                        for (d2 in dPost) {
                            if (nr == d2 && d !in denominatorsUsed) {
                                nSum += numerator
                                dSum += d
                                denominatorsUsed.add(d)
                            }
                        }
                    }
                }
            }
        }
        return nSum to dSum
    }
}