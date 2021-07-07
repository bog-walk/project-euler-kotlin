import kotlin.math.pow

/**
 * Problem 6: Sum Square Difference
 * Goal: Find the absolute difference between the sum of the squares & the square
 * of the sum of the first N natural numbers {1,2,3,..,N}, with 1 <= N <= 10^4.
 * e.g. 1st 10 -> sum of squares = 385, square of sum = 3025; diff = 2640.
 */

class SumSquareDifference {
    fun sumSquareDiffBrute(max: Int): Long {
        val range = 1..max
        val squareOfSums = (range.sum().toDouble()).pow(2).toLong()
        val sumOfSquares = range.fold(0L) { acc, i ->
            acc + i.toLong() * i.toLong()
        }
        return squareOfSums - sumOfSquares
    }

    /**
     * Sum of N = N(N + 1) / 2
     * Assuming sum of squares is of the form f(n) = an^3 + bn^2 + cn + d &
     * f(0) = 0, f(1) = 1, f(2) = 5, f(3) = 14; then
     * f(n) = 1/6(2n^3 + 3n^2 + n) = n/6(2n + 1)(n + 1)
     */
    fun sumSquareDiffImproved(max: Int): Long {
        val sum = 1L * max * (max + 1) / 2
        val squareOfSum: Double = (sum.toDouble()).pow(2)
        val sumOfSquares: Double = (1.0 * max / 6) * (2 * max + 1) * (max + 1)
        return (squareOfSum - sumOfSquares).toLong()
    }
}