package dev.bogwalk.batch6

import kotlin.math.log10
import kotlin.math.pow

/**
 * Problem 63: Powerful Digit Counts
 *
 * https://projecteuler.net/problem=63
 *
 * Goal: Given N, find all N-digit positive integers that are also a Nth power.
 *
 * Constraints: 1 <= N <= 19
 *
 * e.g.: N = 2
 *       output = [16, 25, 36, 49, 64, 81]
 */

class PowerfulDigitCounts {
    /**
     * Solution optimised by searching backwards from 9^N, as 10^N produces a number with (N + 1)
     * digits, and breaking the loop as soon as the power's digits drop below N.
     *
     * @return list of all N-digit integers that are a Nth power, in ascending order.
     */
    fun nDigitNthPowers(n: Int): List<Long> {
        val results = mutableListOf<Long>()
        for (x in 9 downTo 1) {
            var power = 1L
            // using pow would create scientific notation double at higher constraints
            // which would lose accuracy when converted to Long
            repeat(n) {
                power *= x
            }
            val digits = log10(1.0 * power).toInt() + 1
            if (digits == n) {
                results.add(power)
            } else {
                break
            }
        }
        return results.reversed()
    }

    /**
     * Project Euler specific implementation that requests a count of all N-digit positive
     * integers that are also a Nth power.
     *
     * N.B. The last 9-digit number to also be a 9th power occurs at N = 21.
     *
     * SPEED (WORSE) 9.0e4ns to count all valid numbers
     */
    fun allNDigitNthPowers(): Int {
        var total = 0
        var n = 1
        while (true) {
            var count = 0
            for (x in 9 downTo 1) {
                val power = (x.toDouble()).pow(n)
                val digits = log10(power).toInt() + 1
                if (digits == n) count++ else break
            }
            // if 9^N has less than N digits, no future integers exist
            if (count == 0) break
            total += count
            n++
        }
        return total
    }

    /**
     * Project Euler specific implementation that requests a count of all N-digit positive
     * integers that are also a Nth power.
     *
     * Solution is based on the following formula:
     *
     *      10^(n-1) <= x^n < 10^n, so find where the lower bounds meets x^n
     *
     *      10^(n-1) = x^n, using the quotient rule of exponentiation becomes
     *
     *      10^n / 10^1 = x^n
     *
     *      log(10)n / log(10) = log(x)n, using the quotient rule of logarithms becomes
     *
     *      log(10)n - log(10) = log(x)n
     *
     *      n = log(10) / (log(10) - log(x)) -> 1 / (1 - log(x))
     *
     * SPEED (BETTER) 3.0e4ns to count all valid numbers
     */
    fun allNDigitNthPowersFormula(): Int {
        var total = 0
        for (n in 1..9) {
            total += (1 / (1 - log10(1.0 * n))).toInt()
        }
        return total
    }
}