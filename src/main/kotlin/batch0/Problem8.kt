package batch0

/**
 * Problem 8: Largest Product in a Series
 *
 * https://projecteuler.net/problem=8
 *
 * Goal: Find the largest product of K adjacent digits in an N-digit number.
 *
 * Constraints: 1 <= K <= 13, K <= N <= 1000
 *
 * e.g.: N = 10 with input = "3675356291", K = 5
 *       products LTR = {1890, 3150, 3150, 900, 1620, 540}
 *       largest = 3150 -> {6*7*5*3*5} or {7*5*3*5*6}
 */


class LargestProductInSeries {
    /**
     * The constraints of this solution ensure that [series] will not exceed 13 characters, so
     * the max product of 13 '9's would be less than Long.MAX_VALUE.
     */
    fun stringProduct(series: String): Long {
        return series.fold(1L) { acc, ch ->
            if (ch == '0') {
                return 0L // prevents further folding
            } else {
                acc * ch.digitToInt()
            }
        }
    }

    /**
     * SPEED (WORSE) 7.4e5ns for N = 1000, K = 4
     * SPEED (BETTER) 4.3e5ns for N = 1000, K = 13
     */
    fun largestSeriesProductRecursive(number: String, n: Int, k: Int): Long {
        return when {
            n == 1 -> number.toLong()
            k == 1 -> number.maxOf { it.digitToInt() }.toLong()
            n == k -> stringProduct(number)
            else -> {
                maxOf(
                    // first substring with k-adjacent digits
                    largestSeriesProduct(number.take(k), k, k),
                    // original string minus the first digit
                    largestSeriesProduct(number.drop(1), n - 1, k)
                )
            }
        }
    }

    /**
     * SPEED (BETTER) 4.9e5ns for N = 1000, K = 4
     * SPEED (WORSE) 6.8e5ns for N = 1000, K = 13
     */
    fun largestSeriesProduct(number: String, n: Int, k: Int): Long {
        return when (n) {
            1 -> number.toLong()
            k -> stringProduct(number)
            else -> {
                var largest = 0L
                for (i in 0..n - k) {
                    largest = maxOf(largest, stringProduct(number.substring(i, i + k)))
                }
                largest
            }
        }
    }
}