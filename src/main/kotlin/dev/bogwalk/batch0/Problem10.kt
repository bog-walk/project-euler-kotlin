package dev.bogwalk.batch0

/**
 * Problem 10: Summation of Primes
 *
 * https://projecteuler.net/problem=10
 *
 * Goal: Find the sum of all prime numbers <= N.
 *
 * Constraints: 1 <= N <= 1e6
 *
 * e.g. N = 5
 *      primes = {2, 3, 5}
 *      sum = 10
 */

class SummationOfPrimes {
    /**
     * Stores the cumulative sum of prime numbers to allow quick access to the answers for
     * multiple N <= [n].
     *
     * Solution mimics original Sieve of Eratosthenes algorithm that iterates over only odd
     * numbers & their multiples, but uses boolean mask to alter a list of cumulative sums
     * instead of returning a list of prime numbers.
     *
     * SPEED (WORSE) 51.48ms for N = 1e6
     *
     * @return array of the cumulative sums of prime numbers <= index.
     */
    fun sumOfPrimesQuickDraw(n: Int): LongArray {
        require(n % 2 == 0) { "Limit must be even otherwise loop check needed" }
        val primesBool = BooleanArray(n + 1) {
            it == 2 || it > 2 && it % 2 != 0
        }
        val sums = LongArray(n + 1) { 0L }.apply { this[2] = 2L }
        for (i in 3..n step 2) {
            if (primesBool[i]) {
                // change current odd number
                sums[i] = sums[i - 1] + i
                // mark all multiples of this prime
                if (1L * i * i < n.toLong()) {
                    for (j in (i * i)..n step 2 * i) {
                        primesBool[j] = false
                    }
                }
            } else {
                // odd is not prime so keep most recent total
                sums[i] = sums[i - 1]
            }
            // change next even number
            sums[i + 1] = sums[i]
        }
        return sums
    }

    /**
     * Similar to the above solution in that it stores the cumulative sum of prime numbers to
     * allow future quick access; however, it replaces the typical boolean mask from the Sieve of
     * Eratosthenes algorithm with this cumulative cache.
     *
     * An unaltered element == 0 indicates a prime, with future multiples of that prime
     * marked with a -1, before the former, and its even successor, are replaced by the total so
     * far.
     *
     * SPEED (BETTER) 31.90ms for N = 1e6
     *
     * @return array of the cumulative sums of prime numbers <= index.
     */
    fun sumOfPrimesQuickDrawOptimised(n: Int): LongArray {
        require(n % 2 == 0) { "Limit must be even otherwise loop check needed" }
        val sums = LongArray(n + 1) { 0L }.apply { this[2] = 2L }
        var total = 2L
        for (i in 3..n step 2) {
            if (sums[i] == 0L) {
                total += i
                // mark all multiples of this prime
                if (1L * i * i < n.toLong()) {
                    for (j in (i * i)..n step 2 * i) {
                        sums[j] = -1L
                    }
                }
            }
            // change current odd & next even number
            sums[i] = total
            sums[i + 1] = total
        }
        return sums
    }
}