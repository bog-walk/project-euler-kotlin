package dev.bogwalk.batch1

import dev.bogwalk.util.custom.RollingQueue
import java.math.BigInteger

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
 *       input = [34827, 93726, 90165]
 *       sum = 218_718
 *       1st 2 digits = 21
 */

class LargeSum {
    /**
     * Solution simulates manual addition from RTL, using custom RollingQueue class to abstract
     * away the need to maintain output length with every iteration.
     *
     * SPEED (BETTER) 6.86ms for 100 50-digit numbers
     * SPEED (BETTER) 15.96ms for 1000 50-digit numbers
     */
    fun addInReverse(numbers: List<String>): String {
        val n = numbers.size
        if (n == 1) return numbers.single().slice(0..9)
        val output = RollingQueue<Int>(10)
        val finalIndex = numbers.first().length - 1
        var carryOver = 0
        for (i in finalIndex downTo 0) {
            val sum = numbers.sumOf { it[i].digitToInt() } + carryOver
            output.add(sum % 10)
            carryOver = sum / 10
        }
        while (carryOver > 0) {
            output.add(carryOver % 10)
            carryOver /= 10
        }
        return output.reversed().joinToString("")
    }

    /**
     * Solution using BigInteger to intrinsically sum all numbers.
     *
     * SPEED (WORSE) 7.17ms for 100 50-digit numbers
     * SPEED (WORSE) 18.30ms for 1000 50-digit numbers
     */
    fun sliceSum(numbers: List<String>): String {
        return numbers.sumOf { BigInteger(it) }.toString().take(10)
    }
}