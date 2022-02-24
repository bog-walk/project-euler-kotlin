package batch5

import util.maths.powerDigitSum

/**
 * Problem 56: Powerful Digit Sum
 *
 * https://projecteuler.net/problem=56
 *
 * Goal: Considering natural numbers of the form a^b, where a, b < N, find the maximum digit sum.
 *
 * Constraints: 5 <= N <= 200
 *
 * Googol: A massive number that is 1 followed by 100 zeroes, i.e. 10^100. Another unimaginably
 * large number is 100^100, which is 1 followed by 200 zeroes. Despite both their sizes, the sum
 * of each number's digits equals 1.
 *
 * e.g.: N = 5
 *       4^4 = 256 -> 2 + 5 + 6 = 13
 *       max sum = 13
 */

class PowerfulDigitSum {
    /**
     * Solution optimised by setting loop limits based on the pattern observed of the a, b
     * combinations that achieved the maximum digit sums:
     *
     *  -   a is never less than n / 2, even for outliers with large deltas, e.g. n = 42 achieved
     *  max sum at 24^41.
     *
     *  -   b is never more than 5 digits less than [n], e.g. n = 100 achieved max sum at 99^95.
     */
    fun maxDigitSum(n: Int): Int {
        var maxSum = 1
        val minA = n / 2
        val minB = maxOf(1, n - 5)
        for (a in minA until n) {
            for (b in minB until n) {
                maxSum = maxOf(maxSum, powerDigitSum(a, b))
            }
        }
        return maxSum
    }
}