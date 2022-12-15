package dev.bogwalk.batch9

import dev.bogwalk.util.combinatorics.permutationID
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Problem 98: Anagramic Squares
 *
 * https://projecteuler.net/problem=98
 *
 * Goal: Find all square numbers with N digits that have at least 1 anagram that is also a square
 * number with N digits. Use this set to either map squares to anagram strings or to find the
 * largest square with the most anagramic squares in the set.
 *
 * Constraints: 3 <= N <= 13
 *
 * e.g.: N = 4
 *       only 1 set of anagramic squares is of length > 2 -> {1296, 2916, 9216}
 *       largest = 9216
 */

class AnagramicSquares {
    /**
     * Project Euler specific implementation that requests the largest square number possible from
     * any anagramic square pair found within a large list of words.
     *
     * e.g. CARE, when mapped to 1296 (36^2), forms a pair with its anagram RACE, which maps to
     * one of the square's own anagrams, 9216 (96^2).
     *
     * @return Triple of Anagram word #1, Anagram word #2, the largest mapped square.
     */
    fun findLargestAnagramicSquareByWords(words: List<String>): Triple<String, String, Int> {
        val anagrams = findAnagrams(words)
        var numOfDigits = Int.MAX_VALUE
        var squares = emptyList<List<String>>()
        var largest = Triple("", "", 0)
        for (anagram in anagrams) {
            if (anagram.first.length < numOfDigits) {
                numOfDigits = anagram.first.length
                squares = getEligibleSquares(numOfDigits)
            }
            for (squareSet in squares) {
                nextSquare@for (square in squareSet) {
                    // ensure square can be mapped to first word without different chars using
                    // the same digit; e.g. CREATION should not map to 11323225
                    val mapping = mutableMapOf<Char, Char>()
                    for ((ch, digit) in anagram.first.zip(square)) {
                        if (ch in mapping.keys) {
                            if (mapping[ch] == digit) continue else continue@nextSquare
                        } else {
                            if (digit in mapping.values) continue@nextSquare else mapping[ch] = digit
                        }
                    }
                    val expected = anagram.second.map { mapping[it]!! }.joinToString("")
                    if (expected in squareSet) {
                        val largerSquare = maxOf(square.toInt(), expected.toInt())
                        if (largerSquare > largest.third) {
                            largest = Triple(anagram.first, anagram.second, largerSquare)
                        }
                    }
                }
            }
            // only interested in the largest found from the first set of squares with a result
            if (largest.first.isNotEmpty()) break
        }
        return largest
    }

    /**
     * Returns all sets of anagrams found in a list of words, sorted in descending order by the
     * length of the words.
     */
    private fun findAnagrams(words: List<String>): List<Pair<String, String>> {
        val permutations = mutableMapOf<String, List<String>>()
        for (word in words) {
            if (word.length <= 2) continue  // palindromes are not anagrams
            val permId = word.toList().sorted().joinToString("")
            permutations[permId] = permutations.getOrDefault(permId, emptyList()) + word
        }

        val anagrams = mutableListOf<Pair<String, String>>()
        for (permMatch in permutations.values) {
            when (val count = permMatch.size) {
                1 -> continue
                2 -> anagrams.add(permMatch[0] to permMatch[1])
                else -> {  // only occurs once (size 3) from test resource, but let's be dynamic
                    for (i in 0 until count - 1) {
                        for (j in i + 1 until count) {
                            anagrams.add(permMatch[i] to permMatch[j])
                        }
                    }
                }
            }
        }
        return anagrams.sortedByDescending { it.first.length }
    }

    /**
     * Returns all squares that have the desired number of digits and that are anagrams of other
     * squares (based on their permutation id), with the latter being grouped together.
     *
     * Note that squares are found and returned in groups already sorted in ascending order.
     */
    private fun getEligibleSquares(digits: Int): List<List<String>> {
        val allSquares = mutableMapOf<String, List<String>>()
        val maximum = 10.0.pow(digits).toLong()
        var base = sqrt(10.0.pow(digits - 1)).toLong()
        var square = base * base
        while (square < maximum) {
            val permId = permutationID(1L * square)
            allSquares[permId] = allSquares.getOrDefault(permId, emptyList()) + square.toString()
            base++
            square = base * base
        }
        return allSquares.values.filter { it.size > 1 }
    }

    /**
     * HackerRank specific implementation that only requires that all possible anagram squares
     * with [n] digits be found. From this list of sets, the largest square from the largest set
     * must be returned; otherwise, return the largest square from all equivalently sized sets.
     */
    fun findLargestAnagramicSquareByDigits(n: Int): Long {
        val squares = getEligibleSquares(n)
        var largestSize = 0
        var largestSquare = 0L
        for (squareSet in squares) {
            if (squareSet.size > largestSize) {
                largestSize = squareSet.size
                largestSquare = squareSet.last().toLong()
                continue
            }
            if (squareSet.size == largestSize) {
                largestSquare = max(largestSquare, squareSet.last().toLong())
            }
        }
        return largestSquare
    }
}