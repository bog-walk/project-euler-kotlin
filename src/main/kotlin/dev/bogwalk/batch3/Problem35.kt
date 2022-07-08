package dev.bogwalk.batch3

import dev.bogwalk.util.maths.isPrime
import dev.bogwalk.util.maths.primeNumbers
import dev.bogwalk.util.search.binarySearch

/**
 * Problem 35: Circular Primes
 *
 * https://projecteuler.net/problem=35
 *
 * Goal: Find the sum of all circular primes less than N, with rotations allowed to exceed N, &
 * the rotations themselves allowed as duplicates if below N.
 *
 * Constraints: 10 <= N <= 1e6
 *
 * Circular Prime: All rotations of the number's digits are themselves prime.
 * e.g. 197 -> {197, 719, 971}.
 *
 * e.g.: N = 100
 *       circular primes = {2,3,5,7,11,13,17,31,37,71,73,79,97}
 *       sum = 446
 */

class CircularPrimes {
    /**
     * Increase this solution's efficiency by only using Sieve of Eratosthenes once
     * to calculate all primes less than the upper constraint.
     */
    fun sumOfCircularPrimes(n: Int): Int {
        return getCircularPrimes(n).sum()
    }

    /**
     * Solution is optimised by filtering out primes with any even digits as an even digit means
     * at least 1 rotation will be even and therefore not prime.
     *
     * The primes list is also searched using a binary search algorithm, which brings solution
     * speed from 489.23ms (originally using r in primes) to 205.29ms.
     *
     * @return unsorted list of circular primes < [n].
     */
    fun getCircularPrimes(n: Int): List<Int> {
        val primes = primeNumbers(n - 1).filter {
            it == 2 || it > 2 && it.toString().none { ch -> ch in "02468" }
        }
        if (n == 10) return primes
        val circularPrimes = mutableListOf<Int>()
        for (prime in primes) {
            if (prime < 10) {
                circularPrimes.add(prime)
                continue
            }
            val pRotated = getRotations(prime)
            // avoid duplicates and non-primes
            if (
                pRotated.any { r ->
                    r in circularPrimes || r < n && !binarySearch(r, primes) || !r.isPrime()
                }
            ) continue
            circularPrimes += pRotated.filter { it < n }
        }
        return circularPrimes
    }

    private fun getRotations(num: Int): Set<Int> {
        val rotations = mutableSetOf(num)
        var rotation = num.toString()
        // number of possible rotations minus original already listed
        val r = rotation.length - 1
        repeat(r) {
            rotation = rotation.substring(r) + rotation.substring(0 until r)
            rotations += rotation.toInt()
        }
        return rotations
    }
}