package dev.bogwalk.batch2

import dev.bogwalk.util.combinatorics.combinationsWithReplacement
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
    /**
     * SPEED (WORSE) 88.60ms for N = 6
     */
    fun digitNthPowersBrute(n: Int): List<Int> {
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

    /**
     * Considers all combinations of digits (0-9 with replacement) for max number of
     * digits that allow valid candidates, using a combinatorics algorithm.
     *
     * This algorithm returns all possible subsets, allowing element repetitions, but not
     * allowing arrangements that are identical except for order. This produces significantly
     * fewer subsets than a Cartesian product algorithm, which would not differ between, e.g.,
     * 122 and 212. It is redundant to check both these numbers as both would return the same
     * comboSum (due to commutative addition).
     *
     * Instead, if the generated comboSum itself produces an identical comboSum, then it is a
     * valid number to include in the sum.
     *
     * SPEED (BETTER) 56.97ms for N = 6
     */
    fun digitNthPowers(n: Int): List<Int> {
        val nums = mutableListOf<Int>()
        val powers = List(10) { (1.0 * it).pow(n).toInt() }
        val maxDigits = if (n < 5) n else 6
        for (digits in combinationsWithReplacement(0..9, maxDigits)) {
            val comboSum = digits.sumOf { powers[it] }
            val comboSum2 = comboSum.toString().sumOf { powers[it.digitToInt()] }
            if (comboSum == comboSum2 && comboSum > 9) nums.add(comboSum)
        }
        return nums.sorted()
    }
}