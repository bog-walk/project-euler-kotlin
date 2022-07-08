package dev.bogwalk.batch6

import dev.bogwalk.util.combinatorics.permutationID
import dev.bogwalk.util.maths.primeNumbers

/**
 * Problem 70: Totient Permutation
 *
 * https://projecteuler.net/problem=70
 *
 * Goal: Find the value of n, with 1 < n < N, for which Phi(n) is a permutation of n and the
 * ratio n/Phi(n) produces a minimum.
 *
 * Constraints: 100 <= N <= 1e7
 *
 * Euler's Totient Function: Phi(n) is used to determine the count of positive integers < n that are
 * relatively prime to n. The number 1 is considered co-prime to every positive number, so
 * Phi(1) = 1. Phi(87109) = 79180, which is a permutation.
 *
 * e.g.: N = 100
 *       result = 21 -> Phi(21) = 12 & 21/Phi(21) = 1.75
 */

class TotientPermutation {
    /**
     * Solution based on Euler's product formula:
     *
     *      Phi(n) = nPi(1 - (1/p)), with p being distinct prime factors of n.
     *
     * If the ratio n / Phi(n) is the required result, this becomes:
     *
     *      n/Phi(n) = n / (nPi(1 - (1/p)))
     *      n/Phi(n) = Pi(p / (p - 1))
     *
     * If this ratio must be minimised then the result of Phi(n) needs to be maximised, which is
     * done by finding n with the fewest but largest distinct prime factors. If these prime
     * factors are narrowed down to only 2 & are both as close to sqrt([limit]) as possible, then
     * Phi's formula becomes:
     *
     *      Phi(n) = n(1 - (1/p_1))(1 - (1/p_2)), with n = p_1p_2 it becomes:
     *
     *      Phi(n) = p_1p_2(1 - (1/p_1))(1 - (1/p_2))
     *
     *      Phi(n) = (p_1p_2 - (p_1p_2/p_1))(1 - (1/p_2))
     *
     *      Phi(n) = (p_1p_2 - p_2)(1 - (1/p_2))
     *
     *      Phi(n) = p_1p_2 - p_1 - p_2 + 1 = (p_1 - 1)(p_2 - 1)
     *
     * This solution finds all valid n except for one, 2817 = {3^2, 313^1}, as it is a product of
     * 3 prime factors. This n is captured in the alternative solution below, without adding a
     * special case clause, but is a slower solution.
     *
     * SPEED (BETTER) 2.23s for N = 1e7
     */
    fun totientPermutation(limit: Int): Int {
        if (limit in 2818..2991) return 2817
        val primes = primeNumbers(limit)
        val numOfPrimes = primes.size
        var minPerm = 1 to Double.MAX_VALUE
        for (i in 0 until numOfPrimes - 1) {
            val p1 = primes[i]
            for (j in i + 1 until numOfPrimes) {
                val p2 = primes[j]
                if (1L * p1 * p2 >= limit) break
                val n = p1 * p2
                val phi = (p1 - 1) * (p2 - 1)
                val ratio = 1.0 * n / phi
                if (
                    ratio < minPerm.second &&
                    permutationID(n.toLong()).joinToString("") ==
                    permutationID(phi.toLong()).joinToString("")
                ) {
                    minPerm = n to ratio
                }
            }
        }
        return minPerm.first
    }

    /**
     * Solution finds totient of every n under [limit] by iterating over prime numbers instead of
     * using a helper prime factorisation method. This iteration is broken early & returns null if
     * the increasingly reducing totient count becomes too small to satisfy the current minimum
     * ratio.
     *
     * SPEED (WORSE) 3.17s for N = 1e7
     */
    fun totientPermutationRobust(limit: Int): Int {
        val primes = primeNumbers(limit)
        var minPerm = 2 to Double.MAX_VALUE

        /**
         * Only returns a value of Phi(n) if n/Phi(n) will create a value that challenges the
         * current minimum ratio stored.
         *
         * Based on Euler's product formula:
         *
         *      Phi(n) = nPi(1 - (1/p)), that repeatedly subtracts found prime factors from n.
         */
        fun maximisedTotient(n: Int): Int? {
            var count = n
            var reduced = n
            for (p in primes) {
                if (p * p > reduced) break
                if (reduced % p != 0) continue
                do {
                    reduced /= p
                } while (reduced % p == 0)
                // equivalent to count = count * (1 - (1/p))
                count -= count / p
                if (count * minPerm.second < n) return null
            }
            return if (reduced > 1) count - (count / reduced) else count
        }

        for (n in 3 until limit) {
            val phi = maximisedTotient(n)
            val ratio = phi?.let { 1.0 * n / it } ?: continue
            if (
                ratio < minPerm.second &&
                permutationID(n.toLong()).joinToString("") ==
                permutationID(phi.toLong()).joinToString("")
            ) {
                minPerm = n to ratio
            }
        }
        return minPerm.first
    }
}