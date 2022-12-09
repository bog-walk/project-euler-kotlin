package dev.bogwalk.batch8

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Problem 85: Counting Rectangles
 *
 * https://projecteuler.net/problem=85
 *
 * Goal: Find the area of a grid (made up of 1x1 squares) that can contain a number of rectangles
 * closest to T. If multiple such areas exist, output the one with the largest area.
 *
 * Constraints: 1 <= T <= 2e6
 *
 * For reference, a 3x2 grid contains 18 rectangles: {6 1x1 squares, 4 2x1 rectangles, 2 3x1
 * rectangles, 3 2x1 rectangles, 2 2x2 squares, and the 3x2 rectangle itself}
 *
 * e.g.: T = 2
 *       1x1 grid (A = 1) has 1 rectangle (delta = 1)
 *       both 2x1 and 1x2 grids (A = 2) have 3 rectangles (delta = 1)
 *       ans = 2
 */

class CountingRectangles {
    /**
     * The count of contained rectangles is calculated based on the sequence of triangle numbers
     * (T_n) being equivalent to the contained count for every smaller nx1 grid, such that:
     *
     *      A(x, y) -> T_x * T_y = x(x + 1)/2 * y(y + 1)/2
     *                           = xy(x + 1)(y + 1)/4
     *
     * Since the area A is linked to the resulting count, and assuming that x is the smaller
     * side of the grid, x cannot exceed sqrt(A) as one of the sides must be smaller than this
     * value to produce a valid A.
     *
     * For every x below this limit, y is iterated over until a resulting rectangles count
     * exceeds [target]. Every resulting count is checked to see if it has a smaller delta
     * than the stored best.
     */
    fun findClosestContainingArea(target: Int): Int {
        var closest = Int.MAX_VALUE to 0  // delta to area
        for (x in 1..sqrt(1.0 * target).toInt()) {
            var y = x
            var count = 0
            while (count < target) {
                count = x * y * (x + 1) * (y + 1) / 4
                val area = x * y
                val delta = abs(target - count)
                if (delta <= closest.first && closest.second <= area) {
                    closest = delta to area
                }
                y++
            }
            // if x^2 is already exceeding the target, any larger x will immediately do so too
            if (y - 1 == x) break
        }
        return closest.second
    }
}