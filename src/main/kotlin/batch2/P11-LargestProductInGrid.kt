package batch2

import util.IntMatrix2D
import util.product

/**
 * Problem 11: Largest Product in a Grid
 * Goal: Find the greatest product of 4 adjacent integers, with 0 <= N <= 100,
 * in the same direction (up, down, left, right, diagonal) in a 20x20 grid.
 * Test ans should be 73812150
 */

class LargestProductInGrid {
    fun maxProductSmallest(grid: IntMatrix2D): Int {
        return maxOf(
            getMaxProduct(grid.getDiagonals()),
            getMaxProduct(grid),
            getMaxProduct(grid.transpose())
        )
    }

    private fun getMaxProduct(iterable: Iterable<IntArray>): Int {
        var maxProd = 0
        for (intArray in iterable) {
            val prod = intArray.product()
            if (prod > maxProd) {
                maxProd = prod
            }
        }
        return maxProd
    }
}