package dev.bogwalk.batch1

import dev.bogwalk.util.combinatorics.binomialCoefficient
import java.math.BigInteger

/**
 * Problem 15: Lattice Paths
 *
 * https://projecteuler.net/problem=15
 *
 * Goal: Find the number of routes through an NxM grid, starting at (0,0) & ending at (n,m),
 * while only being able to move right or down.
 *
 * Constraints: 1 <= N <= 500, 1 <= M <= 500
 *
 * e.g.: N = 2, M = 2
 *       routes = 6 -> {RRDD, RDRD, RDDR, DRRD, DRDR, DDRR}
 */

class LatticePaths {
    private val mod = 1_000_000_007

    /**
     * Calculates distinct permutations with identical items.
     *
     * Solution is based on the formula:
     *
     *      x! / Pi(i!)
     *
     * where x is the number of items to be combined & i represents the groups of
     * indistinguishable items to undergo product notation.
     *
     * Note that, if the lattice was assured to be square, the number of routes would be equal to
     * the central binomial coefficient C(2n, n) found as the midline number in the (2n)th row
     * of Pascal's triangle.
     *
     * The formula for a rectangular grid with C(n+m, n) becomes:
     *
     *      (n + m)! / n!m!
     *
     * since grid dimensions determine the number of steps taken & there is a deterministic
     * proportion of R vs D steps.
     *
     * @return  number of valid routes scaled down to modulo (1e9 + 7).
     */
    fun latticePathRoutes(n: Int, m: Int): BigInteger {
        val routes = binomialCoefficient(n + m, m)
        return routes % mod.toBigInteger()
    }

    /**
     * Solution uses breadth-first search summation to generate a graph that contains all counts
     * of possible paths from the start node (0, 0 or top left corner) to the node at index[n][m].
     *
     * The original lattice is converted to a graph representation of its points (nodes) instead
     * of its edges. For example, exploring each node at each depth results in the following
     * cumulative count:
     *
     *      given N = 2, M = 2
     *        1
     *       1 1
     *      1 2 1
     *       3 3
     *        6 -> total count of paths to bottom right corner
     */
    fun latticePathRoutesBFS(n: Int, m: Int): Array<LongArray> {
        // goal is bottom right (outer) corner, so an extra node exists for each outer edge
        val lattice = Array(n + 1) { LongArray(m + 1) }.apply {
            // path from root to itself
            this[0][0] = 1L
        }

        // create queue to keep track of encountered but unexplored adjacent nodes
        val unvisited = ArrayDeque(listOf(1 to 0, 0 to 1))
        while (!unvisited.isEmpty()) {
            val (row, col) = unvisited.removeFirst()
            // already explored paths from root to this node
            if (lattice[row][col] != 0L) continue
            var routes = 0L
            if (row > 0) {
                routes += lattice[row - 1][col]
            }
            if (col > 0) {
                routes += lattice[row][col - 1]
            }
            routes %= mod
            lattice[row][col] = routes
            // queue next 2 adjacent nodes (down & right) if they exist
            if (row < n) {
                unvisited.add(row + 1 to col)
            }
            if (col < m) {
                unvisited.add(row to col + 1)
            }
        }

        return lattice
    }
}