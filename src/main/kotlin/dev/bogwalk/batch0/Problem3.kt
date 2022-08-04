package dev.bogwalk.batch0

import dev.bogwalk.util.maths.primeFactorsOG
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Problem 3: Largest Prime Factor
 *
 * https://projecteuler.net/problem=3
 *
 * Goal: Find the largest prime factor of N.
 *
 * Constraints: 10 <= N <= 1e12
 *
 * Fundamental Theorem of Arithmetic: There will only ever be a unique set of prime factors for
 * any number greater than 1.
 *
 * e.g.: N = 10
 *       prime factors = {2, 5}
 *       largest = 5
 */

class LargestPrimeFactor {
    /**
     * Uses prime decomposition via trial division with some optimisations to return the largest
     * prime factor.
     *
     * SPEED (BETTER for N with small factors) 14.72ms for N = 1e12
     * SPEED (BETTER for N with large factors) 10.79ms for N = 600_851_475_143
     */
    fun largestPrimeFactor(n: Long): Long {
        return primeFactorsOG(n).keys.maxOrNull()!!
    }

    /**
     * Uses prime decomposition via trial division without any optimisation.
     *
     * SPEED (BEST for N with small factors) 3073ns for N = 1e12
     * SPEED (BEST for N with large factors) 2.6e+04ns for N = 600_851_475_143
     */
    fun largestPrimeFactorSimple(n: Long): Long {
        var num = n
        var factor = 2L
        while (factor * factor <= num) {
            while (num % factor == 0L && num != factor) {
                num /= factor
            }
            factor++
        }
        return num
    }

    /**
     * Identical to the optimised primeFactorsOG() algorithm but uses recursion instead of
     * storing the entire prime factorisation map.
     *
     * This method was implemented just to see how recursion could fit into this solution.
     *
     * SPEED (WORST for N with small factors) 122.06ms for N = 1e12
     * SPEED (WORST for N with large factors) 37.80ms for N = 600_851_475_143
     */
    fun largestPrimeFactorRecursive(n: Long, largest: Long = 2L): Long {
        val maxFactor = sqrt(n.toDouble()).toLong()
        val factors = listOf(2L) + (3L..maxFactor step 2L)
        for (factor in factors) {
            if (n % factor == 0L) {
                return largestPrimeFactorRecursive(n / factor, factor)
            }
        }
        return if (n > 2) max(largest, n) else largest
    }
}