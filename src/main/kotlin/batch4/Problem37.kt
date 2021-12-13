package batch4

import util.primeNumbers

/**
 * Problem 37: Truncatable Primes
 *
 * https://projecteuler.net/problem=37
 *
 * Goal: Find the sum of all primes less than N that are truncatable
 * both from left to right and from right to left (single-digit primes
 * are not considered).
 *
 * Constraints: 100 <= N <= 1e6
 *
 * Truncatable Prime: A prime that remains prime as digits are continuously
 * removed from LTR or RTL.
 * e.g. 3797 -> 797 -> 97 -> 7 and 3797 -> 379 -> 37 -> 3
 *
 * e.g.: N = 50
 *       truncatable primes = {23, 37}
 *       sum = 60
 */

class TruncatablePrimes {
    /**
     * Solution speed optimised based on the following:
     *
     * - There are only 11 such qualifying numbers.
     *
     * - A number must start and end with a single-digit prime.
     *
     * - No point in considering double digit primes less than 23.
     *
     * - Above 100, pattern shows that qualifying numbers must
     * start and end in a 3 or 7.
     *
     * - Above 1000, pattern shows that qualifying numbers must
     * have their first & last 3 digits be a prime number.
     *
     * - No need to check first & last digits again in final loop.
     */
    fun sumOfTruncPrimes(n: Int): Int {
        val primes = primeNumbers(n - 1)
        var sum = 0
        var count = 0
        outer@for (prime in primes.subList(8, primes.size)) {
            val p = prime.toString()
            val digits = p.length
            if (digits == 2) {
                if (p.take(1) !in "2357" || p.takeLast(1) !in "2357") {
                    continue
                }
            } else {
                if (p.take(1) !in "37" || p.takeLast(1) !in "37") {
                    continue
                }
                if (digits >= 4) {
                    if (p.take(3).toInt() !in primes || p.takeLast(3).toInt() !in primes) {
                        continue
                    }
                }
            }
            if (digits > 2) {
                for (i in 2 until digits) {
                    if (p.take(i).toInt() !in primes || p.takeLast(i).toInt() !in primes) {
                        continue@outer
                    }
                }
            }
            sum += prime
            count++
            if (count == 11) break@outer
        }
        return sum
    }
}