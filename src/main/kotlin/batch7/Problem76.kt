package batch7

import java.math.BigInteger

/**
 * Problem 76: Counting Summations
 *
 * https://projecteuler.net/problem=76
 *
 * Goal: Count the number of ways (mod 1e9 + 7) that N can be written as a sum of at least 2
 * positive integers.
 *
 * Constraints: 2 <= N <= 1000
 *
 * e.g.: N = 5
 *       count = 6 -> {4+1, 3+2, 3+1+1, 2+2+1, 2+1+1+1, 1+1+1+1+1}
 */

class CountingSummations {
    /**
     * Solution is identical to the bottom-up approach that found the number of ways a total could
     * be achieved using coins of different values (Batch 3 - Problem 31).
     *
     * The amount of ways to sum [n] is determined using the previous amount calculated for a sum
     * of lesser value and the previous amount calculated for the partition of [n].
     *
     * N.B. The final value at index[n] returns the correct result but every value in preceding
     * indices will be off by 1, as they include the sum itself alone (which is not valid given the
     * constraints require a minimum of 2 positive integers). So if this solution is used to
     * generate all counts <= 1000 for quick-pick testing, the array must be of size 1002 & the
     * loop limits must be 1001, then all results can be found by subtracting 1 from the stored
     * value.
     */
    fun countSumCombos(n: Int): Int {
        val modulus = BigInteger.valueOf(1_000_000_007)
        val combosBySum = Array<BigInteger>(n + 1) {
            BigInteger.ZERO
        }.apply { this[0] = BigInteger.ONE }
        for (i in 1 until n) {
            for (j in i..n) {
                combosBySum[j] += combosBySum[j - i]
            }
        }
        return combosBySum[n].mod(modulus).intValueExact()
    }
}