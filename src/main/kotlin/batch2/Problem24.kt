package batch2

import util.maths.factorial

/**
 * Problem 24: Lexicographic Permutations
 *
 * https://projecteuler.net/problem=24
 *
 * Goal: Return the Nth lexicographic permutation of "abcdefghijklm".
 *
 * Constraints: 1 <= N <= 13!
 *
 * Lexicographic Permutation: The alphabetically/numerically ordered arrangements of an object.
 * e.g. "abc" -> {"abc", "acb", "bac", "bca", "cab", "cba"}
 *
 * e.g.: N = 1 -> "abcdefghijklm"
 *       N = 2 -> "abcdefghijkml"
 */

class LexicographicPermutations {
    /**
     * Recursive solution uses factorial (permutations without repetition) to calculate the next
     * character in the permutation based on batch position.
     *
     * e.g. "abcd" has 4! = 24 permutations & each letter will have 6 permutations in which that
     * letter will be the 1st in the order. If n = 13, this permutation will be in batch 2
     * (starts with "c") at position 1 (both 0-indexed). So "c" is removed and n = 1 is used with
     * the new string "abd". This continues until n = 0 and "cabd" is returned by the base case.
     *
     * SPEED (EQUAL) 5.5e5ns for 10-digit string
     *
     * @param [n] the nth permutation requested should be zero-indexed.
     * @param [input] the object to generate permutations of should be already sorted in
     * ascending order.
     */
    fun lexicographicPerm(
        n: Long,
        input: String,
        permutation: String = ""
    ): String {
        return if (n == 0L) {
            permutation + input
        } else {
            val batchSize = (input.length).factorial().toLong() / input.length
            val i = (n / batchSize).toInt()
            lexicographicPerm(
                n % batchSize,
                input.removeRange(i..i),
                permutation + input[i]
            )
        }
    }

    /**
     * Recursive solution above is altered by removing the unnecessary creation of a storage
     * string to pass into every recursive call, as well as reducing the factorial call, since
     * x! / x = (x - 1)!
     *
     * SPEED (EQUAL) 7.7e5ns for 10-digit string
     *
     * @param [n] the nth permutation requested should be zero-indexed.
     * @param [input] the object to generate permutations of should be already sorted in
     * ascending order.
     */
    fun lexicographicPermImproved(n: Long, input: String): String {
        return if (input.length == 1) {
            input
        } else {
            val batchSize = (input.length - 1).factorial().toLong()
            val i = (n / batchSize).toInt()
            input[i] + lexicographicPermImproved(
                n % batchSize, input.removeRange(i..i)
            )
        }
    }
}