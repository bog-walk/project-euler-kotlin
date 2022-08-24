package dev.bogwalk.batch4

import java.math.BigInteger

/**
 * Problem 48: Self Powers
 *
 * https://projecteuler.net/problem=48
 *
 * Goal: Find the last 10 digits of the number generated by the series,
 * 1^1 + 2^2 + 3^3 + .. + N^N, without leading zeroes.
 *
 * Constraints: 1 <= N <= 2e6
 *
 * e.g.: N = 10
 *       1^1 + 2^2 + 3^3 + .. + 10^10 = 10_405_071_317
 *       last 10 digits = 405_071_317 (leading zero omitted)
 */

class SelfPowers {
    private val modulo: Long = 10_000_000_000

    /**
     * Solution based on the modular arithmetic rule that:
     *
     *      (x + y) % z == ((x % z) + (y % z)) % z
     *
     * This same rule applies to multiplication with modulo:
     *
     *      (x * y) % z == ((x % z) * (y % z)) % z
     *
     * The carried over number for each new self-power is thereby significantly reduced by
     * performing modulo at every step, which avoids the need to use BigInteger.
     *
     * SPEED (Worse) 1.13s for N = 1e4
     */
    fun selfPowersSumModulo(n: Int): Long {
        var sum = 0L
        for (d in 1..n) {
            var power = d.toLong()
            repeat(d - 1) {
                power *= d
                power %= modulo
            }
            sum += power
            sum %= modulo
        }
        return sum
    }

    /**
     * Solution optimised by using BigInteger's built-in modPow(exp, mod).
     *
     * SPEED (BETTER) 337.51ms for N = 1e4
     */
    fun selfPowersSum(n: Int): Long  {
        val bigMod = BigInteger.valueOf(modulo)
        var sum = BigInteger.ZERO
        for (d in 1L..n) {
            val power = BigInteger.valueOf(d)
            sum += power.modPow(power, bigMod)
        }
        return sum.mod(bigMod).longValueExact()
    }
}