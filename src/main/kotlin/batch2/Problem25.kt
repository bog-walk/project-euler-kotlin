package batch2

import java.math.BigInteger
import kotlin.math.*

/**
 * Problem 25: N-digit Fibonacci Number
 *
 * https://projecteuler.net/problem=25
 *
 * Goal: Find the first term in the Fibonacci sequence to have N digits.
 *
 * Constraints: 2 <= N <= 5000
 *
 * e.g.: N = 3
 *       Fibonacci sequence = {0,1,1,2,3,5,8,13,21,34,55,89,144}
 *       first term with 3 digits = F(12)
 */

class NDigitFibonacciNumber {
    private val phi = (1 + sqrt(5.0)) / 2

    /**
     * Iterative solution that checks all Fibonacci numbers.
     *
     * SPEED (WORST for low N) 1.9e5ns for N = 10
     * SPEED (BETTER for high N) 7.60s for N = 5000
     *
     * @return list of the first Fibonacci terms to have (index + 2) digits.
     */
    fun nDigitFibTermsBrute(maxDigits: Int): IntArray {
        var term = 7
        var fN = BigInteger.valueOf(13)
        val terms = IntArray(maxDigits - 1).apply { this[0] = term }
        var fNMinus1 = BigInteger.valueOf(8)
        var digits = 3
        while (digits <= maxDigits) {
            term++
            val fNMinus2 = fNMinus1
            fNMinus1 = fN
            fN = fNMinus1 + fNMinus2
            if (fN.toString().length == digits) {
                terms[digits++ - 2] = term
            }
        }
        return terms
    }

    /**
     * Iterative solution uses the Golden Ratio to check all Fibonacci numbers.
     *
     * Original solution compared digit lengths by casting fN to a String. This
     * has been replaced by calling log10(fN) and comparing it to the required digits minus 1,
     * with significant performance improvement.
     *
     * SPEED (BETTER for low N) 87900ns for N = 10
     * SPEED (IMPOSSIBLE for N > 10) Significantly slower execution due to the exponential need
     * to calculate larger Phi^N and resulting OverflowError
     *
     * @return first Fibonacci term to have N digits.
     */
    fun nDigitFibTermGoldenBrute(n: Int): Int {
        var term = 7
        var fN = 13
        // pattern shows the amount of digits increases every 4th-5th term
        val step = 4
        while (log10(1.0 * fN) < n - 1) {
            term += step
            fN = nthFibUsingGoldenRatio(term)
            while (log10(1.0 * fN) < n - 1) {
                term++
                fN = nthFibUsingGoldenRatio(term)
            }
        }
        return term
    }

    /**
     * Finds the [n]th Fibonacci number using Binet's formula.
     *
     * The Golden Ration, Phi, provides an alternative to iteration, based on the closed-form
     * formula:
     *
     * Fn = (Phi^n - Psi^n) / sqrt(5),
     * with Phi = (1 + sqrt(5)) / 2 ~= 1.61803... & Psi = -Phi^-1
     *
     * Rounding, using the nearest integer function, reduces the formula to:
     * Fn = round(Phi^n / sqrt(5)), where n >= 0.
     *
     * Truncation, using the floor function, would result instead in:
     * Fn = floor((Phi^n / sqrt(5)) + 0.5), where n >= 0.
     */
    fun nthFibUsingGoldenRatio(n: Int): Int {
        return round(phi.pow(n) / sqrt(5.0)).toInt()
    }

    /**
     * O(n) solution based on the inversion of closed-form Binet's formula.
     *
     * Phi^t / sqrt(5) > 10^(n-1)
     *
     * Phi^t > 10^(n-1) * sqrt(5)
     *
     * log(Phi)t > log(10)(n - 1) + log(5)/2
     *
     * t > (1(n - 1) + log(5)/2) / log(Phi)
     *
     * t = ceil((n - 1 + log(5)/2) / log(Phi))
     *
     * SPEED (BEST for low N) 54600ns for N = 10
     * SPEED (BEST for high N) 29200ns for N = 5000
     *
     * @return first Fibonacci term to have N digits.
     */
    fun nDigitFibTermGoldenFormula(n: Int): Int {
        return ceil((n - 1 + log10(5.0) / 2) / log10(phi)).toInt()
    }
}