package batch3

import util.gcd

/**
 * Problem 26: Reciprocal Cycles
 *
 * https://projecteuler.net/problem=26
 *
 * Goal: Find the value of the smallest d less than N for which
 * 1/d contains the longest recurring cycle in its decimal fraction part.
 *
 * Constraints: 4 <= N <= 1e4
 *
 * Unit Fraction: A fraction with 1 as the numerator.
 *
 * e.g.: N = 10
 *       1/2 = 0.5
 *       1/3 = 0.(3) -> 1-digit recurring cycle
 *       1/4 = 0.25
 *       1/5 = 0.2
 *       1/6 = 0.1(6) -> 1-digit recurring cycle
 *       1/7 = 0.(142857) -> 6-digit recurring cycle
 *       1/8 = 0.125
 *       1/9 = 0.(1) -> 1-digit recurring cycle
 *       result = 7
 */

class ReciprocalCycles {
    /**
     * Repetend/Reptend: the infinitely repeated digit sequence of the
     * decimal representation of a number.
     */
    fun isRepetend(n: Int): Boolean {
        TODO()
    }

    fun longestRecurringDecimal(n: Int): Int {
        TODO()
    }

    fun totient(n: Long): Int {
        return (1L..n).count { k -> gcd(n, k) == 1L }
    }
}