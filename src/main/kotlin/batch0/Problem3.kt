package batch0

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
 * Fundamental Theorem of Arithmetic: There will only ever be a
 * unique set of prime factors for any number.
 *
 * e.g.: N = 10
 *      prime factors = {2, 5}
 *      largest = 5
 */

class LargestPrimeFactor {
    fun largestPrime(primes: List<Long>): Long = primes.maxOrNull()!!

    /**
     * Start from the smallest prime & repeatedly divide until
     * dividend cannot be divided exactly, then increase factor
     * and repeat.
     * Very slow &/or TimeOut for N >= 1e8.
     */
    fun getPrimeFactorsBrute(n: Long): List<Long> {
        var factor = 1L
        var dividend = n
        val factors = mutableListOf<Long>()
        while (factor <= n) {
            factor++
            if (isComposite(factor)) continue
            var division = divide(dividend, factor)
            while (division.first) {
                factors.add(factor)
                dividend = division.second
                division = divide(dividend, factor)
            }
        }
        return factors
    }

    /**
     * Determines if number has at least 1 divisor other than 1 and
     * itself. This is the opposite of a prime number.
     */
    private fun isComposite(n: Long): Boolean {
        val max = sqrt(n.toFloat()).toLong()
        return (2L..max).any {
            n % it == 0L
        }
    }

    private fun divide(n: Long, factor: Long): Pair<Boolean, Long> {
        return Pair(n % factor == 0L, n / factor)
    }

    /**
     * Stores prime factors as keys in a map with their exponent values.
     * Only tests odd factors as 2 is the only even prime number.
     * Limits factors as the largest prime factor will not be > sqrt(N).
     * Map could be converted to a list to return all prime factors.
     */
    fun getLargestPrimeExponent(n: Long): Long {
        var num = n
        val primes = mutableMapOf<Long, Int>()
        val maxFactor = sqrt(num.toDouble()).toLong()
        val factors = listOf(2L) + (3L..maxFactor step 2L)
        for (factor in factors) {
            while (num % factor == 0L) {
                primes[factor] = primes.getOrDefault(factor, 0) + 1
                num /= factor
            }
        }
        if (num > 2) primes[num] = primes.getOrDefault(num, 0) + 1
        return primes.keys.maxOrNull()!!
    }

    /**
     * Recursive implementation that also limits factors to 2, then steps
     * through only odd factors (as 2 is the only even prime), & stops once
     * the sqrt(N) is reached.
     */
    fun getPrimeFactorsRecursive(
        n: Long,
        primes: MutableList<Long> = mutableListOf()
    ): List<Long> {
        val maxFactor = sqrt(n.toDouble()).toLong()
        val factors = listOf(2L) + (3L..maxFactor step 2L)
        for (factor in factors) {
            if (n % factor == 0L) {
                primes.add(factor)
                return getPrimeFactorsRecursive(n / factor, primes)
            }
        }
        if (n > 2) primes.add(n)
        return primes
    }
}