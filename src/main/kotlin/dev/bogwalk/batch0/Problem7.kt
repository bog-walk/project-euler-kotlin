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
     * Solution iterates over all numbers & checks for primality using an optimised helper function,
     * until the [n]th prime is found.
     *
     * Odd numbers are not excluded from this iteration as this solution ran on average 7.80ms
     * for N = 10_001, compared to 8.21ms when the number starts at 1 and increments by 2.
     */
    fun getNthPrime(n: Int): Int {
        var count = n
        var number = 1
        while (count > 0) {
            if ((++number).isPrime()) count--
        }
        return number
    }

    /**
     * Solution generates a list of primes of size [count] for quick-draw access. The dynamic
     * list itself is used to test primality of every number based on the prime factorisation
     * principle.
     *
     * The same rules for primes apply, namely that every prime after 2 is odd and the only
     * factor greater than sqrt(x) would be x itself if x is prime.
     *
     * @return list of primes with the nth prime at index n-1.
     */
    fun getAllPrimes(count: Int): List<Int> {
        val primes = mutableListOf(2)
        var number = 1
        nextNum@while (primes.size < count) {
            number += 2
            for (p in primes) {
                if (p * p > number) break
                if (number % p == 0) continue@nextNum
            }
            primes.add(number)
        }
        return primes
    }
}