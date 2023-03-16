package dev.bogwalk.batch1

import dev.bogwalk.util.custom.IntMatrix2D
import dev.bogwalk.util.custom.product

/**
 * Problem 11: Largest Product in a Grid
 *
 * https://projecteuler.net/problem=11
 *
 * Goal: Find the largest product of 4 adjacent integers in the same direction (up, down, left,
 * right, diagonal) in an NxN grid.
 *
 * Constraints: 0 <= integer <= 100, 4 <= N <= 20
 *
 * e.g.: 1 1 1 1
 *       1 1 2 1
 *       1 1 3 1
 *       1 1 1 1
 *       column {1,2,3,1} = 6
 */

class LargestProductInGrid {
    private val series: Int = 4

    /**
     * Solution uses custom class IntMatrix2D to abstract away most of the functions.
     *
     * SPEED (BETTER) 2.07ms for N = 20
     */
    fun largestProductInGridCustom(grid: IntMatrix2D): Int {
        return maxOf(
            getMaxProduct(grid),
            getMaxProduct(grid.transpose(), allDirections = false)
        )
    }

    private fun getMaxProduct(grid: IntMatrix2D, allDirections: Boolean = true): Int {
        var maxProd = 0
        for ((i, row) in grid.withIndex()) {
            var col = 0
            while (col <= row.size - series) {
                val right = row.sliceArray(col until col + series).product()
                var leadingDiag = 0
                var counterDiag = 0
                if (allDirections && i <= grid.size - series) {
                    leadingDiag = IntArray(series) { grid[i+it][col+it] }.product()
                    counterDiag = IntArray(series) { grid[i+it][col+series-1-it] }.product()
                }
                maxProd = maxOf(maxProd, right, leadingDiag, counterDiag)
                col++
            }
        }
        return maxProd
    }

    /**
     * SPEED (WORSE) 4.16ms for N = 20
     */
    fun largestProductInGrid(grid: Array<IntArray>): Int {
        var largest = 0
        for (row in grid.indices) {
            for (col in 0..(grid[0].size - series)) {
                val right = grid[row].sliceArray(col until (col + series)).product()
                val down = IntArray(series) { grid[col+it][row] }.product()
                var leadingDiag = 0
                var counterDiag = 0
                if (row <= grid.size - series) {
                    leadingDiag = IntArray(series) { grid[row+it][col+it] }.product()
                    counterDiag = IntArray(series) { grid[row+it][col+series-1-it] }.product()
                }
                largest = maxOf(largest, right, down, leadingDiag, counterDiag)
            }
        }
        return largest
    }
}