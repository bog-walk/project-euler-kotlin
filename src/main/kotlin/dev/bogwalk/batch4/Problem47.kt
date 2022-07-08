package dev.bogwalk.batch4

import dev.bogwalk.util.maths.isPrime
import dev.bogwalk.util.maths.primeFactors

/**
 * Problem 47: Distinct Primes Factors
 *
 * https://projecteuler.net/problem=47
 *
 * Goal: Find the 1st integers (must be <= N) of K consecutive integers, that have exactly K
 * distinct prime factors.
 *
 * Constraints: 20 <= N <= 2e6, 2 <= K <= 4
 *
 * Distinct Prime Factors: The 1st 2 consecutive integers to have 2 distinct prime factors are:
 * 14 -> 2 * 7, 15 -> 3 * 5.
 *
 * The 1st 3 consecutive integers to have 3 distinct prime factors are:
 * 644 -> 2^2 * 7, 645 -> 3 * 5 * 43, 646 -> 2 * 17 * 19.
 *
 * e.g.: N = 20, K = 2
 *       result = {14, 20}
 *       N = 644, K = 3
 *       result = {644}
 */

class DistinctPrimesFactors {
    /**
     * Solution could be integrated with the helper function below, countPrimeFactors(), by looping
     * only once through the range [0, [n] + [k]) and performing both functions' tasks in an 'if'
     * block, branched based on whether the list element == [k].
     */
    fun consecutiveDistinctPrimes(n: Int, k: Int): List<Int> {
        val firstConsecutive = mutableListOf<Int>()
        var count = 0
        // extend k-above limit to account for k-consecutive runs
        val factorCounts = countPrimeFactors(n + k)
        for (composite in 14 until n + k) {
            if (factorCounts[composite] != k) {
                count = 0
                continue
            }
            count++
            if (count >= k) {
                firstConsecutive.add(composite - k + 1)
            }
        }
        return firstConsecutive
    }

    /**
     * Modified primeFactors() helper method.
     *
     * @return IntArray of amount of distinct prime factors for every N, where N = index.
     */
    private fun countPrimeFactors(n: Int): IntArray {
        val count = IntArray(n + 1)
        for (i in 2..n) {
            if (count[i] == 0) {
                for (j in i..n step i) {
                    count[j] += 1
                }
            }
        }
        return count
    }

    /**
     * Project Euler specific implementation that returns the 1st integer of the 1st 4
     * consecutive numbers that have 4 distinct prime factors.
     *
     * The minimum representation with 4 distinct prime factors is:
     *
     * 2 * 3 * 5 * 7 = 210
     */
    fun first4DistinctPrimes(): Int {
        val k = 4
        var composite = 210
        nextC@while (true) {
            composite++
            if (
                composite.isPrime() ||
                primeFactors(composite.toLong()).size != k
            ) continue@nextC
            for (i in 1 until k) {
                val adjacent = composite + i
                if (
                    adjacent.isPrime() ||
                    primeFactors(adjacent.toLong()).size != k
                ) continue@nextC
            }
            // only reachable if composite with 3 valid adjacents found
            break@nextC
        }
        return composite
    }
}