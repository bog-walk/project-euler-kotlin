package dev.bogwalk.batch9

import java.math.BigInteger

/**
 * Problem 97: Large Non-Mersenne Prime
 *
 * https://projecteuler.net/problem=97
 *
 * Goal: Return the last 12 digits of a very large number of the form A * (B^C) + D. If this
 * amount of less than 1e12, return the output padded with leading zeroes.
 *
 * Constraints: 1 <= A, B, C, D <= 1e9
 *
 * Mersenne Prime: A prime number that is 1 less than a power of 2, of the form M_n = (2^n) - 1.
 * For this number to be prime, n must also be prime. The 1st few Mersenne primes are 3, 7, 31,
 * 127, 8191 corresponding to n = 2, 3, 5, 7, 13. The 1st known prime to exceed 1e6 digits is a
 * Mersenne prime of the form (2^6_972_593) - 1, which contains exactly 2_098_960 digits.
 *
 * e.g.: A = 2, B = 3, C = 4, D = 5
 *       2 * (3^4) + 5 = 167
 *       output = "000000000167"
 */

class LargeNonMersennePrime {
    private val modulo: Long = 1_000_000_000_000

    /**
     * Solution optimised by using BigInteger's built-in modPow().
     *
     * SPEED (BETTER) 3.56s for PE problem.
     */
    fun tailOfVeryLargeNumBI(a: Int, b: Int, c: Int, d: Int): String {
        val bigMod = BigInteger.valueOf(modulo)
        val (aBI, bBI, cBI, dBI) = listOf(a, b, c, d).map(Int::toBigInteger)
        val power = bBI.modPow(cBI, bigMod)
        val result = aBI * power + dBI
        return result.mod(bigMod).toString().padStart(12, '0')
    }

    /**
     * Solution is similar to Problem 13's RTL manual addition, except that there is no need to
     * either use RollingQueue or iterate more than 12 times since only the last 12 digits are
     * required.
     *
     * SPEED (WORSE) 6.43s for PE problem.
     */
    fun tailOfVeryLargeNum(a: Int, b: Int, c: Int, d: Int): String {
        val power = b.toBigInteger().pow(c).toString().takeLast(12).padStart(12, '0')
        val tail = IntArray(12)
        var carryOver = 0
        val extra = d.toString().padStart(12, '0')
        for (i in 11 downTo 0) {
            val sum = a * power[i].digitToInt() + extra[i].digitToInt() + carryOver
            tail[i] = sum % 10
            carryOver = sum / 10
        }
        return tail.joinToString("")
    }

    /**
     * HackerRank specific implementation that requires the last 12 digits of the sum of multiple
     * very large numbers resulting from expressions of the form a * b^c + d.
     */
    fun tailSumOfVerlylargeNums(inputs: List<List<String>>): String {
        val bigMod = BigInteger.valueOf(modulo)
        var sum = BigInteger.ZERO

        for ((a, b, c, d) in inputs) {
            val power = b.toBigInteger().modPow(c.toBigInteger(), bigMod)
            sum += (a.toBigInteger() * power + d.toBigInteger()).mod(bigMod)
            // could perform a third modulo here but this significantly reduces performance
        }

        return sum.mod(bigMod).toString().padStart(12, '0')
    }

    /**
     * Project Euler specific implementation that requires the last 10 digits of the massive
     * non-Mersenne prime of the form 28433 * (2^7_830_457) + 1, which contains 2_357_207 digits.
     */
    fun tailOfNonMersennePrime(useManual: Boolean = false): String {
        val (a, b, c, d) = listOf(28433, 2, 7_830_457, 1)
        return if (useManual) {
            tailOfVeryLargeNum(a, b, c, d).takeLast(10)
        } else {
            tailOfVeryLargeNumBI(a, b, c, d).takeLast(10)
        }
    }
}