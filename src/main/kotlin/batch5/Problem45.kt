package batch5

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
 * Note that all hexagonal numbers are also triangle numbers.
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
    private val formulae = mapOf(
        3 to ::isTriangular, 5 to ::isPentagonal, 6 to ::isHexagonal
    )

    private fun isTriangular(tN: Long): Boolean {
        val n = 0.5 * (sqrt(8.0 * tN + 1) - 1)
        return n == floor(n)
    }

    private fun isPentagonal(pN: Long): Boolean {
        val n = (sqrt(24.0 * pN + 1) + 1) / 6.0
        return n == floor(n)
    }

    /**
     * Derivation solution is based on the following:
     * n * (2 * n - 1) = h_n ->
     * inverse function, positive solution ->
     * n = (sqrt((8 * h_n) + 1) + 1) / 4
     */
    private fun isHexagonal(hN: Long): Boolean {
        val n = 0.25 * (sqrt(8.0 * hN + 1) + 1)
        return n == floor(n)
    }

    fun commonNumbers(n: Int, a: Int, b: Int): List<Int> {
        require(listOf(a, b).all { it in listOf(3, 5, 6) }) { "Invalid types" }
        val common = mutableListOf(1)
        val predicates = if (a == 3 && b == 6) {
            listOf(formulae.getValue(a))
        } else {
            listOf(formulae.getValue(a), formulae.getValue(b))
        }
        for (num in 2 until n) {
            if (predicates.all { isType -> isType(num.toLong()) }) {
                common.add(num)
            }
        }
        return common
    }
}