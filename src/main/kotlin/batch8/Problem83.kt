package batch8

import java.util.*

/**
 * Problem 83: Path Sum 4 Ways
 *
 * https://projecteuler.net/problem=83
 *
 * Goal: Find the minimum path sum for an NxN grid, starting at (0,0) and ending at (n,n), by
 * moving left, right, up, or down with each step.
 *
 * Constraints: 1 <= N <= 1000, numbers in [1, 1e9]
 *
 * e.g.: N = 3
 *       grid = 2 0 3 5
 *              8 0 9 9
 *              0 3 9 9
 *              0 1 1 1
 *       minimum = 8: {2 -> R -> D -> D -> L -> D -> R -> R -> 1}
 */

class PathSum4Ways {
    /**
     * Solution isolates the final column of the grid & each of this array's elements iteratively
     * becomes the sum bubbled up by the minimal path encountered moving backwards through the
     * columns.
     *
     * N.B. The nested arrays have to be cloned, otherwise they will reference and alter the
     * original array, causing errors when testing a single grid with multiple solutions. An
     * alternative would be to provide the grid as a List<MutableList<Long>> & process as such or
     * cast to a 2D array.
     *
     * SPEED (BETTER) 1.49ms for N = 80
     */
    fun minPathSum(rows: Int, grid: Array<LongArray>): Long {
        val sums = LongArray(rows) { grid[it].clone()[rows-1] }
        for (i in rows - 2 downTo 0) {
            sums[0] += grid[0][i]
            for (j in 1 until rows) {
                // i.e. between the right or down
                sums[j] = minOf(sums[j], sums[j-1]) + grid[j][i]
            }
            for (j in rows - 2 downTo 0) {
                // bubble up the minimal last sums with the rightmost element
                sums[j] = minOf(sums[j], sums[j+1] + grid[j][i])
            }
        }
        return sums.minOrNull()!!
    }

    /**
     * Solution is identical to the breadth-first search solution used in Problem 81, except
     * extra steps (left & up) are added to the PriorityQueue.
     *
     * N.B. The nested arrays have to be cloned, otherwise they will reference and alter the
     * original array, causing errors when testing a single grid with multiple solutions. An
     * alternative would be to provide the grid as a List<MutableList<Long>> & process as such or
     * cast to a 2D array.
     *
     * SPEED (WORSE) 31.32ms for N = 80
     */
    fun minPathSumBFS(rows: Int, grid: Array<LongArray>): Long {
        val elements = Array(rows) { grid[it].clone() }
        val visited = Array(rows) { BooleanArray(rows) }
        val compareByWeight = compareBy<Triple<Int, Int, Long>> { it.third }
        val queue = PriorityQueue(compareByWeight).apply {
            add(Triple(0, 0, elements[0][0]))
        }
        var minSum = 0L
        while (queue.isNotEmpty()) {
            val (row, col, weight) = queue.poll()
            if (visited[row][col]) continue
            if (row == rows - 1 && col == rows - 1) {
                minSum = weight
                break
            }
            visited[row][col] = true
            if (col - 1 >= 0) {
                queue.add(Triple(row, col - 1, weight + elements[row][col-1]))
            }
            if (row - 1 >= 0) {
                queue.add(Triple(row - 1, col, weight + elements[row - 1][col]))
            }
            if (col + 1 < rows) {
                queue.add(Triple(row, col + 1, weight + elements[row][col+1]))
            }
            if (row + 1 < rows) {
                queue.add(Triple(row + 1, col, weight + elements[row + 1][col]))
            }
        }
        return minSum
    }
}

fun main() {
    val tool = PathSum4Ways()
    val n = 3
    val grid = arrayOf(
        longArrayOf(2, 0, 3, 5),
        longArrayOf(8, 0, 9, 9),
        longArrayOf(0, 3, 9, 9),
        longArrayOf(0, 1, 1, 1)
    )
    println(tool.minPathSumBFS(n, grid))
}