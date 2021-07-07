package util

import kotlin.math.abs
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

fun Int.isPrime(): Boolean {
    val max = sqrt(this.toDouble()).toInt()
    return (2..max).all {
        this % it != 0
    }
}

fun getPrimeNumbers(max: Int): List<Int> {
    return (2..max).filter {
        it.isPrime()
    }.toList()
}