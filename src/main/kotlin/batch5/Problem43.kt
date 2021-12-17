package batch5

import util.getPermutations
import kotlin.system.measureNanoTime

/**
 * Problem 43: SubstringDivisibility
 *
 * https://projecteuler.net/problem=43
 *
 * Goal: Find the sum of all 0 to N pandigital numbers that have their
 * 3-digit substrings, starting from d_2, being divisible by sequential primes.
 *
 * Constraints: 3 <= N <= 9
 *
 * Substring Divisibility: The 0 to 9 pandigital, 1406357289 ->
 * d_2 .. d_4 = 406 % 2 == 0
 * d_3 .. d_5 = 063 % 3 == 0
 * d_4 .. d_6 = 635 % 5 == 0
 * d_5 .. d_7 = 357 % 7 == 0
 * d_6 .. d_8 = 572 % 11 == 0
 * d_7 .. d_9 = 728 % 13 == 0
 * d_8 .. d_10 = 289 % 17 == 0
 *
 * e.g.: N = 3
 *       sum = 22212
 */

class SubstringDivisibility {

    /**
     * SPEED: 3.5926s for N = 9
     */
    fun sumOfPandigitalSubstrings(n: Int): Long {
        val primes = listOf(2, 3, 5, 7, 11, 13, 17)
        var sum = 0L
        val digits = ('0'..('0' + n)).toMutableList()
        next@for (pandigital in getPermutations(digits, n + 1)){
            val subs = pandigital.windowed(3).drop(1)
            for ((i, sub) in subs.withIndex()) {
                if (sub.toInt() % primes[i] != 0) continue@next
            }
            sum += pandigital.toLong()
        }
        return sum
    }

    /**
     * Project Euler specific implementation that only required the sum
     * of all 0 to 9 pandigital numbers that have substring divisibility.
     * Filtering the generated permutations through a sequence allowed the
     * performance speed to be improved compared to the above solution.
     *
     * SPEED (BETTER): 2.0863s for N = 9
     */
    fun sumOf9PandigitalSubstrings(): Long {
        val digits = ('0'..'9').toMutableList()
        val primes = listOf(2, 3, 5, 7, 11, 13, 17)
        return getPermutations(digits, 10)
            .asSequence()
            .filter { perm ->
                (perm.first() == '1' || perm.first() == '4') &&
                        (perm.last() == '7' || perm.last() == '9')
            }
            .mapNotNull { perm ->
                var mapping: Long? = perm.toLong()
                for (i in 1..7) {
                    if (perm.substring(i..i+2).toInt() % primes[i - 1] != 0) {
                        mapping = null
                        break
                    }
                }
                mapping
            }
            .sum()
    }
}