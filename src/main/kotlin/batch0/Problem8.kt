package batch0

/**
 * Problem 8: Largest Product in a Series
 *
 * https://projecteuler.net/problem=8
 *
 * Goal: Find the largest product of K adjacent digits in
 * an N-digit number.
 *
 * Constraints: 1 <= K <= 13, K <= N <= 1000
 *
 * e.g.: input = 3675356291; N = 10
 *       K = 5
 *       products LTR = {1890,3150,3150,900,1620,540}
 *       largest = 3150 -> {6*7*5*3*5} or {7*5*3*5*6}
 */


class LargestProductInSeries {

    /**
     * The constraints of this solution ensure that a substring will not
     * exceed 13 characters, so the max product of 13 '9's would be
     * less than Long.MAX_VALUE.
     */
    fun stringProduct(series: String): Long {
        return series.fold(1L) { acc, c ->
            if (c == '0') {
                return 0L // Prevents further folding
            } else {
                acc * c.digitToInt()
            }
        }
    }

    /**
     * Original solution did not use recursion, but instead used windowed() to map
     * stringProduct() then find maxOrNull() of resulting list.
     * Recursion provides improvement in performance of 56ms to 3ms (for a 1000-digit
     * number searching through 4-digit substrings).
     */
    fun largestSeriesProduct(number: String, digits: Int, seriesSize: Int): Long {
        return when {
            digits == 1 -> number.toLong()
            seriesSize == 1 -> number.maxOf { it.digitToInt() }.toLong()
            digits == seriesSize -> stringProduct(number)
            else -> {
                maxOf(
                    // first substring with k-adjacent digits
                    largestSeriesProduct(number.take(seriesSize), seriesSize, seriesSize),
                    // original string minus the first digit
                    largestSeriesProduct(number.drop(1), digits - 1, seriesSize)
                )
            }
        }
    }
}