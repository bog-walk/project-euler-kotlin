package batch2

import java.math.BigInteger

/**
 * Problem 16: Power Digit Sum
 * Goal: Find the sum of the digits of the number 2^N,
 * where 1 <= N <= 10^4.
 * Test: 2^9 = 512 -> 5 + 1 + 2 = 8
 * 2^3 -> 8, 2^4 -> 7, 2^7 -> 11
 */

class PowerDigitSum {
    fun expDigSum(n: Int): Int {
        val power = BigInteger.TWO.pow(n).toString()
        return power.map(Char::digitToInt).sum()
    }
}