package dev.bogwalk.batch8

import kotlin.math.sqrt

/**
 * Problem 88: Product-Sum Numbers
 *
 * https://projecteuler.net/problem=88
 *
 * Goal: Find the sum of all unique product-sum numbers for 2 <= k <= N.
 *
 * Constraints: 10 <= N <= 2e5
 *
 * Product-Sum Number: A natural number that can be written as both the sum and the product of a
 * given set of at least 2 natural numbers, such that N = a1+a2+..+ak = a1*a2*..*ak.
 *
 * Minimal Product-Sum Number: For a given set of size k, the smallest N that is a product-sum
 * number; e.g. k = 2: 4 = 2 + 2 + 2 * 2
 *              k = 3: 6 = 1 + 2 + 3 = 1 * 2 * 3
 *              k = 4: 8 = 1 + 1 + 2 + 4 = 1 * 1 * 2 * 4
 *              k = 5: 8 = 1 + 1 + 2 + 2 + 2 = 1 * 1 * 2 * 2 * 2
 *              k = 6: 12 = 1 + 1 + 1 + 1 + 2 + 6 = 1 * 1 * 1 * 1 * 2 * 6
 *              Therefore, for 2 <= k <= 6, the sum of unique minimal product-sum numbers is 30.
 *
 * e.g.: N = 12
 *       2 <= k <= 12 -> {4, 6, 8, 8, 12, 12, 12, 15, 16, 16, 16}
 *       sum = 61
 */

class ProductSumNumbers {
    private val limit = 200_000
    // exclude 0 and 1 from cache, so minimalPS for k found at cache[k-2]
    private val minimalPSCache = IntArray(limit - 1) { Int.MAX_VALUE }

    fun sumOfPSNumbers(maxK: Int): Int {
        return minimalPSCache.sliceArray(0..maxK-2).toSet().sum()
    }

    fun generatePSNumbers() {
        var generated = 0
        var num = 4  // minimalPSNum for k = 2
        while (generated < limit - 1) {
            // a number can be a minimalPSNum for multiple k
            generated += countKIfPS(num, num, num)
            num++
        }
    }

    /**
     * Recursively remove factors from a number (and from its corresponding sum) & store it if it
     * is a valid product-sum number for any k.
     */
    private fun countKIfPS(
        n: Int, product: Int, sum: Int, factors: Int = 0, minFactor: Int = 2
    ): Int {
        if (product == 1) {  // remaining sum must be all 1s
            return if (isMinimalPSNum(n, factors + sum)) 1 else 0
        }

        var result = 0
        if (factors > 0) {  // at least 1 factor has been removed
            if (product == sum) {
                return if (isMinimalPSNum(n, factors + 1)) 1 else 0
            }
            if (isMinimalPSNum(n, factors + sum - product + 1)) {
                result++
            }
        }

        for (i in minFactor..sqrt(1.0 * product).toInt()) {
            if (product % i == 0) {
                result += countKIfPS(n, product / i, sum - i, factors + 1, i)
            }
        }

        return result
    }

    private fun isMinimalPSNum(n: Int, k: Int): Boolean {
        if (k > limit) return false

        return if (n < minimalPSCache[k-2]) {
            minimalPSCache[k-2] = n
            true
        } else false
    }
}