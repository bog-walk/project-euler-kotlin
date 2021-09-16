package batch3

import java.math.BigInteger

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
    fun nDigitFibTerm(n: Int): Int {
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
     * Returns list of first terms in sequence to have (index + 2) digits.
     */
    fun fibTermByDigits(maxDigits: Int): List<Int> {
        val terms = mutableListOf(7)
        var term = 7
        var fN = BigInteger.valueOf(13)
        var fNMinus1 = BigInteger.valueOf(8)
        var digits = 3
        while (digits <= maxDigits) {
            term++
            val fNMinus2 = fNMinus1
            fNMinus1 = fN
            fN = fNMinus1 + fNMinus2
            if (fN.toString().length == digits) {
                terms.add(term)
                digits++
            }
        }
        return terms
    }
}