package util

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * This exists solely for handling input in HackerRank submission.
 */
fun handleInput() {
    repeat(readLine()!!.trim().toInt()) {
        val ans = readLine()!!.trim().toInt()
        println(ans)
    }
}

fun getPrimeFactors(n: Long): Map<Long, Int> {
    var num = abs(n)
    val primes = mutableMapOf<Long, Int>()
    while (num % 2 == 0L) {
        primes[2] = primes.getOrDefault(2, 0) + 1
        num /= 2
    }
    if (num > 1) {
        val maxFactor = sqrt(num.toDouble()).toLong()
        for (i in 3L..maxFactor step 2L) {
            while (num % i == 0L) {
                primes[i] = primes.getOrDefault(i, 0) + 1
                num /= i
            }
        }
    }
    if (num > 2) primes[num] = primes.getOrDefault(num, 0) + 1
    return primes
}

/**
 * This algorithm uses all facts about primes to test primality of N.
 */
fun Int.isPrime(): Boolean {
    return when {
        this < 2 -> false
        this < 4 -> true // 2 & 3
        this % 2 == 0 -> false // 2 is only even prime
        this < 9 -> true // 4, 6, & 8 already excluded
        this % 3 == 0 -> false // primes >3 are of form 6k+/-1 (never multiples of 3)
        else -> {
            // N can only have 1 prime factor > sqrt(N): N itself!
            val max = floor(sqrt(this.toDouble()))
            var step = 5 // as the next number not yet excluded is 10
            while (step <= max) {
                if (this % step == 0) return false
                if (this % (step + 2) == 0) return false
                step += 6
            }
            true
        }
    }
}

/**
 * Consider using Sieve of Eratosthenes since an upper bound
 * is being provided in advance.
 */
fun getPrimeNumbers(max: Int): List<Int> {
    return (2..max).filter {
        it.isPrime()
    }.toList()
}