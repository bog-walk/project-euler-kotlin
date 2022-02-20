package batch4

import util.combinatorics.permutations
import util.maths.isPrime
import util.strings.isPandigital
import kotlin.math.pow

/**
 * Problem 41: Pandigital Prime
 *
 * https://projecteuler.net/problem=41
 *
 * Goal: Find the largest pandigital prime <= N or return -1 if none exists.
 *
 * Constraints: 10 <= N < 1e10
 *
 * e.g.: N = 100
 *       return -1, as no 2-digit pandigital primes exist; however,
 *       N = 10_000
 *       return 4231, as the smallest pandigital prime.
 */

class PandigitalPrime {
    /**
     * Project Euler specific implementation that returns the largest n-digit pandigital prime
     * that exists.
     *
     * This solution checks primality of all pandigital permutations backwards starting from 9
     * digits.
     *
     * N.B. There are only 538 pandigital primes & they are all either 4-/7-digit pandigitals, as
     * explained in following function below.
     */
    fun largestPandigitalPrimePE(): Int {
        val magnitudes = List(9) { d -> (10.0).pow(d).toInt() }
        println()
        var n = 987_654_321
        var digits = 9
        var limit = magnitudes[digits - 1]
        while (true) {
            if (n.toString().isPandigital(digits) && n.isPrime()) break
            n -= 2
            if (n < limit) {
                digits--
                limit = magnitudes[digits - 1]
            }
        }
        return n
    }

    fun largestPandigitalPrimeHR(n: Long): Int {
        if (n < 1423L) return -1
        return allPandigitalPrimes()
            .asSequence()
            .filter { it.toLong() <= n }
            .first()
    }

    /**
     * Solution optimised based on the following:
     *
     *  - The smallest pandigital prime is 4-digits -> 1423.
     *
     *  - Found using the brute force backwards search in the function above, the largest
     *  pandigital prime is 7-digits -> 7_652_413.
     *
     *  - The above 2 proven bounds confirm that only 4- & 7-digit pandigitals can be prime
     *  numbers as all primes greater than 3 are of the form 6p(+/- 1) & so cannot be multiples
     *  of 3. If the sum of a pandigital's digits is a multiple of 3, then that number will be a
     *  multiple of 3 & thereby not a prime. Only 4- & 7-digit pandigitals have sums that are not
     *  divisible by 3.
     *
     * @return list of all pandigital primes sorted in descending order.
     */
    private fun allPandigitalPrimes(): List<Int> {
        val pandigitalPrimes = mutableListOf<Int>()
        val digits = (7 downTo 1).toMutableList()
        repeat(2) {
            for (perm in permutations(digits, digits.size)) {
                val num = perm.joinToString("").toInt()
                if (num.isPrime()) {
                    pandigitalPrimes.add(num)
                }
            }
            digits.removeIf { it > 4 }
        }
        return pandigitalPrimes.sortedDescending()
    }
}