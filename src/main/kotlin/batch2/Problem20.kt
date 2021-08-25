package batch2

import util.factorial

/**
 * Problem 20: Factorial Digit Sum
 *
 * https://projecteuler.net/problem=20
 *
 * Goal: Find the sum of the digits of N!
 *
 * Constraints: 0 <= N <= 1000
 *
 * e.g.: N = 10
 *       10! = 10 * 9 * ... * 2 * 1 = 3628800
 *       sum  = 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27
 */

class FactorialDigitSum {
    fun factorialDigitSum(num: Int): Int {
        return num.factorial()
            .toString()
            .map(Char::digitToInt)
            .sum()
    }
}