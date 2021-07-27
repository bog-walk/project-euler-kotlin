package batch2

import util.IntMatrix2D

/**
 * Problem 11: Largest Product in a Grid
 * Goal: Find the greatest product of 4 adjacent integers, with 0 <= N <= 100,
 * in the same direction (up, down, left, right, diagonal) in a 20x20 grid.
 * Test ans should be 73812150
 */

class LargestProductInGrid {
    fun maxProductSmallest(grid: IntMatrix2D): Int {
        var maxProd = 0
        for (diagonal in grid.getDiagonals()) {
            val prod = grid.product(diagonal)
            if (prod > maxProd) {
                maxProd = prod
            }
        }
        for (row in grid.iterator()) {
            val prod = grid.product(row)
            if (prod > maxProd) {
                maxProd = prod
            }
        }
        for (row in grid.transpose().iterator()) {
            val prod = grid.product(row)
            if (prod > maxProd) {
                maxProd = prod
            }
        }
        return maxProd
    }
}