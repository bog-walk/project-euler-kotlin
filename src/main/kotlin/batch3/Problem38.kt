package batch3

import util.strings.isPandigital

/**
 * Problem 38: Pandigital Multiples
 *
 * https://projecteuler.net/problem=38
 *
 * Goal: Find all multipliers M below N that provide a K-pandigital concatenated product when
 * used with a multiplicand starting with 1 onwards.
 *
 * Constraints: 100 <= N <= 1e5, K in {8, 9} and 1 < M
 *
 * e.g.: N = 100, K = 8
 *       multipliers = {18, 78}, since:
 *       18 * (1, 2, 3, 4) -> 18|36|54|72
 *       78 * (1, 2, 3) -> 78|156|234
 */

class PandigitalMultiples {
    /**
     * Since a 9-digit pandigital number is the limit, the multiplier will never be larger than 4
     * digits (as a 5-digit number times 2 would produce another 5-digit number).
     *
     * N.B. The logic behind the inner loop breaks could all be replaced by the isPandigital()
     * helper function used in the PE implementation at the bottom.
     */
    fun findPandigitalMultipliers(n: Int, k: Int): List<Int> {
        val multipliers = mutableListOf<Int>()
        for (num in 2..(minOf(n, 9876))) {
            var concat = ""
            var multiplicand = 1
            while (concat.length < k) {
                val product = (num * multiplicand).toString()
                // ensure result only 1 to k pandigital
                if (
                    product.any { ch ->
                        ch == '0' || ch > k.digitToChar() || ch in concat
                    }
                ) break
                // avoid products that contain duplicate digits
                if (product.length != product.toSet().size) break
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
     * Project Euler specific implementation that finds the largest 1 to 9 pandigital number that
     * can be formed as a concatenated product.
     *
     * Solution is based on the following:
     *
     *  - Since the multiplier must at minimum be multiplied by both 1 & 2, it cannot be larger
     *  than 4 digits to ensure product is only 9 digits.
     *
     *  - The default largest would be M = 9 * (1, 2, 3, 4, 5) = 918_273_645, so M must begin
     *  with the digit 9.
     *
     *  - The 2-digit minimum (M = 91) would result in either an 8-/11-digit product once
     *  multiplied by 3 and 4. The 3-digit minimum (M = 912) would result in either a 7-/11-digit
     *  product once multiplied by 2 and 3.
     *
     *  - So M must be a 4-digit number multiplied by 2 to get a 9-digit product and at minimum
     *  will be 9182 (1st 4 digits of default) and at max 9876.
     *
     *  - Multiplying 9xxx by 2 will at minimum result in 18xxx, always generating a new digit 1,
     *  so M cannot itself contain the digit 1, setting the new minimum to 9234.
     *
     *  - Lastly, multiplying [98xx, 95xx, -1] by 2 will at minimum result in 19xxx, always
     *  generating another digit 9, so M's 2nd digit must be < 5, setting the new maximum to 9487.
     */
    fun largest9Pandigital(): String {
        var largest = ""
        for (m in 9487 downTo 9234) {
            largest = "${m}${m * 2}"
            if (largest.isPandigital(9)) break
        }
        return largest
    }
}