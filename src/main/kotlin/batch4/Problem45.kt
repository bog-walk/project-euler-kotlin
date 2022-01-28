package batch4

import util.maths.isPentagonalNumber
import util.maths.isTriangularNumber
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Problem 45: Triangular, Pentagonal, & Hexagonal
 *
 * https://projecteuler.net/problem=45
 *
 * Goal: Given a & b, find all numbers below N that are
 * both types of numbers queried (as below).
 *
 * Constraints: 2 <= N <= 2e14
 *              a < b
 *              a,b in {3, 5, 6} -> {triangle, pentagonal, hexagonal}
 *
 * Triangle Number: T_n = n * (n + 1) / 2
 * Pentagonal Number: P_n = n * (3 * n - 1) / 2
 * Hexagonal Number: H_n = n * (2 * n - 1)
 * Some numbers can be all 3 type ->
 * e.g. T_1 = P_1 = H_1 = 1
 *      T_285 = P_165 = H_143 = 40755
 *
 * e.g.: N = 10000, a = 3, b = 5
 *       result = {1, 210}
 *       N = 100000, a = 5, b = 6
 *       result = {1, 40755}
 */

class TriPentHex {
    /**
     * Derivation solution is based on the following:
     * n * (2 * n - 1) = h_n ->
     * inverse function, positive solution ->
     * n = 0.25 * (sqrt((8 * h_n) + 1) + 1)
     *
     * @return  If hN is the nth hexagonal, or null
     */
    private fun isHexagonalNumber(hN: Long): Int? {
        val n = 0.25 * (sqrt(8.0 * hN + 1) + 1)
        return if (n == floor(n)) n.toInt() else null
    }

    /**
     * HackerRank specific implementation will never request numbers
     * that are both triangular and hexagonal. The solution is optimised
     * by generating the fastest growing number type first (e.g. hexagonal
     * numbers jump to the limit faster than the other 2) & checking if
     * it matches the other requested type.
     */
    fun commonNumbers(n: Long, a: Int, b: Int): List<Long> {
        val common = mutableListOf(1L)
        var i = 2L
        while (true) {
            val firstType = if (b == 6) i * (2 * i - 1) else i * (3 * i - 1) / 2
            if (firstType >= n) break
            val isSecondType = if (a == 3) {
                firstType.isTriangularNumber() != null
            } else {
                firstType.isPentagonalNumber() != null
            }
            if (isSecondType) common.add(firstType)
            i++
        }
        return common
    }

    /**
     * Project Euler specific implementation that finds the next number,
     * after {1, 40755} that is triangular, pentagonal, and hexagonal.
     * All hexagonal numbers are a subset of triangular numbers made from
     * odd n, as T_(2n - 1) == H_n, based on completing the squares below:
     * (n * (n + 1)) / 2 = m * (2 * m - 1) ->
     * n^2 + n = 4 * m^2 - 2 * m ->
     * (n + 0.5)^2 = 4 * (m - 0.25)^2 ->
     * n = 2 * m - 1
     * So this solution only needs to check for hexagonal numbers that are
     * also pentagonal.
     */
    fun nextTripleType(): Long {
        var next: Long = 40755
        while (true) {
            next++
            if (isHexagonalNumber(next) != null &&
                next.isPentagonalNumber() != null) break
        }
        return next
    }
}