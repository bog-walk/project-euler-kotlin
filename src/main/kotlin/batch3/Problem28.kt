package batch3

import java.math.BigInteger

/**
 * Problem 28: Number Spiral Diagonals
 *
 * https://projecteuler.net/problem=28
 *
 * Goal: Return the sum (mod 1e9 + 7) of the diagonal numbers
 * in a N x N grid that was generated using a spiral pattern.
 *
 * Constraints: 1 <= N <= 1e18, with N always odd
 *
 * Spiral Pattern: start with 1 & move to the right in a
 * clockwise direction, incrementing the numbers.
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
    private val modulus = 1000000000.toBigInteger() + BigInteger.valueOf(7)

    /**
     * @return Int value of result % (1e9 + 7)
     *
     * SPEED: 9.6e4ms for N = 1e9
     */
    fun spiralDiagSumBrute(n: BigInteger): Int {
        var sum = BigInteger.ONE
        var num = BigInteger.ONE
        var step = BigInteger.TWO
        while (step < n) {
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
     * f(n) = 4 * (2 * n + 1)^2 - 12 * n + f(n - 1)
     * This brute version assumes n is the ring number, with f(0) = 1 & f(1) = 25
     * as the first ring creates a 3x3 grid. So the side of a ring is
     * 2N + 1 wide with the upper right corner being (2n + 1)^2 or the area.
     * So provided n would need to be divided by 2.
     *
     * @return Int value of result % (1e9 + 7)
     *
     * SPEED: 1.1e5ms for N = 1e9
     * */
    fun spiralDiagSumFormula(n: BigInteger): Int {
        var sum = BigInteger.ONE
        var i = BigInteger.ONE
        val maxI = n.divide(BigInteger.TWO)
        while (i < maxI) {
            val even = BigInteger.TWO * i
            val odd =  even + BigInteger.ONE
            sum += 4.toBigInteger() * odd.pow(2) - 6.toBigInteger() * even
            i = i.inc()
        }
        return sum.mod(modulus).toInt()
    }

    /**
     * Solution based on the formula:
     * f(n) = 4 * (2 * n + 1)^2 - 12 * n + f(n - 1)
     * This derived version uses a 3rd order polynomial function as the 3rd delta
     * between consecutive f(n) giving a constant -> a*x^3 + b*x^2 + c*x + d.
     * Solving for f(0) to f(3) derives the formula:
     * f(n) = (16 * x^3 + 30 * x^2 + 26 * x + 3) // 3
     *
     * @return Int value of result % (1e9 + 7)
     *
     * SPEED (BEST): 2.1e5ns for N = 1e9
     */
    fun spiralDiagSumFormulaDerived(n: BigInteger): Int {
        val x = (n - BigInteger.ONE) / BigInteger.TWO
        var sum = 16.toBigInteger() * x.pow(3) + 30.toBigInteger() * x.pow(2)
        sum += 26.toBigInteger() * x + 3.toBigInteger()
        sum /= 3.toBigInteger()
        return sum.mod(modulus).toInt()
    }
}