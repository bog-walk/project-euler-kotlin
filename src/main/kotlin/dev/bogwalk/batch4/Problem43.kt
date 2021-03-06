package dev.bogwalk.batch4

import dev.bogwalk.util.combinatorics.permutations

/**
 * Problem 43: Substring Divisibility
 *
 * https://projecteuler.net/problem=43
 *
 * Goal: Find the sum of all 0 to N pandigital numbers that have their 3-digit substrings,
 * starting from d_2, being divisible by sequential primes.
 *
 * Constraints: 3 <= N <= 9
 *
 * Substring Divisibility: The 0 to 9 pandigital, 1_406_357_289 ->
 *      d_2 .. d_4 = 406 % 2 == 0
 *      d_3 .. d_5 = 063 % 3 == 0
 *      d_4 .. d_6 = 635 % 5 == 0
 *      d_5 .. d_7 = 357 % 7 == 0
 *      d_6 .. d_8 = 572 % 11 == 0
 *      d_7 .. d_9 = 728 % 13 == 0
 *      d_8 .. d_10 = 289 % 17 == 0
 *
 * e.g.: N = 3
 *       sum = 22212
 */

class SubstringDivisibility {
    private val primes = listOf(0, 2, 3, 5, 7, 11, 13, 17)

    /**
     * Solution uses built-in function, windowed(), to create a new list of all 3-digit subStrings.
     *
     * SPEED (WORST) 6.33s for N = 9
     */
    fun sumOfPandigitalSubstrings(n: Int): Long {
        var sum = 0L
        val digits = '0'..('0' + n)
        next@for (perm in permutations(digits)) {
            val pandigital = perm.joinToString("")
            val subs = pandigital.windowed(3).drop(1)
            for ((i, sub) in subs.withIndex()) {
                if (sub.toInt() % primes[i + 1] != 0) continue@next
            }
            sum += pandigital.toLong()
        }
        return sum
    }

    /**
     * Solution optimised by replacing windowed() with a loop that slides over the permutation,
     * checking each new subString & breaking early if invalid.
     *
     * SPEED (BETTER) 4.85s for N = 9
     */
    fun sumOfPandigitalSubstringsImproved(n: Int): Long {
        var sum = 0L
        val digits = '0'..('0' + n)
        next@for (perm in permutations(digits)) {
            val pandigital = perm.joinToString("")
            for (i in 1 until n - 1) {
                val sub = pandigital.substring(i..i+2)
                if (sub.toInt() % primes[i] != 0) continue@next
            }
            sum += pandigital.toLong()
        }
        return sum
    }

    /**
     * Project Euler specific implementation that only requires the sum of all 0 to 9 pandigital
     * numbers that have substring divisibility.
     *
     * Filtering the generated permutations through a sequence allowed the performance speed to
     * be improved compared to the previous solution above.
     *
     * Filters pandigital permutations based on the following:
     *
     *  - [d_2, d_4] must be divisible by 2 so d_4 must be an even number.
     *
     *  - [d_4, d_6] must be divisible by 5 so d_6 must be '0' or '5', which is narrowed down to
     *  '5' as [d_6, d_8] must be divisible by 11 and '0' would not allow pandigital options.
     *
     *  - [d_3, d_5] must be divisible by 3, so [d_3, d_5].sum() must be also so.
     *
     *  - If eligible numbers are narrowed down manually, it is proven that d_1 and d_2 are
     *  either '1' or '4' and d_10 is either '7' or '9'.
     *
     * SPEED (BEST) 3.24s for N = 9
     */
    fun sumOf9PandigitalSubstrings(): Long {
        return permutations('0'..'9')
            .filter { perm ->
                perm.first() in "14" &&
                        perm.last() in "79" &&
                        perm[3].digitToInt() % 2 == 0 &&
                        perm[5] == '5' &&
                        perm.slice(2..4).sumOf(Char::digitToInt) % 3 == 0
            }
            .sumOf { perm ->
                var num = perm.joinToString("")
                for (i in 1..7) {
                    if (num.substring(i..i+2).toInt() % primes[i] != 0) {
                        num = "0"
                        break
                    }
                }
                num.toLong()
            }
    }
}