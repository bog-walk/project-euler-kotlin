package batch3

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

    private val recursiveMemo = Array(100001) { Array<BigInteger>(8) { BigInteger.ZERO } }

    /**
     * Recursive solution optimised with memoisation using top-down approach.
     * Repeatedly subtract each coin value from the target & sum combos previously
     * calculated for smaller targets.
     *
     * @param [coin] Index of coin value from [coins] above. Default is the
     * largest coin available (2 pounds).
     * This parameter allows flexibility in method purpose ->
     * e.g. Could find combos for making 10p using 2p (& lower coins) = 6,
     * instead of making 10p using all possible coins = 11 combos.
     *
     * SPEED: 15764ms for N = 1e5
     */
    private fun recursiveCombos(n: Int, coin: Int): BigInteger {
        if (coin < 1) return BigInteger.ONE
        if (recursiveMemo[n][coin] > BigInteger.ZERO) return recursiveMemo[n][coin]
        var target = n
        var combos = BigInteger.ZERO
        while (target >= 0) {
            combos += recursiveCombos(target, coin - 1)
            target -= coins[coin]
        }
        recursiveMemo[n][coin] = combos
        return combos
    }

    fun countCoinCombosRecursive(n: Int, coin: Int = 7): Int {
        return recursiveCombos(n, coin).mod(modulus).intValueExact()
    }

    /**
     * Bottom-up approach that determines a target's combo based on:
     * - the previous combo calculated for the coin with a smaller target, &
     * - the previous combo calculated for a coin of lesser value.
     *
     * SPEED (BEST): 56ms for N = 1e5
     * Better performance due less expensive loops (vs recursive function calls)
     * & use of less memory with better cache-access.
     */
    fun countCoinCombos(n: Int): Int {
        val combosByCoin = Array<BigInteger>(n + 1) { BigInteger.ZERO }
        // Base case for when 0p is needed
        combosByCoin[0] = BigInteger.ONE
        coins.forEach { coin ->
            for (i in coin..n) {
                combosByCoin[i] += combosByCoin[i - coin]
            }
        }
        return combosByCoin[n].mod(modulus).intValueExact()
    }
}