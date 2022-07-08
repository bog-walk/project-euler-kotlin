package dev.bogwalk.batch2

import dev.bogwalk.util.maths.sumProperDivisors

/**
 * Problem 23: Non-Abundant Sums
 *
 * https://projecteuler.net/problem=23
 *
 * Goal: Return whether N can be expressed as the sum of 2 abundant numbers.
 *
 * Constraints: 0 <= N <= 1e5
 *
 * Perfect Number: The sum of its proper divisors equals itself.
 * e.g. properD(6) = sum{1,2,3} = 6
 *
 * Deficient Number: The sum of its proper divisors is less than itself.
 * e.g. properD(4) = sum{1, 2} = 3
 *
 * Abundant Number: The sum of its proper divisors exceeds itself.
 * e.g. properD(12) = sum{1,2,3,4,6} = 16
 *
 * By mathematical analysis, all integers > 28123 can be expressed as the sum of 2 abundant
 * numbers. This upper limit cannot, however, be reduced further even though it is known that the
 * largest number that cannot be expressed as the sum of 2 abundant numbers is less than this
 * upper limit.
 *
 * e.g.: N = 24
 *       smallest abundant number = 12,
 *       so smallest integer that can be expressed = 12 + 12 = 24
 *       result = True
 */

class NonAbundantSums {
    /**
     * Project Euler specific implementation meant to find the sum of all positive integers that
     * cannot be written as the sum of 2 abundant numbers.
     *
     * N.B. The final test case shows that 20161 is the largest integer that cannot be expressed
     * as such, even though 28123 is provided as the upper limit.
     */
    fun sumOfAllNonAbundants(): Int {
        return (0..20161).sumOf { if (isSumOfAbundants(it)) 0 else it }
    }

    /**
     * Solution is optimised based on:
     *
     *  - 12 being the smallest abundant number to exist, so the smallest integer expressed as a
     * sum of abundants is 24.
     *
     *  - 945 being the smallest odd abundant number.
     *
     *  - An odd number has to be the sum of an even & odd number, so all odd numbers under
     *  957 (945 + 12) cannot be the sum of abundants, since all other abundants below 945
     *  are even.
     *
     *  - All integers > 20161 can be expressed as the sum of 2 abundant numbers, as shown in the
     *  final test case.
     *
     *  - xMax of x + y = N would be N / 2, to avoid duplicate checks.
     */
    fun isSumOfAbundants(n: Int): Boolean {
        if (n < 24 || (n < 957 && n % 2 != 0)) return false
        if (n > 20161) return true
        val xMax = n / 2
        val range = if (xMax < 945) {
            12..xMax step 2
        } else {
            // could consider removing prime numbers > 944 from loop as primes cannot be abundant
            // numbers, but the speed difference is negligible
            (12..944 step 2) + (945..xMax)
        }
        for (x in range) {
            if (isAbundant(x) && isAbundant(n - x)) {
                return true
            }
        }
        return false
    }

    fun isAbundant(n: Int) = sumProperDivisors(n) > n
}