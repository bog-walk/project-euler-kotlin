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
 *
 * SPEED: 2.1e7ns for num = 999_999
 */
fun sumProperDivisorsOG(num: Int): Int {
    if (num < 2) return 0
    var sum = 1
    var maxDivisor = sqrt(1.0 * num).toInt()
    if (maxDivisor * maxDivisor == num) {
        sum += maxDivisor
        maxDivisor--
    }
    val range = if (num % 2 != 0) (3..maxDivisor step 2) else (2..maxDivisor)
    for (d in range) {
        if (num % d == 0) {
            sum += d + num / d
        }
    }
    return sum
}

/**
 * Further optimised function that uses prime factorisation to out-perform
 * the original method above. Will be used in future solution sets.
 *
 * SPEED (BETTER): 7.1e3ns for num = 999_999
 */
fun sumProperDivisorsPF(num: Int): Int {
    if (num < 2) return 0
    var n = num
    var sum = 1
    var p = 2
    while (p * p <= num && n > 1) {
        if (n % p == 0) {
            var j = p * p
            n /= p
            while (n % p == 0) {
                j *= p
                n /= p
            }
            sum *= (j - 1)
            sum /= (p - 1)
        }
        if (p == 2) {
            p++
        } else {
            p += 2
        }
    }
    if (n > 1) {
        sum *= (n + 1)
    }
    return sum - num
}

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