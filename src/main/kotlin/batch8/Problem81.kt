package batch8

import java.util.PriorityQueue

/**
 * Problem 81: Path Sum 2 Ways
 *
 * https://projecteuler.net/problem=81
 *
 * Goal: Find the minimum path sum for an NxN grid, starting at (0,0) and ending at (n,n), while
 * only being able to move to the right or down with each step.
 *
 * Constraints: 1 <= N <= 1000, numbers in [1, 1e9]
 *
 * e.g.: N = 3
 *       grid = 1 0 5
 *              1 0 0
 *              1 1 1
 *       minimum = 2: {1 -> R -> D -> R -> D -> 1}
 */

class PathSum2Ways {
    /**
     * Solution starts at top-left corner & works downwards replacing each grid value with the
     * smallest cumulative sum so far.
     *
     * The first row is treated specially in that sums can only be achieved by adding the value
     * from the previous column. The first column is also special in that sums can only be
     * achieved by adding the value from the previous row.
     *
     * N.B. The nested arrays have to be cloned, otherwise they will reference and alter the
     * original array, causing errors when testing a single grid with multiple solutions. An
     * alternative would be to provide the grid as a List<MutableList<Long>> & process as such or
     * cast to a 2D array.
     *
     * SPEED (BETTER) 1.46ms for N = 80
     */
    fun minPathSum(rows: Int, grid: Array<LongArray>): Long {
        val elements = Array(rows) { grid[it].clone() }
        for (row in 0 until rows) {
            for (col in 0 until rows) {
                if (row == 0) {
                    if (col == 0) continue // no need to alter starter
                    elements[row][col] += elements[row][col-1]
                } else {
                    if (col == 0) {
                        elements[row][col] += elements[row-1][col]
                    } else {
                        elements[row][col] += minOf(elements[row-1][col], elements[row][col-1])
                    }
                }
            }
        }
        return elements[rows-1][rows-1]
    }

    /**
     * Solution uses breadth-first search via a PriorityQueue that intrinsically stores the
     * smallest weighted grid element encountered at its head. A step is taken to the right and
     * down, if either are possible, on each smallest polled element until the bottom-right
     * corner is found.
     *
     * This PriorityQueue stores its elements based on the grid element's weight, as described by
     * a Comparator. This means that equivalent grid elements will then be sorted naturally by
     * their first & second element, which will not cause errors in the end, but may cause
     * unnecessary processing.
     *
     * e.g. When using the 3X3 grid in the test suite, after grid[1][2] is encountered &
     * grid[2][2] is added to the queue, the next smallest & valid element should be grid[2][2],
     * but its value is identical with the sum at grid[2][1], which is prioritised as being lesser
     * because it's column value is smaller. This could be avoided by adding .thenByDescending
     * comparators to account for the other 2 components, but this increases the execution time ~4x.
     *
     * N.B. The nested arrays have to be cloned, otherwise they will reference and alter the
     * original array, causing errors when testing a single grid with multiple solutions. An
     * alternative would be to provide the grid as a List<MutableList<Long>> & process as such or
     * cast to a 2D array.
     *
     * SPEED (WORSE) 34.34ms for N = 80
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