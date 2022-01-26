package batch3

import util.isPrime
import util.primeNumbers

/**
 * Problem 35: Circular Primes
 *
 * https://projecteuler.net/problem=35
 *
 * Goal: Find the sum of all circular primes less than N, with rotations allowed
 * to exceed N & the rotation themselves allowed as duplicates if below N.
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

    /**
     * Solution optimised by first getting all primes less than N then filtering
     * out primes with even digits as primes (other than 2) must be odd & an even
     * digit means at least 1 rotation will be even.
     */
    fun getCircularPrimes(n: Int): List<Int> {
        val primes = primeNumbers(n - 1).filter {
            it == 2 || it > 2 && it.toString().none { ch -> ch in "02468" }
        }
        if (n == 10) return primes
        val rotations = primes.slice(0 until 4).toMutableList()
        for (prime in primes.subList(4, primes.size)) {
            val pRotated = getRotations(prime)
            val invalid = pRotated.any {
                it in rotations || it < n && it !in primes || !isPrime(it)
            }
            if (invalid) continue
            rotations += pRotated.filter { it < n }
        }
        return rotations
    }

    /**
     * Increase this solution's efficiency by only using Sieve of Eratosthenes once
     * to calculate all primes less than the upper constraint.
     */
    fun sumOfCircularPrimes(n: Int): Int {
        return getCircularPrimes(n).sum()
    }
}