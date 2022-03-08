package batch3

import java.math.BigInteger

/**
 * Problem 31: Coin Sums
 *
 * https://projecteuler.net/problem=31
 *
 * Goal: Count the number of ways (mod 1e9 + 7) that N pence can be made using any combination of
 * English coins.
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

    private val recursiveMemo = Array(100_001) {
        Array<BigInteger>(8) { BigInteger.ZERO }
    }

    /**
     * Recursive solution uses helper function to allow memoization using top-down, thereby
     * optimising this top-down approach.
     *
     * SPEED (WORSE) 20.34s for N = 1e5
     *
     * @param [n] total amount that needs to be achieved by all combinations.
     * @param [coin] index of coin value from class [coins]. Default is the largest coin
     * available (2 pounds). This parameter allows flexibility in the method purpose. e.g. Count
     * combos for making 10p using 2p (& lower coins) = 6, instead of making 10p using all
     * possible coins = 11 combos.
     */
    fun countCoinCombosRecursive(n: Int, coin: Int = 7): Int {
        return recursiveCombos(n, coin).mod(modulus).intValueExact()
    }

    /**
     * Repeatedly subtract each coin value from the target value & sum combos previously
     * calculated for smaller targets.
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

    /**
     * Solution uses bottom-up approach that determines a target's combo based on:
     *
     *  - The previous combo calculated for the coin with a smaller target, &
     *
     *  - The previous combo calculated for a coin of lesser value.
     *
     * SPEED (BETTER) 34.14ms for N = 1e5
     * Better performance due less expensive loops (vs more expensive recursive function calls) &
     * use of less memory with better cache-access.
     */
    fun countCoinCombos(n: Int): Int {
        // index 0 exists for when 0p is needed
        val combosByCoin = Array<BigInteger>(n + 1) {
            BigInteger.ZERO
        }.apply { this[0] = BigInteger.ONE }
        for (coin in coins) {
            for (i in coin..n) {
                combosByCoin[i] += combosByCoin[i - coin]
            }
        }
        return combosByCoin[n].mod(modulus).intValueExact()
    }
}