package batch2

import util.factorial
import java.math.BigInteger

/**
 * Problem 15: Lattice Paths
 * Goal: Find how many routes there are through a NxM grid, such that
 * 1 <= N <= 500 & 1 <= M <= 500, from (0,0) to (n,m) & only being
 * able to move right or down.
 * e.g. For a 2x2 grid, & only being able to move right & down,
 * there are 6 routes to get from the top left corner to the
 * bottom right corner {RRDD, RDRD, RDDR, DRRD, DRDR, DDRR}.
 * Test: 1x1 has 2. 3x3 has . 3x2 grid has 10 routes.
 */

class LatticePaths {

    /**
     * Example of permutations with identical items & uses the formula:
     * n! / Pi(i!), where n is the number of items to be combined & i is the
     * groups of indistinguishable items that must be multiplied.
     * e.g. 2x2 grid has 4 steps {R:2 , D: 2}. So, # of routes =
     * 4! / (2! * 2!) = 24 / 4 = 6
     * As number of routes may be very large, the amount returned is the total
     * routes % (10^9 + 7).
     */
    fun latticePathsReduced(n: Int, m: Int): BigInteger {
        val routes = (n + m).factorial() / (n.factorial() * m.factorial())
        return routes % (7 + 1_000_000_000).toBigInteger()
    }
}