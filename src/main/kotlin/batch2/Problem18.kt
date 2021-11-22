package batch2

import util.PyramidTree

/**
 * Problem 18: Maximum Path Sum 1
 *
 * https://projecteuler.net/problem=18
 *
 * Goal: Find the maximum sum from tree root to leaf node of an N-rowed tree,
 * which contains i integers on each ith row, by moving to an adjacent number
 * on the row below.
 *
 * Constraints: 1 <= N <= 15, numbers in [0,100)
 *
 * e.g.: N = 4
 *           3
 *          7 4
 *         2 4 6
 *        8 5 9 3
 *        max path -> 3+7+4+9
 *        output = 23
 */

class MaximumPathSum1 {
    fun maxPathSum(levels: Int, vararg elements: Int): Int {
        val tree = PyramidTree(levels, *elements)
        return tree.maxSumPostOrderTraversal()
    }
}