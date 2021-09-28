package batch3

import util.factorial

/**
 * Problem 24: Lexicographic Permutations
 *
 * https://projecteuler.net/problem=24
 *
 * Goal: Return the Nth lexicographic permutation of "abcdefghijklm".
 *
 * Constraints: 1 <= N <= 13!
 *
 * Lexicographic Permutation: the alphabetically/numerically ordered
 * arrangements of an object.
 * e.g. "abc" -> {"abc", "acb", "bac", "bca", "cab", "cba"}
 *
 * e.g.: N = 1 -> "abcdefghijklm"
 *       N = 2 -> "abcdefghijkml"
 */

class LexicographicPermutations {
    /**
     * Recursive solution uses factorial (permutations without repetition)
     * to calculate next character in permutation based on batch position.
     * e.g. "abcd" has 4! = 24 permutations & each letter will have 6
     * permutations in which that letter will be the 1st in the order.
     *
     * @param[n] the nth permutation requested; should be zero-indexed.
     * @param[input] the object to generate permutations of; should be
     * already sorted in ascending lexicographic order.
     */
    fun lexicographicPerm(
        n: Long,
        input: String,
        permutation: String = ""
    ): String {
        val permutations = (input.length).factorial().toLong()
        return when (n) {
            0L -> permutation + input
            else -> {
                val batchSize = permutations / input.length
                val i = (n / batchSize).toInt()
                lexicographicPerm(
                    n % batchSize,
                    input.removeRange(i..i),
                    permutation + input[i]
                )
            }
        }
    }
}