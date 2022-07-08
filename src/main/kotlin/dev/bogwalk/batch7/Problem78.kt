package dev.bogwalk.batch7

import kotlin.math.pow
import kotlin.math.sign

/**
 * Problem 78: Coin Partitions
 *
 * https://projecteuler.net/problem=78
 *
 * Goal: Count the number of ways (mod 1e9 + 7) that N coins can be separated into piles.
 *
 * Constraints: 2 <= N <= 6e4
 *
 * e.g.: N = 5
 *       count = 7
 *       if @ represents a coin, 5 coins can be separated in 7 different ways:
 *       @@@@@
 *       @@@@ @
 *       @@@ @@
 *       @@@ @ @
 *       @@ @@ @
 *       @@ @ @ @
 *       @ @ @ @ @
 */

class CoinPartitions {
    private val modulus = 1_000_000_007

    /**
     * Solution is identical to the bottom-up approach used in Batch 7 - Problem 76 (and is
     * similar to solutions for Problem 31 & 77).
     *
     * SPEED (WORSE) 15.43ms for N = 1e3
     * This solution does not scale at all well for N > 1e3.
     *
     * @return IntArray of partitions (mod 1e9 + 7) of all N <= limit, with index == N.
     */
    fun coinPileCombos(n: Int): IntArray {
        val combosByCoin = IntArray(n + 2).apply { this[0] = 1 }
        for (i in 1..n) {
            for (j in i..n + 1) {
                combosByCoin[j] += combosByCoin[j - i]
                combosByCoin[j] %= modulus
            }
        }
        return combosByCoin
    }

    /**
     * Project Euler specific implementation that requests the first integer N for which the number
     * of ways N can be partitioned is divisible by 1e6.
     */
    fun firstCoinCombo(): Int {
        val smallMod = 1_000_000
        var limit = 0
        var result = -1
        while (result == -1) {
            limit += 10_000
            val allPartitions = coinPileCombosTheorem(limit, smallMod)
            result = allPartitions.indexOfFirst { it.mod(smallMod) == 0 }
        }
        return result
    }

    /**
     * Solution is based on the Pentagonal Number Theorem that states:
     *
     *      (1 - x)(1 - x^2)(1 - x^3)... = 1 - x - x^2 + x^5 + x^7 - x^12 - x^15 + x^22 + x^26 - ...
     *
     * The right-side exponents are generalised pentagonal numbers given by the formula:
     *
     *      g_k = k(3k - 1) / 2, for k = 1, -1, 2, -2, 3, -3, ...
     *
     * This holds as an identity for calculating the number of partitions of [limit] based on:
     *
     *      p(n) = p(n - 1) + p(n - 2) - p(n - 5) - p(n - 7) + ..., which is expressed as:
     *
     *      p(n) = Sigma{k!=0} ((-1)^{k-1} * p(n - g_k))
     *
     * SPEED (BETTER) 2.51ms for N = 1e3
     * SPEED (using BigInteger) 5.50s for N = 6e4
     * SPEED (using Long) 1.05s for N = 6e4
     *
     * N.B. This solution originally calculated count using BigInteger & used the modulus after
     * every summation to allow an integer value to be cached for every N. Using Long instead
     * improved performance speed by 5x. Because of this change, the count could occasionally
     * become negative, so a check was placed to reverse the modulus if this occurs.
     *
     * @return IntArray of partitions (mod 1e9 + 7) of all N <= limit, with index == N.
     */
    fun coinPileCombosTheorem(limit: Int, modValue: Int = modulus): IntArray {
        val partitions = IntArray(limit + 1).apply { this[0] = 1 }
        for (n in 1..limit) {
            var count = 0L
            var k = 1
            while (true) {
                val pentagonal = k * (3 * k - 1) / 2
                if (pentagonal > n) break
                val unary = (-1.0).pow(k-1).toLong()
                count += unary * partitions[n-pentagonal]
                count %= modValue
                if (count < 0L) count += modValue
                k *= -1
                if (k.sign == 1) k++
            }
            partitions[n] = count.toInt()
        }
        return partitions
    }
}