package batch5

import util.primeNumbers
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Problem 46: Goldbach's Other Conjecture
 *
 * https://projecteuler.net/problem=46
 *
 * Goal: Return the number of ways an odd composite number, N, can be
 * represented as proposed by Goldbach's Conjecture, detailed below.
 *
 * Constraints: 9 <= N < 5e5
 *
 * Goldbach's False Conjecture: Every odd composite number can be
 * written as the sum of a prime and twice a square. Proven to be FALSE.
 * e.g. 9 = 7 + 2 * 1^2
 *      15 = 7 + 2 * 2^2 or 13 + 2 * 1^2
 *      21 = 3 + 2 * 3^2 or 13 + 2 * 2^2 or 19 + 2 * 1^2
 *      25 = 7 + 2 * 3^2 or 17 + 2 * 2^2 or 23 + 2 * 1^2
 *      27 = 19 + 2 * 2^2
 *      33 = 31 + 2 * 1^2
 *
 * e.g.: N = 9
 *       result = 1
 *       N = 15
 *       result = 2
 */

class GoldbachsOtherConjecture {
    private fun isGoldbachOther(composite: Int, prime: Int): Boolean {
        val repr = sqrt((composite - prime) / 2.0)
        return repr == floor(repr)
    }

    /**
     * @param   [n] should be an odd composite number, as there is no
     * built-in primality check.
     */
    fun countGoldbachRepresentations(n: Int): Int {
        return primeNumbers(n)
            .drop(1)
            .count { prime ->
                isGoldbachOther(n, prime)
            }
    }

    /**
     * Project Euler specific implementation that returns the smallest odd
     * composite that cannot be written, as proposed, as the sum of a prime
     * and twice a square.
     */
    fun smallestFailingComposite(): Int {
        val primes = primeNumbers(10_000).drop(1)
        // starting point as provided in example
        var composite = 33
        nextC@while (true) {
            composite += 2
            if (composite in primes) continue@nextC
            nextP@for (prime in primes) {
                if (prime > composite) break@nextC
                if (isGoldbachOther(composite, prime)) break@nextP
            }
        }
        return composite
    }
}