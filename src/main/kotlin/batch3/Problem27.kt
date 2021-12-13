package batch3

import util.isPrime
import util.primeNumbersOG

/**
 * Problem 27: Quadratic Primes
 *
 * https://projecteuler.net/problem=27
 *
 * Goal: Find coefficients a & b for the quadratic expression that produces
 * the maximum number of primes for consecutive values of n = 0..N.
 *
 * Constraints: 42 <= N <= 2000
 *
 * Quadratic Formula: n^2 + a*n + b, where |a| < N & |b| <= N.
 *
 * Euler's Quadratic formula -> n^2 + n + 41, produces 40 primes
 * for the consecutive values of n = 0..39.
 * The formula -> n^2 - 79n + 1601, produces 80 primes for the
 * consecutive values of n = 0..79.
 *
 * e.g.: N = 42
 *       formula -> n^2 - n + 41, produces 42 primes
 *       result = -1 41
 */

class QuadraticPrimes {

    /**
     * Brute force of all a, b combinations optimised based on:
     * For n = 0, formula -> 0^2 + 0 + b = b, which means that
     * b must be a prime number itself.
     * For n = 1, formula -> 1^2 + a + b, with b being a prime:
     *  - if b = 2, then a must be even for result to be an odd prime;
     *  - if b > 2, then a must be odd for result to be an odd prime.
     *
     *  @return [Triple] -> (a, b, count_of_primes)
     */
    fun quadPrimeCoeff(maxN: Int): Triple<Int, Int, Int> {
        var maxCoeffs = Triple(0, 0, 0)
        val primes = primeNumbersOG(maxN)
        val lowestA = if (maxN % 2 == 0) -maxN - 1 else -maxN - 2
        // a will only be even if b == 2, so keep odd values only
        for (a in lowestA..maxN step 2) {
            for (b in primes) {
                var n = 0
                val adjustedA = if (b != 2) a else a - 1
                while (isPrime(n * n + adjustedA * n + b)) {
                    n++
                }
                if (n > maxCoeffs.third) {
                    maxCoeffs = Triple(a, b, n + 1)
                }
            }
        }
        return maxCoeffs
    }
}