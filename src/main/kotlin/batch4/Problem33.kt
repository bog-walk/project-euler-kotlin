package batch4

import kotlin.math.pow
import util.gcd

/**
 * Problem 33: Digit Cancelling Fractions
 *
 * https://projecteuler.net/problem=33
 *
 * Goal: Find every non-trivial fraction where the numerator is less than
 * the denominator (both have N-digits) and the value of the reduced fraction
 * (by cancelling K digits from num. & denom.) is equal to the original fraction.
 *
 * Constraints: 2 <= N <= 4 & 1 <= K <= N-1
 *
 * Non-Trivial Fraction: Satisfies goal's conditions, e.g. 49/98 = 4/8.
 * Trivial Fraction: Fractions with trailing zeroes in both
 * numerator and denominator that allow cancellation, e.g. 30/50 = 3/5.
 *
 * e.g.: N =  = 2, K = 1
 *       non-trivials = {16 / 64, 19 / 95, 26 / 65, 49 / 98}
 *       reduced-equivalents = {1 / 4, 1 / 5, 2 / 5, 4 / 8}
*/

class DigitCancellingFractions {

    /**
     * Naive method to check if a reduced fraction is equivalent to its original.
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
     * Brute iteration through all numerators and denominators with the expected
     * amount of digits, & following constraints specified in problem above.
     *
     * @return  List of Pair(numerator, denominator)s.
     *
     * SPEED: 1758ms for N = 4, K = 1
     */
    fun findNonTrivialsBrute(n: Int, k: Int = 1): List<Pair<Int, Int>> {
        val nonTrivials = mutableListOf<Pair<Int, Int>>()
        val minN = ((10.0).pow(n - 1) + 1).toInt()
        val maxN = (10.0).pow(n).toInt()
        for (num in minN..(maxN / 2)) {
            for (denom in (num + 1)..maxN) {
                if (denom % 10 == 0) continue
                if (isReducedEquivalent(n, num, denom, k)) {
                    nonTrivials.add(num to denom)
                }
            }
        }
        return nonTrivials
    }

    /**
     * Rather than checking each brute iteration to see if cancelled digits match,
     * limit loops by pre-cancelling possible digits, thereby pre-reducing all
     * numerators & denominators, which reduces iteration by power of 10.
     *
     * Loop nesting is based on numerator < denominator & cancelled < max_cancelled.
     * This order of solutions is based on the combination equation:
     * ((10^k)*n + c) / ((10^k)*c + d) = n / d; which reduces to,
     * ((10^k)-1)*n(c - d) = c*(d - n)
     *
     * @return  List of Pair(numerator, denominator)s.
     *
     * SPEED (BEST for normal problem): 36ms for N = 4, K = 1
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
        return nonTrivials
    }

    /**
     * Project Euler specific implementation that requires all non-trivial
     * fractions that have 2 digits (pre-cancellation of 1 digit) to be found.
     *
     * @return The denominator of the product of the fractions
     * given in its lowest common terms.
     */
    fun productOfNonTrivials(): Int {
        val (numerators, denominators) = findNonTrivials(2, k=1).unzip()
        val nProd = numerators.fold(1) { acc, n -> acc * n }
        val dProd= denominators.fold(1) { acc, n -> acc * n }
        return dProd / gcd(nProd.toLong(), dProd.toLong()).toInt()
    }

    /**
     * Helper function for HackerRank specific implementation.
     * Finds all combinations for digits cancelled from a number based
     * on the indices of the digits to be cancelled. Ensures no combinations
     * have duplicate digits or duplicate integer outputs.
     *
     * @return Set of {post-cancellation integers}.
     */
    fun getCancelledCombos(num: String, combo: List<Char>): Set<Int> {
        val k = combo.size
        val original = num.indices.toSet()
        // e.g. num = "9919" with cancel_combo = ('9','9')
        // indices = ((0,1,3), (0,1,3))
        val indices: List<List<Int>> = combo.map { ch ->
            num.indices.filter { i ->
                num[i] == ch
            }
        }
        val perms = mutableSetOf<Int>()
        for (a in indices[0]) {
            if (k > 1) {
                for (b in indices[1]) {
                    if (a == b) continue
                    if (k > 2) {
                        for (c in indices[2]) {
                            if (b == c || a == c) continue
                            // e.g. {0,1,2,3} - {0,1} = {2,3}
                            val postCancel = original - setOf(a, b, c)
                            // above identical to "9919" becoming 19 post-cancellation
                            perms.add(postCancel.map { num[it] }.joinToString("").toInt())
                        }
                    } else {
                        val postCancel = original - setOf(a, b)
                        perms.add(postCancel.map { num[it] }.joinToString("").toInt())
                    }
                }
            } else {
                val postCancel = original - setOf(a)
                perms.add(postCancel.map { num[it] }.joinToString("").toInt())
            }
        }
        return perms
    }

    fun getCombinations(
        num: String,
        index: Int = 0,
        k: Int = 1,
        out: MutableList<Char> = mutableListOf(),
        combos: MutableSet<List<Char>> = mutableSetOf()
    ): Set<List<Char>> {
        if (k == 0) {
            combos.add(out)
        } else {
            for (j in index..num.lastIndex) {
                out.add(num[j])
                getCombinations(num, j + 1, k - 1, out, combos)
                out.removeAt(out.lastIndex)
            }
        }
        return combos
    }

    /**
     * HackerRank specific implementation that includes extra restrictions that
     * are not clearly specified on the problem page:
     * - The digits cancelled from the numerator and denominator can be in any order.
     * e.g. 1306/6530 == 10/50 and 6483/8644 == 3/5.
     * - Zeroes should not be cancelled, but leading zeroes are allowed as they will be
     * read as if removed.
     * e.g. 4808/8414 == 08/14 == 8/14 and 490/980 == 40/80.
     * - Pre-cancelled fractions must only be counted once, even if the cancelled
     * digits can be removed in different ways with the same output.
     * e.g. 1616/6464 == 161/644 == 116/464.
     *
     * @return Pair of (sum of numerators, sum of denominators).
     *
     * SPEED: s for N = 4, K = 1
     */
    fun sumOfNonTrivialsBrute(n: Int, k: Int): Pair<Int, Int> {
        var nSum = 0
        var dSum = 0
        val minNumerator = ((10.0).pow(n - 1) + 2).toInt()
        val maxDenominator = (10.0).pow(n).toInt()
        for (numerator in minNumerator..(maxDenominator - 2)) {
            val nS = numerator.toString()
        }
        return nSum to dSum
    }
}

fun main() {
    val tool = DigitCancellingFractions()
    val combos = tool.getCombinations("123", k=2)
    println(combos)
}