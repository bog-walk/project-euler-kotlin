package batch1

import util.RollingQueue

/**
 * Problem 13: Large Sum
 *
 * https://projecteuler.net/problem=13
 *
 * Goal: Find the first 10 digits of the sum of N 50-digit numbers.
 *
 * Constraints: 1 <= N <= 1e3
 *
 * e.g.: N.B. This is a scaled-down example (first 2 digits of N 5-digit numbers)
 *       N = 3
 *       34827, 93726, 90165
 *       sum = 218718
 *       1st 2 digits = 21
 */

class LargeSum {

    /**
     * Given that BigInteger is not capable of handling this sum problem,
     * this solution mimics manual addition from RTL.
     */
    fun addInReverse(digits: List<String>): String {
        val n = digits.size
        if (n == 1) return digits.first().slice(0..9)
        val output = RollingQueue<Int>(10)
        val finalIndex = digits.first().length - 1
        var carryOver = 0
        for (i in finalIndex downTo 0) {
            val sum = List(n + 1) {
                if (it == n) carryOver else digits[it][i].digitToInt()
            }.sum()
            output.add(sum % 10)
            carryOver = sum / 10
        }
        if (carryOver > 0) {
            do {
                output.add(carryOver % 10)
                carryOver /= 10
            } while (carryOver != 0)
        }
        return output.reversed().joinToString("")
    }
}