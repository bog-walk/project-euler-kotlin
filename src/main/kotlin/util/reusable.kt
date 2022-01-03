package util

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
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
 *
 * SPEED: 36.64ms for N = 1e5
 */
fun primeNumbersOG(max: Int): List<Int> {
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

/**
 * Still uses Sieve of Eratosthenes method to output all prime numbers
 * less than or equal to the upper bound provided, but cuts processing
 * time in half by only allocating mask memory to odd numbers and by only
 * looping through multiples of odd numbers. Will be used in future solution sets.
 *
 * SPEED (BETTER): 23.11ms for N = 1e5
 */
fun primeNumbers(max: Int): List<Int> {
    if (max < 2) return emptyList()
    val oddSieve = (max - 1) / 2
    // Creates BooleanArray representing range of 2,3..max step 2
    val primesBool = BooleanArray(oddSieve + 1) { true }
    // primesBool[0] corresponds to prime 2 and is skipped
    for (i in 1..(sqrt(1.0 * max).toInt() / 2)) {
        if (primesBool[i]) {
            // j = next index at which multiple of odd prime exists
            var j = i * 2 * (i + 1)
            while (j <= oddSieve) {
                primesBool[j] = false
                j += 2 * i + 1
            }
        }
    }
    return primesBool.mapIndexed { i, isPrime ->
        if (i == 0) 2 else if (isPrime) 2 * i + 1 else null
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

fun isPrimeLong(n: Long): Boolean {
    return when {
        n < 2L -> false
        n < 4L -> true // 2 & 3
        n % 2L == 0L -> false // 2 is only even prime
        n < 9L -> true // 4, 6, & 8 already excluded
        n % 3L == 0L -> false // primes > (k=3) are of form 6k(+/-1) (i.e. never multiples of 3)
        else -> {
            // N can only have 1 prime factor > sqrt(N): N itself!
            val max = floor(sqrt(1.0 * n))
            var step = 5L // as multiples of prime 5 have not been assessed yet
            // 11, 13, 17, 19, & 23 will all bypass n loop
            while (step <= max) {
                if (n % step == 0L) return false
                if (n % (step + 2) == 0L) return false
                step += 6L
            }
            true
        }
    }
}

fun isLargePrime(
    n: Long,
    kRounds: List<BigInteger> = listOf(
        2.toBigInteger(), 3.toBigInteger(), 5.toBigInteger(),
        7.toBigInteger(), 11.toBigInteger()
    )
): Boolean {
    if (n in 2L..3L) return true
    if (n < 2L || n % 2L == 0L) return false
    // write n as (2^s * d) + 1 by factoring
    // out powers of 2 from n - 1 until d is odd
    var s = 0
    var d = n - 1
    while (d % 2L == 0L) {
        s++
        d /= 2L
    }
    val num = n.toBigInteger()
    rounds@for (a in kRounds) {
        if (a > num - BigInteger.TWO) break
        // calculate a^d % n (pow will not suffice)
        var x = BigInteger.ONE
        var base = a
        var exp = d
        while (exp > 0) {
            //if (exp % 2L != 0L) x = (x * base) % n
            if (exp and 1L == 1L) x = (x * base) % num
            //exp /= 2L
            exp = exp shr 1
            base = (base * base) % num
        }
        if (x == BigInteger.ONE || x == num - BigInteger.ONE) continue@rounds
        for (i in 0 until s) {
            x = (x * x) % num
            if (x == BigInteger.ONE) break
            if (x == num - BigInteger.ONE) continue@rounds
        }
        return false
    }
    return true
}

/**
 * This version, not the 3 alternatives below, will be used in future solutions.
 *
 * SPEED (BEST): 2.32ms for 18-digit N tested 1000 times
 */
fun String.isPalindrome(): Boolean {
    return when {
        length < 2 -> true
        first() == last() -> substring(1, lastIndex).isPalindrome()
        else -> false
    }
}

/**
 * SPEED: 41.24ms for 18-digit N tested 1000 times
 */
fun String.isPalindromeInBuilt() = this == this.reversed()

/**
 * SPEED: 6.02ms for 18-digit N tested 1000 times
 */
fun String.isPalindromeManual(): Boolean {
    if (length == 1) return true
    val mid = lastIndex / 2
    val range = if (length % 2 == 1) (0 until mid) else (0..mid)
    for (i in range) {
        if (this[i] != this[lastIndex - i]) return false
    }
    return true
}

/**
 * SPEED: 4.44ms for 18-digit N tested 1000 times
 */
tailrec fun String.isPalindromeTailRec(): Boolean {
    return when {
        length < 2 -> true
        first() != last() -> false
        else -> substring(1, lastIndex).isPalindromeTailRec()
    }
}

/**
 * Euclid's formula to generate all Pythagorean triplets from 2 numbers m and n.
 * All triplets originate from a primitive one by multiplying them by d = gcd(a,b,c).
 * Note the following assumptions:
 * - m > n > 0.
 * - m and n cannot both be odd.
 * - m and n must be co-prime, i.e. gcd(m, n) == 1
 */
fun pythagoreanTriplet(m: Int, n: Int, d: Int): Triple<Int, Int, Int> {
    val a = (m * m - n * n) * d
    val b = 2 * m * n * d
    val c = (m * m + n * n) * d
    return Triple(minOf(a, b), maxOf(a, b), c)
}

/**
 * Heap's Algorithm to generate all n! permutations of a list of
 * n characters with minimal movement.
 * Initially k = n, then recursively k-- and each step generates k!
 * permutations that end with the same n - k elements. Each step modifies
 * the initial k - 1 elements with a swap based on k's parity.
 * N.B. Consider implementing iterative approach if N > 10, otherwise
 * OutOfMemoryError will be thrown.
 *
 * @param [chars] MutableList<Char> of size <= 10
 * @param [size] K -> initial size of [chars]
 * @return List<String> of n! permutations.
 */
fun getPermutations(
    chars: MutableList<Char>,
    size: Int,
    perms: MutableList<String> = mutableListOf()
): List<String> {
    if (size == 1) {
        perms.add(chars.joinToString(""))
    } else {
        repeat(size) { i ->
            getPermutations(chars, size - 1, perms)
            // avoids unnecessary swaps of the kth & 0th element
            if (i < size - 1) {
                if (size % 2 == 0) {
                    val swap = chars[i]
                    chars[i] = chars[size - 1]
                    chars[size - 1] = swap
                } else {
                    val swap = chars.first()
                    chars[0] = chars[size - 1]
                    chars[size - 1] = swap
                }
            }
        }
    }
    return perms
}

/**
 * Derivation solution is based on the following:
 * 0.5 * n * (n + 1) = t_n ->
 * inverse function, positive solution ->
 * n = 0.5 * (sqrt((8 * t_n) + 1) - 1)
 *
 * @return  If tN is the nth triangular, or null
 */
fun isTriangularNumber(tN: Long): Int? {
    val n = 0.5 * (sqrt(8.0 * tN + 1) - 1)
    return if (n == floor(n)) n.toInt() else null
}

/**
 * Derivation solution is based on the following:
 * 0.5 * n * (3 * n - 1) = p_n ->
 * inverse function, positive solution ->
 * n = (sqrt((24 * p_n) + 1) + 1) / 6
 *
 * @return  If pN is the nth pentagonal, or null
 */
fun isPentagonalNumber(pN: Long): Int? {
    val n = (sqrt(24.0 * pN + 1) + 1) / 6.0
    return if (n == floor(n)) n.toInt() else null
}