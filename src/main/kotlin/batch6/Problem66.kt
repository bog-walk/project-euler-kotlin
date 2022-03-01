package batch6

import kotlin.math.sqrt
import kotlin.math.truncate

/**
 * Problem 66: Diophantine Equation
 *
 * https://projecteuler.net/problem=66
 *
 * Goal: Given a quadratic Diophantine equation of the form, x^2 - Dy^2 = 1, find the value of
 * D <= N in minimal solutions of x for which the largest value of x is obtained. There are no
 * solutions in positive integers when D is square.
 *
 * Constraints: 7 <= N <= 1e4
 *
 * Pell-Fermat Equation: Any Diophantine equation of the form x^2 - ny^2 = 1, where n is a given
 * positive non-square integer. As long as n is not a perfect square, this equation has
 * infinitely many distinct integer solutions that can be used to approximate sqrt(n) by rational
 * numbers of the form x/y.
 *
 * e.g.: N = 7
 *       D = 2 -> 3^2 - 2 * 2^2 = 1
 *       D = 3 -> 2^2 - 3 * 1^2 = 1
 *       D = 5 -> 9^2 - 5 * 4^2 = 1
 *       D = 6 -> 5^2 - 6 * 2^2 = 1
 *       D = 7 -> 8^2 - 7 * 3^2 = 1
 *       largest x = 9 when D = 5
 */

class DiophantineEquation {
    /**
     * Solution is similar to that in Problem 64, which solves continuous fractions based on the
     * formulae below:
     *
     *      n_k = d_{k-1} * a_{k-1} - n_{k-1}
     *      d_k = floor((x - (n_k)^2)/d_{k-1})
     *      a_k = floor((a_0 + n_k)/d_k)
     *
     * The fundamental solution is found by performing this continued fraction expansion, then
     * applying the recursive relation to the successive convergents using the formulae:
     *
     *      h_n = a_n * h_{n-1} + h_{n-2}
     *      k_n = a_n * k_{n-1} + k_{n-2}
     *
     * with h_n representing numerators & k_n representing denominators.
     *
     * When h_n & k_n satisfy the Pell equation, this pair becomes the fundamental solution
     * (x_1, y_1) for the value D, with h_n = x_1 and k_n = y_1.
     */
    fun largestDiophantineX(n: Int): Int {
        var maxValue = 2 to 3.toBigInteger() // smallest fundamental D = 2
        for (d in 3..n) {
            val dBI = d.toBigInteger()
            val root = sqrt(1.0 * d)
            val a0 = truncate(root).toInt()
            if (root - a0 == 0.0) continue // skip perfect squares
            var a = a0
            var numerator = 0
            var denominator = 1
            // represents [hNMinus2, hNMinus1, a * hNMinus1 + hNMinus2]
            val hN = arrayOf(0.toBigInteger(), 1.toBigInteger(), a0.toBigInteger())
            // represents [kNMinus2, kNMinus1, a * kNMinus1 + kNMinus2]
            val kN = arrayOf(1.toBigInteger(), 0.toBigInteger(), 1.toBigInteger())
            while (true) {
                numerator = denominator * a - numerator
                denominator = (d - numerator * numerator) / denominator
                a = (a0 + numerator) / denominator
                hN[0] = hN[1]
                hN[1] = hN[2]
                hN[2] = a.toBigInteger() * hN[1] + hN[0]
                kN[0] = kN[1]
                kN[1] = kN[2]
                kN[2] = a.toBigInteger() * kN[1] + kN[0]
                if (1.toBigInteger() == hN[2].pow(2) - dBI * kN[2].pow(2)) break
            }
            if (hN[2] > maxValue.second) maxValue = d to hN[2]
        }
        return maxValue.first
    }
}