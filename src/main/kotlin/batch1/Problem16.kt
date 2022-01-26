package batch1

import java.math.BigInteger

/**
 * Problem 16: Power Digit Sum
 *
 * https://projecteuler.net/problem=16
 *
 * Goal: Calculate the sum of the digits of the number 2^N.
 *
 * Constraints: 1 <= N <= 1e4
 *
 * e.g.: N = 9
 *       2^9 = 512
 *       sum = 5+1+2 = 8
 */

class PowerDigitSum {
    fun expDigSumUsingString(n: Int): Int {
        val power = BigInteger.TWO.pow(n).toString()
        return power.map(Char::digitToInt).sum()
    }

    fun expDigSumUsingMath(n: Int): Int {
        var power = BigInteger.TWO.pow(n)
        var total = 0
        val ten = 10.toBigInteger()
        while (power != BigInteger.ZERO) {
            total += (power % ten).toInt()
            power /= ten
        }
        return total
    }
}