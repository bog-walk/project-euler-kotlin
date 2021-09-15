package util

import java.math.BigInteger
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

/**
 * gcd(x, y) = gcd(|x * y|, |x|); where |x| >= |y|
 * &
 * gcd(x, 0) = gcd(0, x) = |x|
 */
fun gcd(n1: Long, n2: Long): Long {
    val x = abs(n1)
    val y = abs(n2)
    if (x == 0L || y == 0L) return x + y
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
 * lcm(x, y) = |x * y| / gcd(x, y)
 */
fun lcm(n1: Long, n2: Long): Long {
    require(n1 != 0L && n2 != 0L) { "Neither parameter can be 0" }
    return abs(n1 * n2) / gcd(n1, n2)
}

/**
 * Uses Sieve of Eratosthenes method to output all prime numbers
 * less than or equal to the upper bound provided.
 */
fun primeNumbers(max: Int): List<Int> {
    if (max < 2) return emptyList()
    // Creates BooleanArray representing range of 2..max, with all even numbers
    // except 2 (index 0) marked False
    val primesBool = BooleanArray(max - 1) { !(it != 0 && it % 2 == 0) }
    for (p in 3..(sqrt(1.0 * max).toInt()) step 2) {
        if (primesBool[p - 2]) {
            if (p * p > max) break
            // Mark all multiples (composites of the divisors) of p
            // that are >= the square of p as false
            for (m in (p * p)..max step 2 * p) {
                primesBool[m - 2] = false
            }
        }
    }
    return primesBool.mapIndexed { i, isPrime ->
        if (isPrime) i + 2 else null
    }.filterNotNull()
}

fun Int.gaussianSum(): Long  = 1L * this * (this + 1) / 2

/**
 * This solution is optimised based on the following:
 * - N == 1 has no proper divisor but 1 is a proper divisor of all other naturals;
 * - A perfect square would duplicate divisors if included in the loop range;
 * - Loop range differs for odd numbers as they cannot have even divisors.
 */
fun sumProperDivisors(num: Int): Int {
    if (num < 2) return 0
    var sum = 1
    val maxDivisor = sqrt(1.0 * num).toInt()
    if (maxDivisor * maxDivisor == num) {
        sum += maxDivisor
    }
    val range = if (num % 2 != 0) (3 until maxDivisor step 2) else (2 until maxDivisor)
    for (d in range) {
        if (num % d == 0) {
            sum += d + num / d
        }
    }
    return sum
}