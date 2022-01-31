package batch0

import util.maths.gaussianSum
import kotlin.math.pow

/**
 * Problem 6: Sum Square Difference
 *
 * https://projecteuler.net/problem=6
 *
 * Goal: Find the absolute difference between the sum of the squares of & the square of the sum
 * of the first N natural numbers.
 *
 * Constraints: 1 <= N <= 1e4
 *
 * e.g.: N = 3 -> {1,2,3}
 *       sum of squares = {1,4,9} = 14
 *       square of sum = 6^2 = 36
 *       diff = |14 - 36| = 22
 */

class SumSquareDifference {
    /**
     * SPEED (WORSE) 27.12ms for N = 1e4
     */
    fun sumSquareDiffBrute(n: Int): Long {
        val range: LongProgression = 1L..n
        val sumOfSquares = range.reduce { acc, i ->
            acc + i * i
        }
        val squareOfSum = (range.sum().toDouble()).pow(2).toLong()
        return squareOfSum - sumOfSquares
    }

    /**
     * The sum of the 1st [n] natural numbers (triangular numbers) is found using Gaussian Sum.
     *
     * The sum of the sequence's squares is based on the assumption that:
     *
     * f(n) = an^3 + bn^2 + cn + d, with f(0) = 0, f(1) = 1, f(2) = 5, f(3) = 14
     *
     * The formula (square pyramidal numbers) can then be solved as:
     *
     * f(n) = (2n^3 + 3n^2 + n)/6
     *
     * f(n) = (n(2n + 1)(n + 1))/6
     *
     * SPEED (BETTER) 1.70ms for N = 1e4
     */
    fun sumSquareDiff(n: Int): Long {
        val sumOfSquares: Double = (1.0 * n / 6) * (2 * n + 1) * (n + 1)
        val squareOfSum: Double = (n.gaussianSum().toDouble()).pow(2)
        return (squareOfSum - sumOfSquares).toLong()
    }
}