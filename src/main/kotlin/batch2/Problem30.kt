package batch2

import kotlin.math.pow

/**
 * Problem 30: Digit Fifth Powers
 *
 * https://projecteuler.net/problem=30
 *
 * Goal: Calculate the sum of all numbers that can be written as the sum of the Nth power of
 * their digits.
 *
 * Constraints: 3 <= N <= 6
 *
 * e.g.: N = 4
 *       1634 = 1^4 + 6^4 + 3^4 + 4^4
 *       8208 = 8^4 + 2^4 + 0^4 + 8^4
 *       9474 = 9^4 + 4^4 + 7^4 + 4^4
 *       sum = 1634 + 8208 + 9474 = 19316
 */

class DigitFifthPowers {
    fun digitNthPowers(n: Int): List<Int> {
        val nums = mutableListOf<Int>()
        val powers = List(10) { (1.0 * it).pow(n).toInt() }
        val start = maxOf(100, 10.0.pow(n - 2).toInt())
        val end = minOf(999_999, (9.0.pow(n) * n).toInt())
        for (num in start until end) {
            var digits = num
            var sumOfPowers = 0
            while (digits > 0) {
                sumOfPowers += powers[digits % 10]
                if (sumOfPowers > num) break
                digits /= 10
            }
            if (sumOfPowers == num) nums.add(num)
        }
        return nums
    }
}