package dev.bogwalk.batch1

import kotlin.math.log2

/**
 * Problem 14: Longest Collatz Sequence
 *
 * https://projecteuler.net/problem=14
 *
 * Goal: Find the largest starting number <= N that produces the longest Collatz sequence.
 *
 * Constraints: 1 <= N <= 5e6
 *
 * Collatz Sequence: Thought to all finish at 1, a sequence of positive integers is generated
 * using the hailstone calculator algorithm, such that:
 * (even n) n -> n/2
 * (odd n) n -> 3n + 1
 *
 * e.g.: N = 5
 *       1
 *       2 -> 1
 *       3 -> 10 -> 5 -> 16 -> 8 -> 4 -> 2 -> 1
 *       4 -> 2 -> 1
 *       5 -> 16 -> 8 -> 4 -> 2 -> 1
 *       longest chain when starting number = 3
 */

class LongestCollatzSequence {
    private val limit = 5e6.toInt()
    // cache for all previously counted collatz sequences
    private val countedSeq = IntArray(limit + 1) { if (it == 0) 1 else 0 }
    // cache for the starting number <= index that generates the longest sequence
    private val longestCountedUnderN = IntArray(limit + 1) { if (it == 0) 1 else 0 }

    /**
     * Returns the largest starter that is <= [n], from a list of starters that stores the longest
     * Collatz sequences.
     */
    fun largestCollatzStarter(n: Int): Int = longestCountedUnderN[n - 1]

    /**
     * Returns the length of a Collatz sequence given its [start]ing number.
     */
    fun collatzLength(start: Int): Int {
        var count = 1
        var prev = start
        while (prev != 1) {
            // if x AND 1 == 0 then x is even
            prev = if (prev and 1 == 0) prev / 2 else prev * 3 + 1
            // bitwise AND between positive x and x-1 will be zero if x is a power of 2
            if (prev != 0 && (prev and (prev - 1) == 0)) {
                count += log2(1.0 * prev).toInt() + 1
                break
            } else {
                count++
            }
        }
        return count
    }

    /**
     * Generates all starting numbers < 5e6 that produce the longest sequence,
     * [longestCountedUnderN].
     */
    fun generateLongestStarters() {
        var longestStarter = 1
        var longestCount = 1
        for (i in 2..limit) {
            val currentLength = collatzLengthMemo(1L * i)
            if (currentLength >= longestCount) {
                longestStarter = i
                longestCount = currentLength
            }
            longestCountedUnderN[i - 1] = longestStarter
        }
    }

    /**
     * Recursive solution uses saved lengths of previously determined Collatz sequences to speed
     * performance of calling function.
     */
    private fun collatzLengthMemo(start: Long): Int {
        return if (start <= limit && countedSeq[start.toInt() - 1] != 0) {
            countedSeq[start.toInt() - 1]
        } else {
            // if x AND 1 > 0 then x is odd
            val count: Int = 1 + if (start and 1 > 0) {
                // add a division by 2 as oddStart * 3 + 1 gives an even number,
                // so both steps can be combined
                collatzLengthMemo((3 * start + 1) / 2)
            } else {
                collatzLengthMemo(start / 2)
            }
            if (start <= limit) countedSeq[start.toInt() - 1] = count
            count
        }
    }
}