package batch1

import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Problem 7: The 10001st Prime
 *
 * https://projecteuler.net/problem=7
 *
 * Goal: Find the Nth prime number.
 *
 * Constraints: 1 <= N <= 10001
 *
 * e.g.: N = 6
 *       primes = {2,3,5,7,11,13,...}
 *       6th prime = 13
 */

class The10001stPrime {
    /**
     * This algorithm uses all facts about primes to test primality of N.
     */
    fun isPrime(n: Int): Boolean {
        return when {
            n < 2 -> false
            n < 4 -> true // 2 & 3
            n % 2 == 0 -> false // 2 is only even prime
            n < 9 -> true // 4, 6, & 8 already excluded
            n % 3 == 0 -> false // primes > (k=3) are of form 6k(+/-1) (i.e. never multiples of 3)
            else -> {
                // N can only have 1 prime factor > sqrt(N): N itself!
                val max = floor(sqrt(1.0 * n))
                var step = 5 // as multiples of prime 5 have not been assessed yet
                // 11, 13, 17, 19, & 23 will all bypass n loop
                while (step <= max) {
                    if (n % step == 0) return false
                    if (n % (step + 2) == 0) return false
                    step += 6
                }
                true
            }
        }
    }

    fun getNthPrime(n: Int): Int {
        if (n == 1) return 2
        var count = n - 1
        var number = 1
        while (count > 0) {
            number += 2
            if (isPrime(number)) count--
        }
        return number
    }
}