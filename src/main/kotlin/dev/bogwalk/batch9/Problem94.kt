package dev.bogwalk.batch9

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Problem 94: Almost Equilateral Triangles
 *
 * https://projecteuler.net/problem=94
 *
 * Goal: Find the sum of the perimeters of all almost equilateral triangles with integral side
 * lengths and integral areas and whose perimeters do not exceed N.
 *
 * Constraints: 15 <= N <= 1e18
 *
 * Almost Equilateral Triangle: A triangle that has 2 equal sides, with the third unique side
 * differing by no more than 1 unit.
 *
 * e.g.: N = 16
 *       sum = 16 -> {(5, 5, 6)}
 *       N = 51
 *       sum = 66 -> {(5, 5, 6), (17, 17, 16)}
 */

class AlmostEquilateralTriangles {
    /**
     * Solution takes advantage of the observed pattern that all almost equilateral triangles
     * alternate in having the unique side being 1 unit greater or less than the equal sides.
     *
     * Area of an almost equilateral triangles found using Heron's formula:
     *
     *      sp = (a + b + c) / 2
     *      area = sqrt(sp * (sp - a) * (sp - b) * (sp - c))
     *
     *      substitute a for all sides, e.g. if distinct side expected to be 1 unit greater:
     *      sp = (3a + 1) / 2
     *      area = sqrt(sp * (sp - a)^2 * (sp - a - 1))
     *
     * Note that BigDecimal is necessary to handle issues with large Doubles when N > 1e6.
     *
     * SPEED (WORSE) 9.99ms for N = 1e6
     *               65.26s for N = 1e7
     */
    fun perimeterSumOfAlmostEquilaterals(n: Long): Long {
        var sum = 0L
        var side = 5  // smallest valid triangle (5, 5, 6)
        var validPerimeter = 16L
        var toggle = -1  // side 5 used +1

        while (validPerimeter <= n) {
            sum += validPerimeter
            val perimeter = 3L * ++side + toggle
            val semiP = BigDecimal(perimeter / 2.0)
            val a = side.toBigDecimal()
            val inner = semiP * (semiP - a) * (semiP - a) * (semiP - (a + toggle.toBigDecimal()))
            val area = inner.sqrt(MathContext(20, RoundingMode.HALF_UP))
            validPerimeter = if (area * area == inner) {
                toggle *= -1
                perimeter
            } else 0
        }

        return sum
    }

    /**
     * Solution uses the observed pattern as above, as well as a sequential pattern that forms
     * the next term based on the previous 2 valid triangle sides:
     *
     *          {(5, +1), (17, -1), (65, +1), (241, -1), ...}
     *          when next unique side expected to be greater ->
     *          side_i = 4 * side_{i-1} - side_{i-2} + 2
     *          when next unique side expected to be smaller ->
     *          side_i = 4 * side_{i-1} - side_{i-2} - 2
     *
     * SPEED (BETTER) 2.6e+04ns for N = 1e6
     *                4.3e+04ns for N = 1e7
     */
    fun perimeterSumOfSequence(n: Long): Long {
        var sum = 0L
        val validSides = longArrayOf(1, 5)  // smallest almost equilateral triangle (5, 5, 6)
        var validPerimeter = 16L
        var toggle = -1  // first triangle was +1

        while (validPerimeter <= n) {
            sum += validPerimeter
            val nextValid = 4 * validSides[1] - validSides[0] + (toggle * 2)
            validPerimeter = 3 * nextValid + toggle
            toggle *= -1
            validSides[0] = validSides[1]
            validSides[1] = nextValid
        }

        return sum
    }
}