package dev.bogwalk.batch0

import dev.bogwalk.util.maths.isPrime

/**
 * Problem 7: The 10001st Prime
 *
 * https://projecteuler.net/problem=7
 *
 * Goal: Find the Nth prime number.
 *
 * Constraints: 1 <= N <= 10_001
 *
 * e.g.: N = 6
 *       primes = {2,3,5,7,11,13,...}
 *       6th prime = 13
 */

class The10001stPrime {
    /**
     * After the number 2, every prime number is odd, so this solution iterates over all odd
     * numbers & checks for primality using an optimised helper function, until the [n]th
     * prime is found.
     */
    fun getNthPrime(n: Int): Int {
        if (n == 1) return 2
        var count = n - 1
        var number = 1
        while (count > 0) {
            number += 2
            if (number.isPrime()) count--
        }
        return number
    }
}