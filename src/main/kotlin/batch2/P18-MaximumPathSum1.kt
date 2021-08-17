package batch2

import util.PyramidTree

/**
 * Problem 18: Maximum Path Sum 1
 * Goal: Find the maximum sum from tree root to leaf node of a tree
 * of 1 <= N <= 15 rows, with each ith row containing i integers, and
 * all integers members of [0, 100). Path moves between adjacent numbers,
 */

class MaximumPathSum1 {
    fun maxPathSum(levels: Int, vararg elements: Int): Int {
        val tree = PyramidTree(levels, *elements)
        return tree.maxSumPostOrderTraversal()
    }
}