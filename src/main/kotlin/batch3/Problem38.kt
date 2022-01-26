package batch3

/**
 * Problem 38: Pandigital Multiples
 *
 * https://projecteuler.net/problem=38
 *
 * Goal: Find all multipliers M below N that provide a K-pandigital concatenated
 * product when used with a multiplicand starting with 1 onwards.
 *
 * Constraints: 100 <= N <= 1e5 and K in {8, 9} and 1 < M
 *
 * e.g.: N = 100, K = 8
 *       multipliers = {18, 78}, since:
 *       18 * (1, 2, 3, 4) -> 18|36|54|72
 *       78 * (1, 2, 3) -> 78|156|234
 */

class PandigitalMultiples {
    /**
     * Since a 9-digit pandigital number is the limit, the multiplier will
     * never be larger than 4 digits (as a 5-digit number times 2 would give
     * another 5-digit number).
     */
    fun findPandigitalMultipliers(n: Int, k: Int): List<Int> {
        val multipliers = mutableListOf<Int>()
        for (num in 2..(minOf(n, 9876))) {
            var concat = ""
            var multiplicand = 1
            while (concat.length < k) {
                val product = (num * multiplicand).toString()
                // ensure result only 1 to k pandigital
                val invalid = product.any { ch ->
                    ch == '0' || ch > k.digitToChar() || ch in concat
                }
                // avoid products that contain duplicate digits
                if (invalid || product.length != product.toSet().size) break
                concat += product
                multiplicand++
            }
            if (concat.length == k) {
                multipliers.add(num)
            }
        }
        return multipliers
    }

    /**
     * Project Euler specific implementation that finds the largest
     * 1 to 9 pandigital number that can be formed as a concatenated product.
     * Solution is based on the following:
     *
     * - Since the multiplier must at minimum be multiplied by both 1 & 2, it
     * cannot be larger than 4 digits to ensure product is only 9 digits.
     *
     * - The default largest would be M = 9 * (1, 2, 3, 4, 5) = 918273645, so
     * M must begin with the digit 9.
     *
     * - The 2-digit minimum (M = 91) would result in either an 8-/11-digit
     * product once multiplied by 3 and 4. The 3-digit minimum (M = 912) would
     * result in either a 7-/11-digit product once multiplied by 2 and 3.
     *
     * - So M must be a 4-digit number multiplied by 2 to get a 9-digit product
     * and at minimum will be 9182 (1st 4 digits of default) and at max 9876.
     *
     * - Multiplying 9xxx by 2 will at minimum result in 18xxx, always
     * generating a new digit 1, so M cannot itself contain the digit 1, setting
     * the new minimum to 9234.
     *
     * - Lastly, multiplying (98xx downTo 95xx) by 2 will at minimum result in 19xxx,
     * always generating another digit 9, so M's 2nd digit must be < 5, setting
     * the new maximum to 9487.
     */
    fun largest9Pandigital(): String {
        val pandigital = ('1'..'9').toList()
        var largest = ""
        for (m in 9487 downTo 9234) {
            largest = "${m}${m * 2}"
            if (pandigital == largest.toList().sorted()) break
        }
        return largest
    }
}