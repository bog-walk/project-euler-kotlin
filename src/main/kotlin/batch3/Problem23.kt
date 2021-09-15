package batch3

import util.primeNumbers
import util.sumProperDivisors

/**
 * Problem 23: Non-Abundant Sums
 *
 * https://projecteuler.net/problem=23
 *
 * Goal: Return whether or not N can be expressed as the sum of 2 abundant numbers.
 *
 * Constraints: 0 <= N <= 1e5
 *
 * Perfect Number: the sum of its proper divisors equals itself.
 * e.g. proper_D(6) = {1,2,3}.sum() = 6
 *
 * Deficient Number: the sum of its proper divisors is less than itself.
 * e.g. proper_D(4) = {1, 2}.sum() = 3
 *
 * Abundant Number: the sum of its proper divisors exceeds itself.
 * e.g. proper_D(12) = {1,2,3,4,6}.sum() = 16
 *
 * By mathematical analysis, all integers > 28123 can be expressed as the sum
 * of 2 abundant numbers. This upper limit cannot, however, be reduced further
 * even though it is known that the largest number that cannot be expressed as
 * the sum of 2 abundant numbers is less than this upper limit.
 *
 * e.g.: N = 24
 *       smallest abundant number = 12,
 *       so smallest integer that can be expressed = 12 + 12 = 24
 *       result = True
 */

class NonAbundantSums {
    fun isAbundant(n: Int): Boolean = sumProperDivisors(n) > n

    /**
     * This solution is optimised based on:
     * - 12 being the smallest abundant number to exist, so the smallest
     * integer expressed as a sum of abundants is 24.
     * - 945 being the smallest odd abundant number.
     * - An odd number has to be the sum of an even & odd number, so
     * all odd numbers under 957 (945 + 12) cannot be the sum of abundants,
     * since all abundants below it will be even.
     * - All integers > 20161 can be expressed as sum of 2 abundant numbers.
     * - xMax of x + y = N would be N / 2, to avoid duplicate checks.
     */
    fun isSumOfAbundants(n: Int): Boolean {
        if (n < 24) return false
        if (n < 957 && n % 2 != 0) return false
        if (n > 20161) return true
        val xMax = n / 2
        val range = if (xMax < 945) {
            12..xMax step 2
        } else {
            // Could consider removing prime numbers > 944 as primes
            // cannot be abundant numbers. Diff in speed negligible.
            (12..944 step 2) + (945..xMax)
        }
        for (x in range) {
            val y = n - x
            if (isAbundant(x) && isAbundant(y)) {
                return true
            }
        }
        return false
    }

    /**
     * Project Euler specific solution meant to find the sum of all
     * positive integers that cannot be written as the sum of 2
     * abundant numbers.
     * 20161 being the largest integer that cannot be expressed as such
     * is shown in the final test case.
     */
   fun sumOfAllNonAbundants(): Int {
        var sum = 0
        for (n in 0..20161) {
            if (!isSumOfAbundants(n)) {
                sum += n
            }
        }
        return sum
    }
}