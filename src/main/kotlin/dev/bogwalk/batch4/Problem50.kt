package dev.bogwalk.batch4

import dev.bogwalk.util.maths.isPrimeMRBI
import dev.bogwalk.util.maths.primeNumbers
import dev.bogwalk.util.search.binarySearch

/**
 * Problem 50: Consecutive Prime Sum
 *
 * https://projecteuler.net/problem=50
 *
 * Goal: Return the smallest prime <= N that can be represented as the sum of the longest
 * consecutive prime sequence.
 *
 * Constraints: 2 <= N <= 1e12
 *
 * e.g.: N = 100
 *       sum = 41 -> 2 + 3 + 5 + 7 + 11 + 13
 *       length = 6
 */

class ConsecutivePrimeSum {
    // sieve generation of prime numbers is limited by memory
    private val sieveLimit = 10_000_000L

    /**
     * Brute force solution shows that all valid sequences start at low primes, with only a few
     * starting as high as the 4th, 8th, or 12th prime.
     *
     * If the sum of a sequence exceeds the given limit, the next sequence starting from a larger
     * prime will be 1 prime longer from where it broke.
     *
     * SPEED (WORSE) 4.96s for N = 1e10
     *
     * @return pair of the smallest valid prime to the length of the generating prime sequence.
     */
    fun consecutivePrimeSum(n: Long): Pair<Long, Int> {
        val limit = minOf(n, sieveLimit).toInt()
        val primes = primeNumbers(limit)
        val size = primes.size
        var smallest = 2L to 1
        val maxI = minOf(size, 12)
        var maxJ = size
        for (i in 0 until maxI) {
            for (j in i + smallest.second until maxJ) {
                val seqSum = primes.subList(i, j).sumOf { it.toLong() }
                if (seqSum > n) {
                    maxJ = j + 1
                    break
                }
                if (
                    seqSum <= limit && binarySearch(seqSum.toInt(), primes) ||
                    seqSum.isPrimeMRBI()
                ) {
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
     * Solution optimised by generating a smaller list of cumulative sums of primes to loop
     * through, of which only the last value can exceed the given limit.
     *
     * Nested loop starts backwards to get the longest sequence sum for each starting prime by
     * subtracting cumulative sums, then breaking the internal loop if a valid sequence is found.
     *
     * SPEED (BETTER) 117.64ms for N = 1e10
     *
     * @return pair of the smallest valid prime to the length of the generating prime sequence.
     */
    fun consecutivePrimeSumImproved(n: Long): Pair<Long, Int> {
        val limit = minOf(n, sieveLimit).toInt()
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
                if (
                    length > smallest.second &&
                    (seqSum <= limit && binarySearch(seqSum.toInt(), primes) ||
                            seqSum.isPrimeMRBI())
                ) {
                    smallest = seqSum to length
                    break
                }
            }
        }
        return smallest
    }
}