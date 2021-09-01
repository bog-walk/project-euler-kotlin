package util

import java.math.BigInteger
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

/**
 * gcd(x, y) = gcd(|x * y|, |x|); where |x| >= |y|
 * &
 * gcd(x, 0) = gcd(0, x) = |x|
 */
fun gcd(n1: Int, n2: Int): Int {
    val x = abs(n1)
    val y = abs(n2)
    if (x == 0 || y == 0) return x + y
    val bigger = maxOf(x, y)
    val smaller = minOf(x, y)
    return gcd(bigger % smaller, smaller)
}

tailrec fun Int.factorial(run: BigInteger = BigInteger.ONE): BigInteger {
    require(this >= 0) { "Integer must not be negative" }
    return when (this) {
        0 -> BigInteger.ONE
        1 -> run
        else -> (this - 1).factorial(run * this.toBigInteger())
    }
}

/**
 * Returns prime decomposition in exponential form.
 * N = 12 returns {2=2, 3=1} -> 2^2 * 3^1 = 12
 */
fun primeFactors(n: Long): Map<Long, Int> {
    require(n > 1) { "Must provide a natural number greater than 1" }
    var num = n
    val primes = mutableMapOf<Long, Int>()
    val maxFactor = sqrt(num.toDouble()).toLong()
    val factors = listOf(2L) + (3L..maxFactor step 2L)
    for (factor in factors) {
        while (num % factor == 0L) {
            primes[factor] = primes.getOrDefault(factor, 0) + 1
            num /= factor
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
    if (max < 2) return emptyList()
    val primes = mutableListOf(2)
    (3..max step 2).filter {
        it.isPrime()
    }.toCollection(primes)
    return primes
}

fun getPrimesUsingSieve(max: Int): List<Int> {
    if (max < 2) return emptyList()
    val primesBool = BooleanArray(max - 1) { !(it != 0 && it % 2 == 0) }
    for (p in 3..(sqrt(1.0 * max).toInt()) step 2) {
        if (primesBool[p - 2]) {
            if (p * p > max) break
            for (m in (p * p)..max step 2 * p) {
                primesBool[m - 2] = false
            }
        }
    }
    return primesBool.mapIndexed { i, isPrime ->
        if (isPrime) i + 2 else null
    }.filterNotNull()
}