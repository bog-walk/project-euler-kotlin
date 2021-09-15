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
    fun lexicographicPerm(
        n: Long, input: String, permutation: String = ""
    ): String {
        val permutations = (input.length).factorial().toLong()
        return when (n) {
            1L -> {
                println("Base case first reached")
                permutation + input
            }
            permutations -> {
                println("Base case last reached")
                permutation + input.reversed()
            }
            else -> {
                // Find index of next letter
                val i = (n / (permutations / input.length)).toInt() - 1
                // Find new n for next recursion
                val new_n = n % (permutations / input.length) + 1
                // Remove letter at index above
                val new_input = input.removeRange(i..i)
                val ans_sofar = permutation + input[i]
                println("Re-entering with n=$new_n input=$new_input ans=$ans_sofar")
                lexicographicPerm(
                    new_n, new_input, ans_sofar
                )
            }
        }
    }
}

fun main() {
    val tool = LexicographicPermutations()
    val ans = tool.lexicographicPerm(2, "abc")
    println(ans)
}