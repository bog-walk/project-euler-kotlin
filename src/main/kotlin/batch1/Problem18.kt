package batch1

import util.custom.PyramidTree
import kotlin.math.max

/**
 * Problem 18: Maximum Path Sum 1
 *
 * https://projecteuler.net/problem=18
 *
 * Goal: Find the maximum sum from tree root to leaf node of an N-rowed tree, which contains i
 * integers on each ith row, by moving to an adjacent number on the row below.
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
    /**
     * Generates a graph using custom PyramidTree class & finds the largest valued route using
     * post-order traversal.
     *
     * SPEED (WORSE) 6.09ms for N = 15
     */
    fun maxPathSum(rows: Int, vararg elements: Int): Int {
        val tree = PyramidTree(rows, *elements)
        return tree.maxSumPostOrderTraversal()
    }

    /**
     * Rather than create a custom tree, this solution starts from the bottom of the tree (last
     * nested list), repeatedly finding the maximum of adjacent values and adding it to the
     * parent value 1 row above, until the tree root is reached. This new root value is returned
     * as the maximum path value.
     *
     * SPEED (BETTER) 1.7e5ns for N = 15
     */
    fun maxPathSumDynamic(rows: Int, elements: Array<IntArray>): Int {
        for (row in (rows - 1) downTo 1) {
            for (col in 0 until row) {
                elements[row-1][col] += max(elements[row][col], elements[row][col+1])
            }
        }
        return elements[0][0]
    }
}