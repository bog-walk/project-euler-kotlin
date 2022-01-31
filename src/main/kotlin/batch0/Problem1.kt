package batch0

import util.maths.gaussianSum
import util.maths.lcm

/**
 * Problem 1: Multiples of 3 or 5
 *
 * https://projecteuler.net/problem=1
 *
 * Goal: Find the sum of all natural numbers less than N that are multiples of either of the
 * provided factors K1 or K2.
 *
 * Constraints: 1 <= N <= 1e9, 1 <= K <= N
 *
 * e.g.: N = 10, K1 = 3, K2 = 5
 *       multiples of K1 || K2 < N = {3, 5, 6, 9}
 *       sum = 23
 */

class MultiplesOf3Or5 {
    /**
     * Brute iteration through all numbers < [n] that checks for predicate.
     *
     * @throws OutOfMemoryError for upper test constraints > N = 1e7.
     */
    fun sumOfMultiplesBrute(n: Int, factor1: Int, factor2: Int): Long {
        val minNum = minOf(factor1, factor2).toLong()
        return (minNum until n.toLong()).filter { num ->
            num % factor1 == 0L || num % factor2 == 0L
        }.sum()
    }

    /**
     * Calculates the sum of an arithmetic progression sequence.
     *
     * Solution based on the formula:
     *
     * {n-1}Sigma{k=0} a + kd = n(2a + (n - 1)d)/2,
     *
     * where a is the 1st term, d is the delta, and n is the amount of terms to add.
     *
     * a and d are the same in this case, so the formula becomes:
     *
     * {n-1}Sigma{k=0} a + kd = (n(n + 1)d)/2
     *
     * Note that this is an adapted Gaussian sum formula, where n is replaced with the amount of
     * terms that are evenly divisible by d, then the original formula is multiplied by d.
     */
    private fun sumOfArithProgression(maxTerm: Int, delta: Int): Long {
        val terms = maxTerm / delta
        return terms.gaussianSum() * delta.toLong()
    }

    /**
     * Calculates the sum of multiples of both factors minus the sum of duplicates found via the
     * least common multiple of the given factors.
     */
    fun sumOfMultiples(n: Int, factor1: Int, factor2: Int): Long {
        val maxTerm = n - 1 // n not inclusive
        return if (factor1 == factor2) {
            sumOfArithProgression(maxTerm, factor1)
        } else {
            val duplicateSum = sumOfArithProgression(maxTerm, lcm(factor1, factor2))
            sumOfArithProgression(maxTerm, factor1)
                .plus(sumOfArithProgression(maxTerm, factor2))
                .minus(duplicateSum)
        }
    }
}