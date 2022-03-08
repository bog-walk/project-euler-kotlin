package batch4

import util.maths.isTriangularNumber
import util.maths.lcm
import kotlin.math.sqrt

/**
 * Problem 42: Coded Triangle Numbers
 *
 * https://projecteuler.net/problem=42
 *
 * Goal: Given an integer, if it is a triangle number tN, return its corresponding term n;
 * otherwise, return -1.
 *
 * Constraints: 1 <= tN <= 1e18
 *
 * Triangle Number Sequence: The nth term is given by -> tN = n(n + 1) / 2 ->
 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55,...
 *
 * e.g.: N = 2
 *       return -1, as 2 is not a triangle number; however,
 *       N = 3
 *       return 2, as 3 is the 2nd triangle number to exist.
 */

class CodedTriangleNumbers {
    /**
     * SPEED (WORSE) 1.4e5ns for tN = 1e18
     */
    fun triangleNumber(tN: Long): Int = getTriangleTerm(tN) ?: -1

    /**
     * Triangle Number Sequence follows the pattern (odd, odd, even, even,...) & each tN is the
     * sum of the previous tN & the current n.
     *
     * Rather than brute pre-computation of all triangle numbers below 1e18, this solution is
     * based on the formula:
     *
     *      tN = n(n + 1) / 2
     *
     *      2tN = n(n + 1)
     *
     *      2tN / n = n + 1 and 2tN / (n + 1) = n, therefore:
     *
     *      2tN == lcm(n, n + 1) and
     *
     *      n must at minimum be sqrt(2tN)
     */
    private fun getTriangleTerm(tN: Long): Int? {
        val tN2 = 2 * tN
        val n = sqrt(1.0 * tN2).toLong()
        return if (tN2 == lcm(n, 1 + n)) n.toInt() else null
    }

    /**
     * Solution formula optimised by using its derived inverse top-level function.
     *
     * SPEED (BETTER) 2.9e4ns for tN = 1e18
     */
    fun triangleNumberImproved(tN: Long): Int = tN.isTriangularNumber() ?: -1

    /**
     * Project Euler specific implementation that returns the count, from an input of <2000
     * words, of words whose summed alphabetical character value corresponds to a triangle number.
     *
     * e.g. "SKY" = 19 + 11 + 25 = 55 = t_10
     */
    fun countTriangleWords(words: List<String>): Int {
        return words.mapNotNull { word ->
            // get alphabetical position based on 'A' code = 65
            val num = word.sumOf { ch -> ch.code - 64L }
            num.isTriangularNumber()
        }.count()
    }
}