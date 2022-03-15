package batch7

import util.maths.primeNumbers

/**
 * Problem 77: Prime Summations
 *
 * https://projecteuler.net/problem=77
 *
 * Goal: Count the number of ways that N can be written as the sum of 1 or more primes.
 *
 * Constraints: 2 <= N <= 1000
 *
 * e.g.: N = 5
 *       count = 2 -> {5, 3+2}
 *       N = 10
 *       count = 5 -> {7+3, 5+5, 5+3+2, 3+3+2+2, 2+2+2+2+2}
 */

class PrimeSummations {
    /**
     * Project Euler specific implementation that requests the first integer that can be written
     * as the sum of primes in over 5000 different ways.
     */
    fun firstPrimeSumCombo(): Int {
        var limit = 0
        var result = -1
        while (result == -1) {
            limit += 50
            val allCounts = allPrimeSumCombos(limit)
            result = allCounts.indexOfFirst { it > 5000 }
        }
        return result
    }

    /**
     * Solution is identical to the bottom-up approach that found the number of ways a total could
     * be achieved, either using coins of different values (Batch 3 - Problem 31) or using
     * combinations of lesser value positive integers (Batch 7 - Problem 76).
     *
     * @return LongArray of prime partitions (mod 1e9 + 7) of all N <= limit, with index == N.
     */
    fun allPrimeSumCombos(n: Int): LongArray {
        val primeCombosBySum = LongArray(n + 1).apply { this[0] = 1L }
        val primes = primeNumbers(n)
        for (prime in primes) {
            for (i in prime..n) {
                primeCombosBySum[i] += primeCombosBySum[i - prime]
            }
        }
        return primeCombosBySum
    }
}