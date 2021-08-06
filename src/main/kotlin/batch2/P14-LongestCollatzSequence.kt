package batch2

import kotlin.math.log2

/**
 * Problem 14: Longest Collatz Sequence
 * Goal: Find the largest starting number <= N, such that
 * 1 <= N <= 5 x 10^6, the produces the longest Collatz chain.
 * Collatz sequences, thought to all finish at 1, are defined
 * for positive integer starting numbers as:
 * n -> n / 2 (if n is even) & n -> 3n + 1 (if n is odd).
 */

class LongestCollatzSequence {
    private val countedSeq = IntArray(5000001) { if (it == 0) 1 else 0 }
    private val longestCountedUnderN = IntArray(5000001) { if (it == 0) 1 else 0 }

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
                count += log2(1.0 * prev).toInt() - 1 + 2
                break
            } else {
                count++
            }
        }
        return count
    }

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

    fun longestCollatzImproved(max: Int): Int {
        var longest = 1
        var longestStarter = 1
        val min = (max / 2).coerceAtLeast(1)
        for (starter in min..max) {
            val count = collatzLengthSaved(1L * starter)
            if (count >= longest) {
                longest = count
                longestStarter = starter
            }
        }
        return longestStarter
    }

    fun longestCollatz(max: Int): Int {
        val overMax = mutableMapOf<Int, Int>()
        val sizes = IntArray(max) { if (it == 0) 1 else 0 }
        var longestStarter = 1
        var longest = 1
        for (starter in 2..max) {
            if (sizes[starter - 1] > 0) continue
            var count = 1
            val sequence = mutableListOf(starter)
            var prev = starter
            seq@while (prev != 1) {
                val next = if (prev % 2 == 0) prev / 2 else prev * 3 + 1
                if (next > max) {
                    if (overMax.containsKey(next)) {
                        count += overMax.getValue(next)
                        break@seq
                    } else {
                        count++
                        sequence.add(next)
                    }
                } else {
                    if (sizes[next - 1] == 0) {
                        count++
                        sequence.add(next)
                    } else {
                        count += sizes[next - 1]
                        break@seq
                    }
                }
                prev = next
            }
            if (count >= longest) {
                longestStarter = starter
                longest = count
            }
            for ((i, e)in sequence.withIndex()) {
                if (e <= max) {
                    sizes[e - 1] = count - i
                } else {
                    overMax[e] = count - i
                }
            }
            var factor = sizes[starter - 1]
            var multiple = starter * 2
            while (multiple <= max) {
                factor++
                sizes[multiple - 1] = factor
                if (factor >= longest) {
                    longestStarter = multiple
                    longest = factor
                }
                multiple *= 2
            }
        }
        return longestStarter
    }
}

// Bitwise AND operation between 2 values should be zero if a power of 2
fun Int.isPowerOfTwo(): Boolean = this != 0 && (this and(this - 1) == 0)