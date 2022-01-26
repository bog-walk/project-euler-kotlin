package batch1

import kotlin.math.log2

/**
 * Problem 14: Longest Collatz Sequence
 *
 * https://projecteuler.net/problem=14
 *
 * Goal: Find the largest starting number <= N that produces the
 * longest Collatz sequence.
 *
 * Constraints: 1 <= N <= 5e6
 *
 * Collatz Sequence: thought to all finish at 1, a sequence of
 * positive integer, such that:
 * (even n) n -> n / 2
 * (odd n)  n -> 3*n + 1
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
    // Will store previously counted Collatz sequence lengths
    private val countedSeq = IntArray(5000001) { if (it == 0) 1 else 0 }
    private val longestCountedUnderN = IntArray(5000001) { if (it == 0) 1 else 0 }

    /**
     * Generate a Collatz Sequence from the provided start number.
     */
    fun collatzSequence(start: Int): List<Int> {
        val sequence = mutableListOf(start)
        var prev = start
        while (prev != 1) {
            val next = if (prev % 2 == 0) prev / 2 else prev * 3 + 1
            sequence.add(next)
            prev = next
        }
        return sequence
    }

    fun collatzLength(start: Int): Int {
        var count = 1
        var prev = start
        while (prev != 1) {
            prev = if (prev % 2 == 0) prev / 2 else prev * 3 + 1
            if (prev.isPowerOfTwo()) {
                count += log2(1.0 * prev).toInt() + 1
                break
            } else {
                count++
            }
        }
        return count
    }

    /**
     * Recursive solution uses saved lengths of previously
     * determined Collatz sequences to speed performance.
     */
    private fun collatzLengthSaved(start: Long): Int {
        return if (start <= 5000000L && countedSeq[start.toInt() - 1] != 0) {
            countedSeq[start.toInt() - 1]
        } else {
            val count: Int = if (start % 2L == 0L) {
                1 + collatzLengthSaved(start / 2)
            } else {
                2 + collatzLengthSaved((3 * start + 1) / 2)
            }
            if (start <= 5000000L) countedSeq[start.toInt() - 1] = count
            count
        }
    }

    fun generateLongestSequences() {
        var longest = 1
        var longestCount = 1
        for (i in 2..5000000) {
            val count = collatzLengthSaved(1L * i)
            if (count >= longestCount) {
                longest = i
                longestCount = count
            }
            longestCountedUnderN[i - 1] = longest
        }
    }

    fun longestCollatzMemo(max: Int): Int = longestCountedUnderN[max - 1]
}

// Bitwise AND operation between 2 values should be zero if a power of 2
fun Int.isPowerOfTwo(): Boolean = this != 0 && (this and(this - 1) == 0)