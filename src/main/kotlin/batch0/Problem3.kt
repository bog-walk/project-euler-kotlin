package batch0

import util.maths.primeFactors
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
 * any number.
 *
 * e.g.: N = 10
 *       prime factors = {2, 5}
 *       largest = 5
 */

class LargestPrimeFactor {
    /**
     * Uses prime decomposition via the Sieve of Eratosthenes algorithm to return the largest
     * prime factor.
     *
     * SPEED (BETTER for N with small factors) 51.83ms for N = 1e12
     * SPEED (WORSE for N with large factors) 33.34ms for N = 600_851_475_143
     */
    fun largestPrimeFactor(n: Long): Long {
        return primeFactors(n).keys.maxOrNull()!!
    }

    /**
     * SPEED (WORSE for N with small factors) 256.15ms for N = 1e12
     * SPEED (BETTER for N with large factors) 24.54ms for N = 600_851_475_143
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