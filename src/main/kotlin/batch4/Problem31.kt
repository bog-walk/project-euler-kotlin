package batch4

import java.math.BigInteger

/**
 * Problem 31: Coin Sums
 *
 * https://projecteuler.net/problem=31
 *
 * Goal: Count the number of ways (mod 1e9 + 7) that N pence
 * can be made using any combination of English coins.
 *
 * Constraints: 1 <= N <= 1e5
 *
 * English currency: There are 8 types of coins in circulation ->
 * {1p, 2p, 5p, 10p, 20p, 50p, 1 pound (= 100p), 2 pound (= 200p)}.
 *
 * e.g.: N = 5 (i.e. goal is 5 pence in coins)
 *       combos = [{5p}, {2p, 2p, 1p}, {2p, 1p, 1p, 1p}, {1p, 1p, 1p, 1p, 1p}]
 *       count = 4
 */

class CoinSums {
    private val modulus = BigInteger.valueOf(1_000_000_007)
    private val coins = listOf(1, 2, 5, 10, 20, 50, 100, 200)

    fun countCoinCombos(n: Int): Int {
        val combosByCoin = Array<BigInteger>(n + 1) { BigInteger.ZERO }
        // Base case for when 0p is needed
        combosByCoin[0] = BigInteger.ONE
        coins.forEach { coin ->
            // Keep shifting forward as cannot give a higher value coin than needed
            for (i in coin..n) {
                combosByCoin[i] += combosByCoin[i - coin]
            }
        }
        return combosByCoin[n].mod(modulus).intValueExact()
    }
}