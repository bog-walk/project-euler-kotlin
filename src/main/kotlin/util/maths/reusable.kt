package util.maths

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Calculates the sum of the first [this] natural numbers.
 *
 * Conversion of very large Floats to Longs in this formula can lead to large rounding
 * losses, so Integer division by 2 is replaced with a single bitwise right shift,
 * as n >> 1 = n/(2^1).
 */
fun Int.gaussianSum(): Long  = 1L * this * (this + 1) shr 1

/**
 * Recursive calculation of the greatest common divisor of 2 Longs.
 *
 * gcd(x, y) = gcd(|x*y|, |x|); where |x| >= |y|
 *
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

/**
 * Tail recursive solution allows the compiler to replace recursion with iteration for
 * optimum efficiency & the reduction of StackOverflowErrors.
 *
 * 13! overflows 32 bits and 21! overflows 64 bits and 59! produces a result that is greater
 * than 1e80 (postulated to be the number of particles in the universe).
 *
 * @throws IllegalArgumentException If calling Int is negative.
 */
tailrec fun Int.factorial(run: BigInteger = BigInteger.ONE): BigInteger {
    require(this >= 0) { "Integer must not be negative" }
    return when (this) {
        0 -> BigInteger.ONE
        1 -> run
        else -> (this - 1).factorial(run * this.toBigInteger())
    }
}

/**
 * Returns the corresponding term of the number if pentagonal, or null.
 *
 * Derivation solution is based on the formula:
 *
 * n(3n - 1)/2 = pN, in quadratic form becomes:
 *
 * 0 = 3n^2 - n - 2pN, with a, b, c = 3, -1, (-2pN)
 *
 * putting these values in the quadratic formula becomes:
 *
 * n = (1 +/- sqrt(1 + 24pN))/6
 *
 * so the inverse function, positive solution becomes:
 *
 * n = (1 + sqrt(1 + 24pN))/6
 */
fun Long.isPentagonalNumber(): Int? {
    val n = (1 + sqrt(1 + 24.0 * this)) / 6.0
    return if (n == floor(n)) n.toInt() else null
}

fun Int.isPrime() = this.toLong().isPrime()

/**
 * Checks if [this] is prime.
 *
 * This version will be used preferentially, unless the argument is expected to frequently
 * exceed 1e13.
 *
 * SPEED (WORSE for N > 1e13) 42.98ms for 14-digit prime
 * SPEED (EQUAL for N ~ 1e13) 17.21ms for 13-digit prime
 * SPEED (BETTER for N ~ 1e12) 11.27ms for 12-digit prime
 * SPEED (BETTER for N < 1e6) 5.9e5ns for 3-digit prime
 */
fun Long.isPrime(): Boolean {
    if (log10(this.toDouble()).toInt() + 1 > 13) return this.isPrimeMR()
    return when {
        this < 2L -> false
        this < 4L -> true // 2 & 3 are primes
        this % 2L == 0L -> false // 2 is the only even prime
        this < 9L -> true // 4, 6, & 8 already excluded
        this % 3L == 0L -> false
        // primes > (k=3) are of the form 6k(+/-1) (i.e. they are never multiples of 3)
        else -> {
            // n can only have 1 prime factor > sqrt(n): n itself!
            val max = floor(sqrt(1.0 * this))
            var step = 5L // as multiples of prime 5 have not been assessed yet
            // 11, 13, 17, 19, & 23 will all bypass n loop
            while (step <= max) {
                if (this % step == 0L || this % (step + 2L) == 0L) return false
                step += 6L
            }
            true
        }
    }
}

/**
 * Miller-Rabin probabilistic algorithm determines if a large number [this] is likely to be prime.
 *
 * -   The number received [this], once determined to be odd, is expressed as n = 2^rs + 1, with s
 *      being odd.
 * -   A random integer, a, is chosen k times (higher k means higher accuracy), with 0 < a < n.
 * -   Calculate a^s % n. If this equals 1 or this plus 1 equals n while s has the powers of 2
 *      previously factored out returned, then [this] passes as a strong probable prime.
 * -   [this] should pass for all generated a.
 *
 * This algorithm uses a list of the first 5 primes instead of randomly generated a, as this has
 * been proven valid for numbers up to 2.1e12. Providing a list of the first 7 primes gives test
 * validity for numbers up to 3.4e14.
 *
 * SPEED (BETTER for N > 1e13) 26.93ms for 14-digit prime
 * SPEED (EQUAL for N ~ 1e13) 21.19ms for 13-digit prime
 * SPEED (WORSE for N ~ 1e12) 20.98ms for 12-digit prime
 * SPEED (WORSE for N < 1e6) 16.77ms for 3-digit prime
 */
fun Long.isPrimeMR(kRounds: List<Long> = listOf(2, 3, 5, 7, 11)): Boolean {
    if (this in 2L..3L) return true
    if (this < 2L || this % 2L == 0L) return false
    val n = this.toBigInteger()
    val one = BigInteger.ONE
    // write n as 2^r * s + 1 by factoring out powers of 2 from n - 1 (even) until s is odd
    var s = this - 1L
    while (s % 2L == 0L) {
        s /= 2L
    }
    rounds@for (a in kRounds) {
        if (a > this - 2L) break
        // calculate a^s % n (pow will not suffice)
        var x = BigInteger.ONE
        var base = a.toBigInteger()
        var exp = s.toBigInteger()
        while (exp > BigInteger.ZERO) {
            if (exp and one == one) x = (x * base) % n
            exp = exp shr 1
            base = base * base % n
        }
        if (x == one || x == n - one) continue@rounds
        while (s != this - 1L) {
            x = (x * x) % n
            s *= 2L
            if (x == one) break
            if (x == n - one) continue@rounds
        }
        return false
    }
    return true
}

/**
 * Returns the corresponding term of the number if triangular, or null.
 *
 * Derivation solution is based on the formula:
 *
 * n(n + 1)/2 = tN, in quadratic form becomes:
 *
 * 0 = n^2 + n - 2tN, with a, b, c = 1, 1, (-2tN)
 *
 * putting these values in the quadratic formula becomes:
 *
 * n = (-1 +/- sqrt(1 + 8tN))/2
 *
 * so the inverse function, positive solution becomes:
 *
 * n = (sqrt(1 + 8tN) - 1)/2
 */
fun Long.isTriangularNumber(): Int? {
    val n = 0.5 * (sqrt(1 + 8.0 * this) - 1)
    return if (n == floor(n)) n.toInt() else null
}

/**
 * Calculates the least common multiple of a variable amount of [n].
 *
 * lcm(x, y) = |x * y| / gcd(x, y)
 *
 * @throws IllegalArgumentException If any [n] = 0.
 */
fun lcm(vararg n: Long): Long {
    require(n.all { it != 0L }) { "Parameter cannot be 0" }
    return n.reduce { acc, num ->
        abs(acc * num) / gcd(acc, num)
    }
}

/**
 * Calculates the sum of the digits of the number produced by [base]^[exp].
 */
fun powerDigitSum(base: Int, exp: Int): Int {
    val num = base.toBigInteger().pow(exp).toString()
    return num.sumOf { it.digitToInt() }
}

/**
 * Prime decomposition using Sieve of Eratosthenes algorithm.
 *
 * Every prime number after 2 will be odd and there can be at most 1 prime factor greater than
 * sqrt([n]), which would be [n] itself if [n] is a prime.
 *
 * e.g. N = 12 returns {2=2, 3=1} -> 2^2 * 3^1 = 12
 *
 * @throws IllegalArgumentException If [n] <= 1.
 * @return Map of prime factors (keys) and their exponents (values).
 */
fun primeFactors(n: Long): Map<Long, Int> {
    require(n > 1) { "Must provide a natural number greater than 1" }
    var num = n
    val primes = mutableMapOf<Long, Int>()
    val maxFactor = sqrt(num.toDouble()).toLong()
    val factors = listOf(2L) + (3L..maxFactor step 2L)
    factors.forEach { factor ->
        while (num % factor == 0L) {
            primes[factor] = primes.getOrDefault(factor, 0) + 1
            num /= factor
        }
    }
    if (num > 2) primes[num] = primes.getOrDefault(num, 0) + 1
    return primes
}

/**
 * Sieve of Eratosthenes algorithm outputs all prime numbers less than or equal to [n].
 *
 * SPEED (WORSE) 28.75ms for N = 1e5
 */
fun primeNumbersOG(n: Int): List<Int> {
    if (n < 2) return emptyList()
    // create mask representing [2, max], with all even numbers except 2 (index 0) marked false
    val boolMask = BooleanArray(n - 1) { !(it != 0 && it % 2 == 0) }
    for (p in 3..(sqrt(1.0 * n).toInt()) step 2) {
        if (boolMask[p - 2]) {
            if (p * p > n) break
            // mark all multiples (composites of the divisors) that are >= the square of p as false
            for (m in (p * p)..n step 2 * p) {
                boolMask[m - 2] = false
            }
        }
    }
    return boolMask.mapIndexed { i, isPrime ->
        if (isPrime) i + 2 else null
    }.filterNotNull()
}

/**
 * Still uses Sieve of Eratosthenes method to output all prime numbers less than or equal to [n],
 * but cuts processing time in half by only allocating mask memory to odd numbers and by only
 * looping through multiples of odd numbers.
 *
 * This version will be used in future solutions.
 *
 * SPEED (BETTER) 7.46ms for N = 1e5
 */
fun primeNumbers(n: Int): List<Int> {
    if (n < 2) return emptyList()
    val oddSieve = (n - 1) / 2
    // create mask representing [2, 3..n step 2]
    val boolMask = BooleanArray(oddSieve + 1) { true }
    // boolMask[0] corresponds to prime 2 and is skipped
    for (i in 1..(sqrt(1.0 * n).toInt() / 2)) {
        if (boolMask[i]) {
            // j = next index at which multiple of odd prime exists
            var j = i * 2 * (i + 1)
            while (j <= oddSieve) {
                boolMask[j] = false
                j += 2 * i + 1
            }
        }
    }
    return boolMask.mapIndexed { i, isPrime ->
        if (i == 0) 2 else if (isPrime) 2 * i + 1 else null
    }.filterNotNull()
}

/**
 * Euclid's formula generates all Pythagorean triplets from 2 numbers, [m] and [n].
 *
 * All triplets originate from a primitive one by multiplying them by d = gcd(a,b,c).
 *
 * @throws IllegalArgumentException If arguments do not follow [m] > [n] > 0, or if both are odd,
 * or if they are not co-prime, i.e. gcd(m, n) != 1.
 */
fun pythagoreanTriplet(m: Int, n: Int, d: Int): Triple<Int, Int, Int> {
    require(n in 1 until m) { "Positive integers assumed to be m > n > 0" }
    require(!(m % 2 != 0 && n % 2 != 0)) { "Both integers cannot be odd" }
    require(gcd(1L * m, 1L * n) == 1L) { "Positive integers must be co-prime" }
    val a = (m * m - n * n) * d
    val b = 2 * m * n * d
    val c = (m * m + n * n) * d
    return Triple(minOf(a, b), maxOf(a, b), c)
}

/**
 * Calculates the sum of all divisors of [num], not inclusive of [num].
 *
 * Solution optimised based on the following:
 *
 * -    N == 1 has no proper divisor but 1 is a proper divisor of all other naturals.
 *
 * -    A perfect square would duplicate divisors if included in the loop range.
 *
 * -    Loop range differs for odd numbers as they cannot have even divisors.
 *
 * SPEED (WORSE) 6.91ms for N = 1e6 - 1
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
 * Calculates the sum of all divisors of [num], not inclusive of [num].
 *
 * Solution above is further optimised by using prime factorisation to out-perform the original
 * method.
 *
 * This version will be used in future solutions.
 *
 * SPEED (BETTER) 10000ns for N = 1e6 - 1
 */
fun sumProperDivisors(num: Int): Int {
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

fun main() {
    val n = 999_973_156_643
    println(n.isPrimeMR())
}