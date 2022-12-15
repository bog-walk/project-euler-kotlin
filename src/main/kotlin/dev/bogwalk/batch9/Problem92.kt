package dev.bogwalk.batch9

import dev.bogwalk.util.combinatorics.permutationID
import dev.bogwalk.util.maths.factorial
import java.math.BigInteger

/**
 * Problem 92: Square Digit Chains
 *
 * https://projecteuler.net/problem=92
 *
 * Goal: Return the count of starting numbers below 10^K will arrive at 89 (modulo 10^9 + 7).
 *
 * Constraints: 1 <= K <= 200
 *
 * Square Digit Chain: Sequence created by adding the square of the digits in a number to form a
 * new number until it has been seen before.
 *      e.g. 44 -> 32 -> 13 -> 10 -> 1 -> 1
 *           85 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 * All starting numbers will eventually end in an infinite loop of 1 or 89.
 *
 * Happy Number: A positive integer whose square digit chain ends in 1. The first few are: {1, 7,
 * 10, 13, 19, 23}. If a number is known to be happy, any number in its sequence is also happy,
 * as are any permutations of that number.
 * Unhappy Number: A positive integer whose square digit chain does not end in 1. The first few are:
 * {2, 3, 4, 5, 6, 8}.
 *
 * e.g.: K = 1
 *       1 -> 1
 *       2 -> 4 -> 16 -> 37 -> 58 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       3 -> 9 -> 81 -> 65 -> 61 -> 37 -> 58 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       4 -> 16 -> 37 -> 58 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       5 -> 25 -> 29 -> 85 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       6 -> 36 -> 45 -> 41 -> 17 -> 50 -> 25 -> 29 -> 85 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       7 -> 49 -> 97 -> 130 -> 10 -> 1 -> 1
 *       8 -> 64 -> 52 -> 29 -> 85 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       9 -> 81 -> 65 -> 61 -> 37 -> 58 -> 89 -> 145 -> 42 -> 20 -> 4 -> 16 -> 37 -> 58 -> 89
 *       count = 7
 */

class SquareDigitChains {
    private val modulo = 1_000_000_007
    private val squares = List(10) { it * it }

    /**
     * Iterates through all starters to find the end chain value and stores results in a cache to
     * reduce chain formation in later starters.
     *
     * SPEED (WORSE) 2.95s for K = 7 (becomes unbearably long at K = 10)
     */
    fun countSDChainsBrute(k: Int): Int {
        val sumLimit = 200 * 9 * 9
        val cache = IntArray(sumLimit + 1)
        var count = 0

        val limit = BigInteger.TEN.pow(k)
        var starter = BigInteger.TWO
        while (starter < limit) {
            var number = digitSquaredSum(starter.toString())
            while (number != 1 && number != 89) {
                when (cache[number]) {
                    1 -> {
                        number = 1
                        break
                    }
                    89 -> {
                        number = 89
                        break
                    }
                    0 -> {
                        number = digitSquaredSum(number.toString())
                    }
                }
            }
            if (number == 89) {
                count++
                count %= modulo
            }
            if (starter < sumLimit.toBigInteger()) {
                cache[starter.intValueExact()] = number
            }
            starter++
        }

        return count
    }

    private fun digitSquaredSum(number: String): Int {
        return number.sumOf { ch -> squares[ch.digitToInt()] }
    }

    /**
     * Iterates through all potential sums of squared [k] digits and stores in a cache the amount
     * of starters that will result in each sum. Each potential sum is then iterated over and, if
     * found to have the desired end chain value, its stored count is accumulated.
     *
     * SPEED (BEST) 1.6e+05ns for K = 7
     */
    fun countSDChainsImproved(k: Int): Int {
        val cache = IntArray(k * 9 * 9 + 1)
        // single digit sums
        for (square in squares) {
            cache[square]++
        }
        for (digits in 2..k) {
            for (sum in digits * 9 * 9 downTo 1) {
                for (d in 1..9) {
                    val square = d * d
                    if (square > sum) {
                        break
                    }
                    // since sums are iterating backwards, add count of all sums without
                    // the most recent digit d
                    cache[sum] += cache[sum-square]
                    cache[sum] %= modulo
                }
            }
        }

        var count = 0
        for (i in 2..k * 9 * 9) {
            if (cache[i] == 0) continue
            var sum = i
            while (sum != 1 && sum != 89) {
                sum = digitSquaredSum(sum.toString())
            }
            if (sum == 89) {
                count += cache[i]
                count %= modulo
            }
        }
        return count
    }

    /**
     * Similar to the brute force method but attempts to cache starters based on the premise that
     * all permutations of an unhappy number will also be unhappy.
     *
     * SPEED (WORSE) 2.96s for K = 7
     */
    fun countSDChainsPerm(k: Int): Int {
        val cache = mutableMapOf(
            permutationID(1L) to 1,
            permutationID(89L) to 89
        )
        var count = 0

        val limit = BigInteger.TEN.pow(k)
        var starter = BigInteger.TWO
        while (starter < limit) {
            var number = digitSquaredSum(starter.toString())
            val starterPermID = permutationID(number.toLong())
            if (starterPermID in cache.keys) {
                if (cache[starterPermID] == 89) {
                    count++
                    count %= modulo
                }
            } else {
                while (number != 1 && number != 89) {
                    number = digitSquaredSum(number.toString())
                    val nPermID = permutationID(number.toLong())
                    if (nPermID in cache.keys) {
                        number = cache[nPermID]!!
                        if (number == 89) {
                            count++
                            count %= modulo
                        }
                        break
                    }
                }
                cache[starterPermID] = number
            }
            starter++
        }

        return count
    }

    /**
     * This solution attempts to mimic the PARI/GP code that generates a count of all happy
     * numbers below 10^[k]. This could then be subtracted from the limit or the helper function
     * switched to find the count of unhappy numbers.
     *
     * TODO!!! - issue with mimicking `forvec` iterative behaviour.
     *
     * @see <a href="https://oeis.org/A068571">PARI/GP code</a>
     */
    fun countHappyNums(k: Int): Int {
        val factor = k.factorial()
        val digits = List(9) { (it + 1).toBigInteger() }
        var count = 0

        var v: IntArray
        var d: IntArray
        for (length in 1..k) {
            v = IntArray(9) { if (it == 8) length else 0 }
            d = IntArray(9) { (if (it > 7) k else v[it+1]) - v[it] }
            val sum = (1..9).sumOf { d[it-1] * it * it }
            if (isHappy(sum)) {
                val p = digits.reduce { acc, pI -> acc * d[pI.intValueExact()-1].factorial() }
                val toAdd = (factor / p / v[0].factorial()).intValueExact()
                count += toAdd
                count %= modulo
            }
        }
        for (i in 8 downTo 1) {
            v = IntArray(9) { if (it >= i-1) 1 else 0 }
            d = IntArray(9) { (if (it > 7) k else v[it+1]) - v[it] }
            var sum = (1..9).sumOf { d[it-1] * it * it }
            if (isHappy(sum)) {
                val p = digits.reduce { acc, pI -> acc * d[pI.intValueExact()-1].factorial() }
                val toAdd = (factor / p / v[0].factorial()).intValueExact()
                count += toAdd
                count %= modulo
            }
            for (j in 9 downTo i) {
                for (length in 2..k) {
                    v[j-1] = length
                    d = IntArray(9) { (if (it > 7) k else v[it+1]) - v[it] }
                    sum = (1..9).sumOf { d[it-1] * it * it }
                    if (isHappy(sum)) {
                        val p = digits.reduce { acc, pI -> acc * d[pI.intValueExact()-1].factorial() }
                        val toAdd = (factor / p / v[0].factorial()).intValueExact()
                        count += toAdd
                        count %= modulo
                    }
                }
            }
        }

        return count + 1
    }

    private fun isHappy(number: Int): Boolean {
        var sum = number
        // the first happy number is 7
        while (sum > 6 && sum != 89) {
            sum = digitSquaredSum(sum.toString())
        }
        return sum == 1
    }
}