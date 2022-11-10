package dev.bogwalk.batch9

import dev.bogwalk.util.combinatorics.combinations
import dev.bogwalk.util.custom.Fraction
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Problem 93: Arithmetic Expressions
 *
 * https://projecteuler.net/problem=93
 *
 * Goal: Distinct positive integer outcomes can be achieved from expressions created using a set
 * of M distinct digits, each used exactly once with any of the operators +, -, *, /, as well as
 * parentheses. Find the largest possible integer N, such that [1, N] is expressible. If 1 is
 * not even a possible outcome, return 0.
 *
 * Constraints: 1 <= M <= 5
 *
 * e.g.: M = 2 -> {2, 8}
 *       outcomes = {8/2, 8-2, 2+8, 2*8} = {4, 6, 10, 16}
 *       output = 0
 *
 *       M = 2 -> {1, 2}
 *       outcomes = {1+2, 2-1, 1*2 or 2/1} = {3, 1, 2}
 *       output = 3
 */

class ArithmeticExpressions {
    /**
     * Recursive solution repeatedly reduces the number of digits in the set by evaluating all
     * possible expressions between different combinations of 2 digits in the set. The digits are
     * treated as Doubles from the beginning so that, once only 1 digit remains in the set, any
     * floating-point values can be handled to ensure no expressible outcomes are missed.
     *
     * Note that non-commutative operations have to be performed twice to account for the
     * alternative ordered combinations.
     *
     * N.B. Cache size was initially calculated using the product of the M largest digits:
     *
     *          listOf(5, 6, 7, 8, 9).takeLast(m).reduce { acc, d -> acc * d })
     *
     * Exhaustive search showed that this was excessive as, while these maximum values are
     * achievable, the longest streaks barely make it a fraction of the way:
     *      1 digit -> 1
     *      2 digits -> 3
     *      3 digits -> 10
     *      4 digits -> 51
     *      5 digits -> 192
     *
     * @param [digits] List of unique digits sorted in ascending order.
     */
    fun highestStreak(digits: List<Int>): Int {
        val maxCache = 200
        val expressed = BooleanArray(maxCache)  // value 1 at index 0

        fun evaluateExpression(digits: List<Double>) {
            if (digits.size == 1) {
                val final = digits.single()
                val finalAsInt = final.roundToInt()
                if (abs(final - finalAsInt) <= 0.00001 && finalAsInt in 1..maxCache) {
                    expressed[finalAsInt - 1] = true
                }
            } else {
                for (i in 0 until digits.lastIndex) {
                    for (j in i+1..digits.lastIndex) {
                        val newDigits = digits.toMutableList().apply {
                            removeAt(j)
                            removeAt(i)
                        }
                        val x = digits[i]
                        val y = digits[j]
                        // commutative operators
                        newDigits.add(x + y)
                        evaluateExpression(newDigits)
                        newDigits.replaceLast(x * y)
                        evaluateExpression(newDigits)
                        // non-commutative operators
                        newDigits.replaceLast(x - y)
                        evaluateExpression(newDigits)
                        newDigits.replaceLast(y - x)
                        evaluateExpression(newDigits)
                        if (y != 0.0) {
                            newDigits.replaceLast(x / y)
                            evaluateExpression(newDigits)
                        }
                        if (x != 0.0) {
                            newDigits.replaceLast(y / x)
                            evaluateExpression(newDigits)
                        }
                    }
                }
            }
        }

        evaluateExpression(digits.map(Int::toDouble))
        // index of first false means end of streak comes at previous index
        return expressed.indexOfFirst { !it }
    }

    /**
     * Identical to the previous solution except that digits are treated as custom class,
     * Fraction, (rational numbers) to avoid any floating-point issues.
     *
     * @param [digits] List of unique digits sorted in ascending order.
     */
    fun highestStreakWithFractions(digits: List<Int>): Int {
        val maxCache = 200
        val expressed = BooleanArray(maxCache)

        fun evaluateExpression(digits: List<Fraction>) {
            if (digits.size == 1) {
                val final = digits.single()
                if (final.denominator == 1 && final.numerator in 1..maxCache) {
                    expressed[final.numerator - 1] = true
                }
            } else {
                for (i in 0 until digits.lastIndex) {
                    for (j in i+1..digits.lastIndex) {
                        val newDigits = digits.toMutableList().apply {
                            removeAt(j)
                            removeAt(i)
                        }
                        val x = digits[i]
                        val y = digits[j]
                        newDigits.add(x + y)
                        evaluateExpression(newDigits)
                        newDigits.replaceLast(x * y)
                        evaluateExpression(newDigits)
                        newDigits.replaceLast(x - y)
                        evaluateExpression(newDigits)
                        newDigits.replaceLast(y - x)
                        evaluateExpression(newDigits)
                        if (y != Fraction()) {
                            newDigits.replaceLast(x / y)
                            evaluateExpression(newDigits)
                        }
                        if (x != Fraction()) {
                            newDigits.replaceLast(y / x)
                            evaluateExpression(newDigits)
                        }
                    }
                }
            }
        }

        evaluateExpression(digits.map { Fraction(it) })
        return expressed.indexOfFirst { !it }
    }

    private fun <T> MutableList<T>.replaceLast(element: T) {
        removeLast()
        add(element)
    }

    /**
     * Project Euler specific implementation that returns a String representation of the set of
     * digits that can express the longest streak of positive integers.
     *
     * Note that, including the digit 0 would have resulted in 210 4-digit combinations instead of
     * 126. It was left out because exhaustive search showed that [1, 2] was the longest streak
     * achievable by a 4-digit set that includes 0.
     */
    fun longestStreakSet(m: Int = 4): String {
        var longestStreak = 0 to ""
        for (digitCombo in combinations(1..9, m)) {
            val highest = highestStreak(digitCombo)
            if (highest > longestStreak.first) {
                longestStreak = highest to digitCombo.joinToString("")
            }
        }
        return longestStreak.second
    }
}