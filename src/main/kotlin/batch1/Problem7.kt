package batch1

import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Problem 7: 10001st Prime
 * Goal: Find the Nth prime number, where 1 <= N <= 10000
 * e.g. The 6th prime is 13.
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
            n % 3 == 0 -> false // primes >3 are of form 6k+/-1 (never multiples of 3)
            else -> {
                // N can only have 1 prime factor > sqrt(N): N itself!
                val max = floor(sqrt(n.toDouble()))
                var step = 5 // as the next number not yet excluded is 10
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