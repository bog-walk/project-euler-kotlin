package batch8

import java.util.PriorityQueue

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
     * Solution is identical to the Dijkstra solution used above & also in Problem 81, except
     * extra steps (left & up) are added to the PriorityQueue, instead of using an adjacency matrix.
     *
     * N.B. The nested arrays have to be cloned, otherwise they will reference and alter the
     * original array, causing errors when testing a single grid with multiple solutions. An
     * alternative would be to provide the grid as a List<MutableList<Long>> & process as such or
     * cast to a 2D array.
     *
     * SPEED (WORSE) 109.45ms for N = 80
     */
    fun minPathSumDijkstra(rows: Int, grid: Array<IntArray>): Long {
        val visited = Array(rows) { BooleanArray(rows) }
        val compareByWeight = compareBy<Triple<Int, Int, Long>> { it.third }
        val queue = PriorityQueue(compareByWeight).apply {
            add(Triple(0, 0, grid[0][0].toLong()))
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
                queue.add(Triple(row, col - 1, weight + grid[row][col-1]))
            }
            if (row - 1 >= 0) {
                queue.add(Triple(row - 1, col, weight + grid[row - 1][col]))
            }
            if (col + 1 < rows) {
                queue.add(Triple(row, col + 1, weight + grid[row][col+1]))
            }
            if (row + 1 < rows) {
                queue.add(Triple(row + 1, col, weight + grid[row + 1][col]))
            }
        }
        return minSum
    }

    /**
     * Dijkstra's algorithm for finding the shortest paths between nodes in a graph involves
     * using 2 arrays to store the distance from the source (sum in this case) for each vertex &
     * whether each vertex has been processed. Using a PriorityQueue is faster than storing the sums
     * & writing a function that finds the matrix element with the smallest sum so far.
     *
     * Since only the smallest sum path between the source and the target is required, regardless
     * of the length of the path (how many steps it takes), the loop can be broken once the
     * target element is reached.
     *
     * An adjacency matrix is initially created to link all neighbour vertices, instead of
     * relying on conditional branches to generate the PriorityQueue.
     *
     * SPEED (BETTER) 97.71ms for N = 80
     */
    fun minPathSumDijkstraImproved(rows: Int, grid: Array<IntArray>): Long {
        val adjacents = makeAdjacencyMatrix(rows, grid)
        val visited = Array(rows) { BooleanArray(rows) }
        val compareByWeight = compareBy<Triple<Int, Int, Long>> { it.third }
        val queue = PriorityQueue(compareByWeight).apply {
            add(Triple(0, 0, grid[0][0].toLong()))
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
            for ((adjR, adjC, adjW) in adjacents[row][col]) {
                if (!visited[adjR][adjC] && weight != Long.MAX_VALUE) {
                    queue.add(Triple(adjR, adjC, weight + adjW))
                }
            }
        }
        return minSum
    }

    private fun makeAdjacencyMatrix(
        rows: Int,
        grid: Array<IntArray>
    ): Array<Array<List<Triple<Int, Int, Long>>>> {
        // step up, left, down, right
        val directions = listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
        return Array(rows) { row ->
            Array(rows) { col ->
                directions.mapNotNull { (x, y) ->
                    val (adjR, adjC) = row + x to col + y
                    if (adjR in 0 until rows && adjC in 0 until rows) {
                        Triple(adjR, adjC, grid[adjR][adjC].toLong())
                    } else null
                }
            }
        }
    }
}