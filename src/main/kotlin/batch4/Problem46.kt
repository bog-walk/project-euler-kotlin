package batch4

import util.maths.isPrime
import util.maths.primeNumbers
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Problem 46: Goldbach's Other Conjecture
 *
 * https://projecteuler.net/problem=46
 *
 * Goal: Return the number of ways an odd composite number, N, can be represented as proposed by
 * Goldbach's Conjecture, detailed below.
 *
 * Constraints: 9 <= N < 5e5
 *
 * Goldbach's False Conjecture: Every odd composite number can be written as the sum of a prime
 * and twice a square. Proven to be FALSE.
 *
 *      e.g. 9 = 7 + 2 * 1^2
 *           15 = 7 + 2 * 2^2 || 13 + 2 * 1^2
 *           21 = 3 + 2 * 3^2 || 13 + 2 * 2^2 || 19 + 2 * 1^2
 *           25 = 7 + 2 * 3^2 || 17 + 2 * 2^2 || 23 + 2 * 1^2
 *           27 = 19 + 2 * 2^2
 *           33 = 31 + 2 * 1^2
 *
 * e.g.: N = 9
 *       result = 1
 *       N = 15
 *       result = 2
 */

class GoldbachsOtherConjecture {
    /**
     * Returns count of primes that allow [n] to be represented as per Goldbach's false conjecture.
     *
     * @param [n] an odd composite number (no in-built check to ensure it is not prime).
     */
    fun countGoldbachRepresentations(n: Int): Int {
        return primeNumbers(n)
            .drop(1)
            .count { prime ->
                n.isGoldbachOther(prime)
            }
    }

    private fun Int.isGoldbachOther(prime: Int): Boolean {
        val repr = sqrt((this - prime) / 2.0)
        return repr == floor(repr)
    }

    /**
     * Project Euler specific implementation that returns the smallest odd composite that cannot
     * be written, as proposed, as the sum of a prime and twice a square.
     *
     * The found value can be confirmed by using it as an argument in the HackerRank problem
     * solution above, as seen in the test cases.
     */
    fun smallestFailingComposite(): Int {
        // starting limit guessed with contingency block later down
        var limit = 5000
        var primes = primeNumbers(limit).drop(1)
        // starting point as provided in example
        var composite = 33
        nextC@while (true) {
            composite += 2
            if (composite.isPrime()) continue@nextC
            for (prime in primes) {
                if (prime > composite) break@nextC
                if (composite.isGoldbachOther(prime)) continue@nextC
            }
            // if reached, means not enough primes for current composite
            limit += 5000
            primes = primeNumbers(limit).drop(1)
            composite -= 2
        }
        return composite
    }
}