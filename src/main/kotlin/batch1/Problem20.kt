package batch1

import util.maths.factorial

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
 *       10! = 10 * 9 * ... * 2 * 1 = 3_628_800
 *       sum  = 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27
 */

class FactorialDigitSum {
    /**
     * SPEED (WORSE) 63.65ms for N = 1000
     */
    fun factorialDigitSum(n: Int): Int {
        return n.factorial()
            .toString()
            .map(Char::digitToInt)
            .sum()
    }

    /**
     * SPEED (BETTER) 22.33ms for N = 1000
     */
    fun factorialDigitSumAlt(n: Int): Int {
        return n.factorial().toString().sumOf(Char::digitToInt)
    }
}