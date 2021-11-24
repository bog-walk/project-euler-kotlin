package batch3

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
 * e.g.: Fibonacci sequence = {0,1,1,2,3,5,8,13,21,34,55,89,144}
 *       N = 3
 *       first term with 3 digits = F(12)
 */

class NDigitFibonacciNumber {
    /**
     * Iterative solution checks every Fibonacci term to see if it matches
     * the amount of digits required.
     *
     * SPEED: 1.2e6ns for N = 10.
     *        7053ms (compared to ns with inverted formula) for N = 5000.
     */
    fun nDigitFibTermBrute(n: Int): Int {
        var term = 7
        var fN = BigInteger.valueOf(13)
        var fNMinus1 = BigInteger.valueOf(8)
        while (fN.toString().length < n) {
            term++
            val fNMinus2 = fNMinus1
            fNMinus1 = fN
            fN = fNMinus1 + fNMinus2
        }
        return term
    }

    /**
     * Returns the Nth Fibonacci sequence number, as an alternative to iteration.
     * Based on the closed-form formula:
     * Fn = (Phi^n - Psi^n) / sqrt(5),
     * with Phi = (1 + sqrt(5)) / 2 ~= 1.61803...
     * & Psi = -Phi^-1.
     * Rounding, using the nearest integer function, reduces the formula to:
     * Fn = [Phi^n / sqrt(5)], where n >= 0.
     * Truncation, using the floor function, would result instead in:
     * Fn = [(Phi^n / sqrt(5)) + 0.5], where n >= 0.
     */
    fun nthFibUsingGoldenRatio(n: Int): Int {
        val phi = (1 + sqrt(5.0)) / 2
        return round(phi.pow(n) / sqrt(5.0)).toInt()
    }

    /**
     * Based on the above formula being inverted to calculate log10 of
     * each Fibonacci number, thereby returning the term that has the
     * required amount of digits, without need to iterate.
     *
     * SPEED: 3.5e5ns for N = 10.
     * (BEST for N > 10)  5.0e5ns for N = 5000.
     */
    fun nDigitFibTermByDigitsGolden(n: Int): Int {
        val phi = (1 + sqrt(5.0)) / 2
        return ceil((n - 1 + log10(5.0) / 2) / log10(phi)).toInt()
    }

    /**
     * Iterative solution uses Golden Ratio to calculate the Fibonacci
     * sequence numbers.
     *
     * SPEED (BEST for N <= 10): 8.2e4ns for N = 10.
     * Significantly slower execution from N > 10,
     * due to exponential need to calculate Phi^N.
     */
    fun nDigitFibTermUsingGoldenRatio(n: Int): Int {
        var term = 7
        var fN = 13
        // Pattern shows the amount of digits increases every 4th-5th term
        val step = 4
        while (fN.toString().length < n) {
            term += step
            fN = nthFibUsingGoldenRatio(term)
            while (fN.toString().length < n) {
                term++
                fN = nthFibUsingGoldenRatio(term)
            }
        }
        return term
    }
}