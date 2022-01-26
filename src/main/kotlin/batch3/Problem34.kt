package batch3

import util.factorial

/**
 * Problem 34: Digit Factorials
 *
 * https://projecteuler.net/problem=34
 *
 * Goal: Find the sum of all numbers less than N that divide the sum of
 * the factorial of their digits (& therefore have minimum 2 digits).
 *
 * Constraints: 10 <= N <= 1e5
 *
 * e.g.: N = 20
 *       qualifying numbers = {19}
 *       as 1! + 9! = 362881, which % 19 = 0
 *       e.g. 18 does not work as 1! + 8! = 40321, which % 19 > 0
 *       sum = 19
 */

class DigitFactorials {
    // pre-calculation of all digits to increase performance
    private val factorials = List(10) { it.factorial().intValueExact() }

    /**
     * Increase this solution's efficiency by creating an array of upper constraint
     * size (plus 1) & looping once through all numbers then caching the result plus the
     * previously calculated sum, if it matches the necessary requirements.
     */
    fun sumOfDigitFactorialsHR(n: Int): Int {
        var sum = 0
        for (num in 10 until n) {
            val numSum = num.toString().sumOf { ch -> factorials[ch.digitToInt()] }
            if (numSum % num == 0) {
                sum += num
            }
        }
        return sum
    }

    /**
     * Project Euler specific implementation that finds the sum of all numbers
     * that are equal to the sum of the factorial of their digits.
     * Search limit based on double-digit need and that a 7-digit number of only
     * 9! would sum to 2_540_160, so the 1st digit of the 7-digit number cannot
     * be greater than 2. An 8-digit number is not possible as 8 * 9! still
     * results in a 7-digit sum.
     * The only 2 numbers are: 145 and 40585.
     */
    fun sumOfDigitFactorialsPE(): Int {
        var sum = 0
        for (num in 10 until 2_000_000) {
            val digits = num.toString().map { it.digitToInt() }
            var numSum = 0
            for (digit in digits) {
                numSum += factorials[digit]
                // prevents further unnecessary calculation
                if (numSum > num) break
            }
            if (numSum == num) sum += numSum
        }
        return sum
    }
}