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
    fun spiralDiagSumBrute(n: BigInteger): BigInteger {
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
        return sum
    }

    /**
     * Works well up to N = 7001, then stack overflow error.
     */
    fun spiralDiagSumRecursive(n: BigInteger): BigInteger {
        return when(n) {
            BigInteger.ONE -> n
            else -> {
                var sum = BigInteger.ZERO
                var diag = n * n
                repeat(4) {
                    sum += diag
                    diag -= n - BigInteger.ONE
                }
                spiralDiagSumRecursive(n - BigInteger.TWO) + sum
            }
        }
    }

    fun spiralDiagSumFormula(n: BigInteger): BigInteger {
        var sum = BigInteger.ONE
        var i = BigInteger.ONE
        val maxI = n.divide(BigInteger.TWO).add(BigInteger.ONE)
        while (i < maxI) {
            val even = BigInteger.TWO * i
            val odd =  even + BigInteger.ONE
            sum += 4.toBigInteger() * odd.pow(2) - 6.toBigInteger() * even
            i = i.inc()
        }
        return sum
    }
}