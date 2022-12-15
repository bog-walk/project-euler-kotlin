package dev.bogwalk.batch9

import kotlin.math.log10
import kotlin.math.pow

/**
 * Problem 99: Largest Exponential
 *
 * https://projecteuler.net/problem=99
 *
 * Goal: Compare 2 base (B) /exponent (E) pairs whose numbers contain a very large amount of
 * digits, then use this comparison to either find the Kth smallest exponential or the greatest
 * value exponential from a list of N pairs.
 *
 * Constraints: 1 <= N <= 1e5, 1 <= K <= N, 1 <= B <= 1e9, 1 <= E <= 1e9
 *
 * e.g.: N = 3, K = 2, list -> [4 7, 3 7, 2 11]
 *       sorted -> [2 11, 3 7, 4 7]
 *       output = 3 7
 */

class LargestExponential {
    data class Exponential(
        val base: Int, val exponent: Int, val line: Int
    ) : Comparable<Exponential> {
        /**
         * Solution avoids using large numbers for comparison by reducing both values using the
         * root of the object with the smaller exponent, based on the following:
         *
         *      (a^x)^(1/x) == x_root(a^x) == a
         *      then the object with the larger exponent becomes:
         *      (b^y)^(1/x) == b^(y/x)
         *
         * Based on the large values of the exponents, this provides a more manageable comparison.
         *
         * N.B. An alternative calculates the gcd of the exponents and depends on the following:
         *
         *      a^(x * y) == (a^x)^y
         *
         * e.g. 2^350 and 5^150 == (2^7)^50 and (5^3)^50; 2^7 (128) > 5^3 (125), so 2^350 has teh
         * greater value. This only works if the gcd is assured to be greater than 1.
         */
        override operator fun compareTo(other: Exponential): Int {
            val smallerExp = minOf(this.exponent, other.exponent)
            val reducedExp: Double
            val reducedA: Double
            val reducedB: Double
            if (this.exponent == smallerExp) {
                reducedA = this.base.toDouble()
                reducedExp = other.exponent / smallerExp.toDouble()
                reducedB = other.base.toDouble().pow(reducedExp)
            } else {
                reducedB = other.base.toDouble()
                reducedExp = this.exponent / smallerExp.toDouble()
                reducedA = this.base.toDouble().pow(reducedExp)
            }

            return if (reducedA > reducedB) 1 else -1
        }
    }

    /**
     * Project Euler specific implementation that requires the line number of the base/exponent
     * pair that has the greatest numerical value, from a 1000-line test file.
     */
    fun largestExponential(inputs: List<String>): Triple<Int, Int, Int> {
        val largest =  inputs.mapIndexed { i, str ->
            val nums = str.split(",")
            Exponential(nums[0].toInt(), nums[1].toInt(), i + 1)
        }.reduce { acc, triple ->
            if (acc > triple) acc else triple
        }
        return Triple(largest.base, largest.exponent, largest.line)
    }

    /**
     * HackerRank specific implementation that requires the [k]th smallest exponential from a
     * list of base/exponent pairs.
     *
     * Data class Exponential's compareTo() is used to sort the input values, then the [k-1]th value
     * is accessed.
     *
     * SPEED (WORSE) 28.13ms for PE test list
     */
    fun kSmallestExponential(inputs: List<String>, k: Int): Pair<Int, Int> {
        val exponentials = inputs.mapIndexed { i, str ->
            val nums = str.split(" ")
            Exponential(nums[0].toInt(), nums[1].toInt(), i + 1)
        }
        val kSmallest = exponentials.sortedBy { it }[k - 1]
        return kSmallest.base to kSmallest.exponent
    }

    /**
     * Solution optimised by not relying on data class instances and instead sorting
     * base/exponent pairs solely based on their logarithm calculation, based on the following:
     *
     *      a^x > b^y -> log_10(a^x) > log_10(b^y) -> x * log_10(a) > y * log_10(b)
     *
     * SPEED (BETTER) 12.83ms for PE test list
     */
    fun kSmallestExponentialAlt(inputs: List<String>, k: Int): Pair<Int, Int> {
        val exponentials = inputs.map { str ->
            val (base, exponent) = str.split(" ").map(String::toInt)
            Triple(base, exponent, exponent * log10(base.toDouble()))
        }
        val kSmallest = exponentials.sortedBy { it.third }[k - 1]
        return kSmallest.first to kSmallest.second
    }
}