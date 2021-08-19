package batch1

/**
 * Problem 8: Largest Product in a Series
 * Goal: Find the K adjacent digits in an N-digit number
 * with the greatest product, where 1 <= K <= 13 & K <= N <= 1000.
 * e.g. The greatest product of 5 adjacent digits in 3675356291
 * is 3150 from [7,5,3,5,6].
 */

// Receiver will not exceed 13 characters, so max
// would be all 9s, which the product is less than Long.MAX_VALUE
fun String.seriesProduct(): Long {
    return fold(1L) { acc, c ->
        if (c == '0') {
            return 0L
        } else {
            acc * c.digitToInt()
        }
    }
}

class LargestProductInSeries {
    fun largestSeriesProduct(number: String, digits: Int, series: Int): Long {
        return when {
            digits == 1 -> number.toLong()
            series == 1 -> number.maxOf { it.digitToInt() }.toLong()
            digits == series -> number.seriesProduct()
            else -> findLargestSeries(number, series).seriesProduct()
        }
    }

    private fun findLargestSeries(number: String, series: Int): String {
        return number.windowed(series).maxByOrNull { window ->
            window.seriesProduct()
        } ?: "0"
    }
}