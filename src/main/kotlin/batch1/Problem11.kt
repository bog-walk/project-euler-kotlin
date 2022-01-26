package batch1

import util.IntMatrix2D
import util.RollingQueue
import util.product

/**
 * Problem 11: Largest Product in a Grid
 *
 * https://projecteuler.net/problem=11
 *
 * Goal: Find the largest product of 4 adjacent integers in the
 * same direction (up, down, left, right, diagonal) in an NxN grid.
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

    /**
     * Solution using custom class IntMatrix2D.
     */
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

    /**
     * Solution using Array<IntArray>
     */
    fun maxFromGrid(grid: Array<IntArray>): Int {
        return maxOf(
            assessRows(grid),
            assessCols(grid),
            assessDiagonals(grid)
        )
    }

    fun assessRows(grid: Array<IntArray>): Int {
        var largest = 0
        val adjacent = RollingQueue<Int>(4)
        for (row in grid) {
            for (col in 0..(row.size - 4)) {
                if (col == 0) {
                    adjacent.addAll(row.slice(0..3))
                } else {
                    adjacent.add(row[col + 3])
                }
                val prod = adjacent.reduce { acc, n -> acc * n }
                if (prod > largest) largest = prod
            }
        }
        return largest
    }

    fun assessCols(grid: Array<IntArray>): Int {
        return assessRows(transpose(grid))
    }

    fun assessDiagonals(grid: Array<IntArray>): Int {
        val transposed = transpose(grid)
        return maxOf(
            assessLeadingDiagonals(grid),
            assessLeadingDiagonals(transposed),
            assessCounterDiagonals(grid),
            assessCounterDiagonals(transposed)
        )
    }

    private fun assessLeadingDiagonals(grid: Array<IntArray>): Int {
        var largest = 0
        val adjacent = RollingQueue<Int>(4)
        for (row in 0..(grid.size - 4)) {
            for (col in 0..(grid.size - 4 - row)) {
                if (col == 0) {
                    adjacent.addAll(listOf(
                        grid[row + col][col], grid[row + 1][1],
                        grid[row + 2][2], grid[row + 3][3])
                    )
                } else {
                    adjacent.add(grid[row + col + 3][col + 3])
                }
                val prod = adjacent.reduce { acc, n -> acc * n }
                if (prod > largest) largest = prod
            }
        }
        return largest
    }

    private fun assessCounterDiagonals(grid: Array<IntArray>): Int {
        return assessLeadingDiagonals(rotate(grid))
    }

    private fun transpose(grid: Array<IntArray>): Array<IntArray> {
        return Array(grid[0].size) { r ->
            IntArray(grid.size) { c ->
                grid[c][r]
            }
        }
    }

    private fun rotate(grid: Array<IntArray>): Array<IntArray> {
        return Array(grid[0].size) { r ->
            IntArray(grid.size) { c ->
            grid[c][grid.size - r - 1]
        } }
    }
}