package batch2

/**
 * Problem 14: Longest Collatz Sequence
 * Goal: Find the starting number N, such that
 * 1 < N < 10^6, the produces the longest Collatz chain.
 * Collatz sequences, thought to all finish at 1, are defined
 * for positive integer starting numbers as:
 * n -> n / 2 (if n is even) & n -> 3n + 1 (if n is odd).
 */

class LongestCollatzSequence {
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
}