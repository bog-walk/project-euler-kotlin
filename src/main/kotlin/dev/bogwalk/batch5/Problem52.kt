package dev.bogwalk.batch5

import kotlin.math.pow

/**
 * Problem 52: Permuted Multiples
 *
 * https://projecteuler.net/problem=52
 *
 * Goal: Find all positive integers, x <= N, such that all requested multiples (x, 2x, ..., Kx)
 * are a permutation of x.
 *
 * Constraints: 125_875 <= N <= 2e6, 2 <= K <= 6
 *
 * e.g.: N = 125_875, K = 2
 *       output = [[125_874, 251_748]]
 */

class PermutedMultiples {
    /**
     * Solution optimised by limiting loops to between 10^m and 10^(m+1) / [k], where m is the
     * amount of digits at the time, as any higher starting integer will gain more digits when
     * multiplied & not be a permutation.
     */
    fun permutedMultiples(n: Int, k: Int): List<List<Int>> {
        val results = mutableListOf<List<Int>>()
        var start = 125_874
        var digits = 6
        while (start <= n) {
            val end = minOf(n + 1, (10.0).pow(digits).toInt() / k)
            xLoop@for (x in start until end) {
                val xString = x.toString()
                val perms = mutableListOf(x)
                for (m in 2..k) {
                    val multiple = x * m
                    if (xString.isPermutation((multiple).toString())) {
                        perms.add(multiple)
                    } else continue@xLoop
                }
                results.add(perms)
            }
            digits++
            start = (10.0).pow(digits - 1).toInt()
        }
        return results
    }

    private fun String.isPermutation(other: String): Boolean {
        return this.toCharArray().sorted() == other.toCharArray().sorted()
    }

    /**
     * Project Euler specific implementation that finds the smallest positive integer, x, such
     * that 2x, ..., 6x are all permutations of x.
     */
    fun smallestPermutedMultiple(): Int {
        var x = 125_875
        var perms = 1
        while (perms != 6) {
            x++
            val xString = x.toString()
            for (m in 2..6) {
                if (xString.isPermutation((x * m).toString())) {
                    perms++
                } else {
                    perms = 1
                    break
                }
            }
        }
        return x
    }
}