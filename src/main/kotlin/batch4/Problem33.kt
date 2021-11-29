package batch4

import util.primeFactors
import kotlin.math.pow

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
 * e.g.: N =
*/

class DigitCancellingFractions {
   fun isReducedEquivalent(
        numerator: Int,
        denominator: Int,
        toCancel: Int
    ): Boolean {
        // Pair(digitsToCancel, digitsToKeep)
        val nReduced: Pair<String, String>
        val dReduced: Pair<String, String>
        numerator.toString().run {
            nReduced = this.takeLast(toCancel) to this.dropLast(toCancel)
        }
        denominator.toString().run {
            dReduced = this.take(toCancel) to this.drop(toCancel)
        }
        if (nReduced.first == dReduced.first) {
            val ogFraction = 1.0 * numerator / denominator
            val reduced = nReduced.second.toDouble() / dReduced.second.toDouble()
            return ogFraction == reduced
        }
       return false
   }

    fun findNonTrivials(n: Int, k: Int = 1): List<Pair<Int, Int>> {
        val nonTrivials = mutableListOf<Pair<Int, Int>>()
        // Adjust range based on number of digits before cancellation
        val minN = ((10.0).pow(n - 1) + 1).toInt()
        // Denominator must always be larger than numerator
        val maxN = ((10.0).pow(n) - 2).toInt()
        for (num in minN..maxN) {
            for (denom in (num+1)..(maxN+1)) {
                if (isReducedEquivalent(num, denom, k)) {
                    nonTrivials.add(num to denom)
                }
            }
        }
        return nonTrivials
    }

    /**
     * Project Euler specific implementation that required all non-trivial
     * fractions that have 2 digits (pre-cancellation) to be found.
     * Result = {(16, 64), (19, 95), (26, 65), (49, 98)}.
     *
     * @return The denominator of the product of the fractions
     * given in its lowest common terms.
     */
    fun productOfNonTrivials(): Int {
        val nonTrivials = findNonTrivials(2, k=1).unzip()
        val numerators = nonTrivials.first.toMutableList()
        val denominators = nonTrivials.second.toMutableList()

        val nProd = numerators.fold(1) { acc, n -> acc * n }
        val dProd= denominators.fold(1) { acc, n -> acc * n }

        val nProdReduced = primeFactors(1L * nProd).flatMap { (k, v) ->
            List(v) { k.toInt() }
        }
        val dProdReduced = primeFactors(1L * dProd).flatMap { (k, v) ->
            List(v) { k.toInt() }
        }

        val commonDenominator = dProdReduced.toMutableList()
        for (p in nProdReduced) {
            if (p in commonDenominator) {
                commonDenominator.remove(p)
            }
        }

        return commonDenominator.fold(1) { acc, n -> acc * n }
    }
}

fun main() {
    val tool = DigitCancellingFractions()
   tool.findNonTrivials(3, 2)
}