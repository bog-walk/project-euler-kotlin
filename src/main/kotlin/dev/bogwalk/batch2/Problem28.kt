package dev.bogwalk.batch2

import java.math.BigInteger
import kotlin.math.ceil

/**
 * Problem 28: Number Spiral Diagonals
 *
 * https://projecteuler.net/problem=28
 *
 * Goal: Return the sum (mod 1e9 + 7) of the diagonal numbers in an NxN grid that is generated
 * using a spiral pattern.
 *
 * Constraints: 1 <= N <= 1e18, with N always odd
 *
 * Spiral Pattern: Start with 1 & move to the right in a clockwise direction, incrementing the
 * numbers.
 *
 * e.g.: N = 5
 *       grid = 21  22  23  24  25
 *              20  7   8   9   10
 *              19  6   1   2   11
 *              18  5   4   3   12
 *              17  16  15  14  13
 *       diagonals = {1,3,5,7,9,13,17,21,25}
 *       sum = 101
 */

class NumberSpiralDiagonals {
    private val modulus = BigInteger.valueOf(1_000_000_007)

    /**
     * SPEED (BETTER) 303.11ms for N = 1e6 + 1
     *
     * @return Int value of result % (1e9 + 7).
     */
    fun spiralDiagSumBrute(n: Long): Int {
        var sum = BigInteger.ONE
        var num = BigInteger.ONE
        var step = BigInteger.TWO
        while (step < n.toBigInteger()) {
            repeat(4) {
                num += step
                sum += num
            }
            step += BigInteger.TWO
        }
        return sum.mod(modulus).toInt()
    }

    /**
     * Solution based on the formula:
     *
     * f(n) = 4(2n + 1)^2 - 12n + f(n - 1),
     *
     * with n as the centre ring number, and
     *
     * f(0) = 1, as it is the only element, and
     *
     * f(1) = 25, as the first ring creates a 3x3 grid.
     *
     * So the side of a ring is 2N + 1 wide with the upper right corner being (2n + 1)^2 or the
     * area. So [n] would need to be divided by 2.
     *
     * SPEED (WORST) 452.20ms for N = 1e6 + 1
     *
     * @return Int value of result % (1e9 + 7).
     * */
    fun spiralDiagSumFormulaBrute(n: Long): Int {
        var fN = BigInteger.ONE
        var num = BigInteger.ONE
        val maxNum = BigInteger.valueOf(ceil(n / 2.0).toLong())
        while (num < maxNum) {
            val even = BigInteger.TWO * num
            val odd =  even + BigInteger.ONE
            fN += 4.toBigInteger() * odd.pow(2) - 6.toBigInteger() * even
            num = num.inc()
        }
        return fN.mod(modulus).toInt()
    }

    /**
     * Solution optimised based on the same formula as above, but reduced to:
     *
     * f(n) = 16n^2 + 4n + 4 + f(n - 1)
     *
     * Third order polynomial function required as the 3rd delta between consecutive f(n) gives a
     * constant, such that ->
     *
     * ax^3 + bx^2 + cx + d
     *
     * Solving for f(0) to f(3) derives the closed-form formula:
     *
     * f(n) = (16n^3 + 30n^2 + 26n + 3) / 3
     *
     * SPEED (BEST) 1.2e5ns for N = 1e6 + 1
     *
     * @return Int value of result % (1e9 + 7).
     */
    fun spiralDiagSumFormulaDerived(n: Long): Int {
        val x = BigInteger.valueOf((n - 1) / 2)
        var sum = 16.toBigInteger() * x.pow(3) + 30.toBigInteger() * x.pow(2)
        sum += 26.toBigInteger() * x + 3.toBigInteger()
        sum /= 3.toBigInteger()
        return sum.mod(modulus).toInt()
    }
}