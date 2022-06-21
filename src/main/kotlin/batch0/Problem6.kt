package batch0

import util.maths.gaussSum

/**
 * Problem 6: Sum Square Difference
 *
 * https://projecteuler.net/problem=6
 *
 * Goal: Find the absolute difference between the sum of the squares & the square of the sum
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
     * SPEED (WORST) 7.55ms for N = 1e4
     */
    fun sumSquareDiffBruteOG(n: Int): Long {
        val range = 1L..n
        val sumOfRange = range.sum()
        val sumOfSquares = range.sumOf { it * it }
        return sumOfRange * sumOfRange - sumOfSquares
    }

    /**
     * SPEED (BETTER) 5.15ms for N = 1e4
     */
    fun sumSquareDiffBrute(n: Int): Long {
        val range = 2L..n
        val (sumOfRange, sumOfSquares) = range.fold(1L to 1L) { acc, num ->
            acc.first + num to acc.second + num * num
        }
        return sumOfRange * sumOfRange - sumOfSquares
    }

    /**
     * The sum of the 1st [n] natural numbers (triangular numbers) is found using the gauss
     * summation method.
     *
     * The sum of the sequence's squares is based on the assumption that:
     *
     *      f(n) = an^3 + bn^2 + cn + d, with f(0) = 0, f(1) = 1, f(2) = 5, f(3) = 14
     *
     * The formula (square pyramidal numbers) can then be solved as:
     *
     *      f(n) = (2n^3 + 3n^2 + n)/6
     *
     *      f(n) = (n(2n + 1)(n + 1))/6
     *
     * SPEED (BEST) 2.9e+04ns for N = 1e4
     */
    fun sumSquareDiff(n: Int): Long {
        val sumOfRange = n.gaussSum()
        val sumOfSquares: Double = n / 6.0 * (2 * n + 1) * (n + 1)
        return sumOfRange * sumOfRange - sumOfSquares.toLong()
    }
}