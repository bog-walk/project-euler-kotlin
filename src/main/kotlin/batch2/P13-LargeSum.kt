package batch2

import util.RollingQueue

/**
 * Problem 13: Large Sum
 * Goal: Find the first 10 digits of the sum of N 50-digit
 * numbers, with 1 <= N <= 10^3.
 *
 */

class LargeSum {
    fun addInReverse(digits: List<String>): String {
        val n = digits.size
        if (n == 1) return digits.first()
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
                val mod = carryOver % 10
                output.add(mod)
                carryOver /= 10
            } while (carryOver != 0)
        }
        return output.reversed().joinToString("")
    }
}