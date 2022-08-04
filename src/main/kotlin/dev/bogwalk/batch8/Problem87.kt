package dev.bogwalk.batch8

import dev.bogwalk.util.maths.primeNumbers
import kotlin.math.sqrt

/**
 * Problem 87: Prime Power Triples
 *
 * https://projecteuler.net/problem=87
 *
 * Goal: Count how many numbers <= N can be expressed as the sum of a prime square, prime
 * cube, and prime fourth power.
 *
 * Constraints: 1 <= N <= 1e7
 *
 * e.g.: N = 50
 *       28 = 2^2 + 2^3 + 2^4
 *       33 = 3^2 + 2^3 + 2^4
 *       49 = 5^2 + 2^3 + 2^4
 *       47 = 2^2 + 3^3 + 2^4
 *       count = 4
 */

class PrimePowerTriples {
    /**
     * Stores all cumulative counts for how many numbers <= index N can be expressed as a triple
     * prime-powered sum.
     */
    fun allPrimePowerTripleCounts(n: Int): List<Int> {
        val triple = BooleanArray(n + 1)
        val primes = primeNumbers(sqrt(1.0 * n).toInt())
        nextA@for (a in primes) {
            val totalA = a * a
            nextB@for (b in primes) {
                val totalB = totalA + b * b * b
                if (totalB > n) continue@nextA
                for (c in primes) {
                    val totalC = totalB + c * c * c * c
                    if (totalC > n) continue@nextB
                    triple[totalC] = true
                }
            }
        }
        var count = 0
        // could be done with runningFold() but access would have to change to how many < index
        // as initial value takes first 0-index slot
        return triple.map { valid -> if (valid) ++count else count }
    }
}