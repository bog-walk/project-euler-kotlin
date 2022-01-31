package batch4

import util.maths.isTriangularNumber
import util.maths.lcm
import kotlin.math.sqrt

/**
 * Problem 42: Coded Triangle Numbers
 *
 * https://projecteuler.net/problem=42
 *
 * Goal: Given an integer, if it is a triangle number t_n, return its
 * corresponding term n; otherwise, return -1.
 *
 * Constraints: 1 <= t_n <= 1e18
 *
 * Triangle Number Sequence: The nth term is given by ->
 * t_n = 0.5 * n * (n + 1) ->
 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55,...
 *
 * e.g.: N = 2
 *       return -1, as 2 is not a triangle number; however,
 *       N = 3
 *       return 2, as 3 is the 2nd triangle number to exist.
 */

class CodedTriangleNumbers {
    /**
     * Triangle Number Sequence has interesting properties, e.g. the
     * sequence follows the pattern (odd, odd, even, even,...) & each
     * t_n is the sum of (t_n - 1) & current n.
     *
     * Rather than brute iteration that pre-computes all triangle numbers
     * below 1e18, this solution is based on the following:
     * t_n = 0.5 * n * (n + 1) ->
     * 2 * t_n = n * (n + 1), which means
     * (2 * t_n) / n = n + 1 and (2 * t_n) / (n + 1) = n, therefore
     * 2 * t_n == lcm(n, n+ 1) and
     * n must at minimum be sqrt(2 * t_n)
     */
    private fun getTriangleTerm(tN: Long): Int? {
        val tN2 = 2L * tN
        val n = sqrt(1.0 * tN2).toInt()
        return if (tN2.toInt() == lcm(n, 1 + n)) n else null
    }

    fun triangleNumber(tN: Long): Int = getTriangleTerm(tN) ?: -1

    fun triangleNumberAlt(tN: Long): Int = tN.isTriangularNumber() ?: -1

    /**
     * Project Euler specific implementation that returns the count, from an
     * input of <2000 words, of words whose summed alphabetical character value
     * corresponds to a triangle number. e.g. "SKY" = 19 + 11 + 25 = 55 = t_10.
     */
    fun countTriangleWords(input: List<String>): Int {
        return input.mapNotNull { word ->
            // get alphabetical position based on 'A' code = 65
            val num = word.map { ch -> ch.code - 64L }.sum()
            getTriangleTerm(num)
        }.count()
    }
}