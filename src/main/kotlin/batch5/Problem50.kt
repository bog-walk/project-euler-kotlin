package batch5

import util.isPrimeLong
import util.primeNumbers

/**
 * Problem 50: Consecutive Prime Sum
 *
 * https://projecteuler.net/problem=50
 *
 * Goal: Return the smallest prime <= N that can be represented
 * as the sum of the longest consecutive prime sequence.
 *
 * Constraints: 2 <= N <= 1e12
 *
 * e.g.: N = 100
 *       sum = 41 -> 2 + 3 + 5 + 7 + 11 + 13
 *       length = 6
 */

class ConsecutivePrimeSum {
    /**
     * Sieve generation of prime numbers limited by memory. Brute force shows
     * that all valid sequences start at low primes, with only a few starting
     * as high as the 4th, 8th or 12th prime. If the sum of a sequence exceeds
     * the given limit, the next sequence starting from a larger prime will be
     * 1 prime longer from where it broke.
     *
     * SPEED: 4.3309s for N = 1e10
     */
    fun consecutivePrimeSum(n: Long): Pair<Long, Int> {
        val limit = minOf(n.toInt(), 10_000_000)
        val primes = primeNumbers(limit)
        val size = primes.size
        var smallest = 2L to 1
        val maxI = minOf(primes.lastIndex, 11)
        var maxJ = size
        for (i in 0..maxI) {
            for (j in i + smallest.second until maxJ) {
                val seqSum = primes.subList(i, j).sumOf { it.toLong() }
                if (seqSum > n) {
                    maxJ = j + 1
                    break
                }
                if (seqSum <= limit && seqSum.toInt() in primes || isPrimeLong(seqSum)) {
                    val length = j - i
                    if (length > smallest.second) {
                        smallest = seqSum to length
                    }
                }
            }
        }
        return smallest
    }

    /**
     * Solution optimised by generating a smaller list of cumulative sums
     * of primes to loop through, of which only the last value can exceed
     * the given limit. Nested loop starts backwards to get the longest
     * sequence sum for each starting prime by subtracting cumulative sums,
     * then breaking internal loop if a valid sequence is found.
     *
     * SPEED: 0.1550s for N = 1e10
     */
    fun consecutivePrimeSumImproved(n: Long): Pair<Long, Int> {
        val limit = minOf(10_000_000, n).toInt()
        val primes = primeNumbers(limit)
        val cumulativeSum = mutableListOf(0L)
        for (prime in primes) {
            cumulativeSum.add(cumulativeSum.last() + prime)
            if (cumulativeSum.last() > n) break
        }
        val size = cumulativeSum.lastIndex
        var smallest = 2L to 1
        for (i in 0..size) {
            for (j in size downTo i + smallest.second) {
                val seqSum = cumulativeSum[j] - cumulativeSum[i]
                if (seqSum > n) continue
                val length = j - i
                if (length > smallest.second &&
                    (seqSum <= limit && seqSum.toInt() in primes ||
                            isPrimeLong(seqSum))) {
                    smallest = seqSum to length
                    break
                }
            }
        }
        return smallest
    }
}