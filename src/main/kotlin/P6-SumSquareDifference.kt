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
        val sumOfSquares = range.sumOf {
            (it.toDouble()).pow(2).toLong()
        }
        return squareOfSums - sumOfSquares
    }
}