package util.maths

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Returns the number of ways to choose [k] items from [n] items without repetition and without
 * order, namely C(n, k).
 *
 * @throws IllegalArgumentException if either Int is negative.
 */
fun binomialCoefficient(n: Int, k: Int): BigInteger {
    require(n >= 0 && k >= 0) { "Both numbers must be non-negative" }
    if (k > n) return BigInteger.ZERO
    return n.factorial() / (k.factorial() * (n - k).factorial())
}

/**
 * Calculates the sum of the first [this] natural numbers.
 *
 * Conversion of very large Floats to Longs in this formula can lead to large rounding
 * losses, so Integer division by 2 is replaced with a single bitwise right shift,
 * as n >> 1 = n/(2^1).
 */
fun Int.gaussianSum(): Long  = 1L * this * (this + 1) shr 1

/**
 * Recursive calculation of the greatest common divisor of [n1] and [n2].
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
 * @throws IllegalArgumentException if calling Int is negative.
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

/**
 * Checks if [this] is prime.
 *
 * This version will be used preferentially, unless the argument is expected to frequently
 * exceed Integer.MAX_VALUE (10-digits).
 *
 * SPEED (WORST for N > 1e13) 43.62ms for 14-digit prime
 *
 * SPEED (EQUAL for N > 1e12) 17.21ms for 13-digit prime
 *
 * SPEED (WORST for N > 1e11) 8.68ms for 12-digit prime
 *
 * SPEED (BETTER for N < 1e10) 1.65ms for 10-digit prime
 *
 * SPEED (BEST for N < 1e6) 6.4e5ns for 6-digit prime
 *
 * SPEED (EQUAL for N < 1e3) 6.2e5ns for 3-digit prime
 */
fun Int.isPrime(): Boolean {
    return when {
        this < 2 -> false
        this < 4 -> true // 2 & 3 are primes
        this % 2 == 0 -> false // 2 is the only even prime
        this < 9 -> true // 4, 6, & 8 already excluded
        this % 3 == 0 -> false
        // primes > (k=3) are of the form 6k(+/-1) (i.e. they are never multiples of 3)
        else -> {
            // n can only have 1 prime factor > sqrt(n): n itself!
            val max = floor(sqrt(1.0 * this))
            var step = 5 // as multiples of prime 5 have not been assessed yet
            // 11, 13, 17, 19, & 23 will all bypass n loop
            while (step <= max) {
                if (this % step == 0 || this % (step + 2) == 0) return false
                step += 6
            }
            true
        }
    }
}

/**
 * BigInteger built-in isProbablePrime() uses Miller-Rabin algorithm.
 *
 * Overall, this solution returns the most consistent performance for numbers of all sizes, at a
 * default certainty of 5. It is expected that performance will slow as the number of digits and
 * certainty tolerance increase.
 *
 * This version will be used for all numbers above Integer.MAX_VALUE (11-digits and up).
 *
 * SPEED (BEST for N > 1e14) 1.12ms for 15-digit prime
 *
 * SPEED (BEST for N > 1e13) 1.11ms for 14-digit prime
 *
 * SPEED (BEST for N > 1e12) 1.26ms for 13-digit prime
 *
 * SPEED (BETTER for N > 1e11) 1.56ms for 12-digit prime
 *
 * SPEED (BEST for N < 1e10) 1.46ms for 10-digit prime
 *
 * SPEED (BETTER for N < 1e6) 9.4e5ns for 6-digit prime
 *
 * SPEED (EQUAL for N < 1e3) 7.2e5ns for 3-digit prime
 *
 * @see <a href="https://developer.classpath.org/doc/java/math/BigInteger-source.html">Source
 * code line 1279</a>
 */
fun Long.isPrimeMRBI(certainty: Int = 5): Boolean {
    val n = BigInteger.valueOf(this)
    return n.isProbablePrime(certainty)
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
 * The algorithm's complexity is O(k*log^3*n). This algorithm uses a list of the first 5 primes
 * instead of randomly generated a, as this has been proven valid for numbers up to 2.1e12.
 * Providing a list of the first 7 primes gives test validity for numbers up to 3.4e14.
 *
 * SPEED (BETTER for N > 1e14) 28.86ms for 15-digit prime
 *
 * SPEED (BETTER for N > 1e13) 17.88ms for 14-digit prime
 *
 * SPEED (EQUAL for N > 1e12) 17.62ms for 13-digit prime
 *
 * SPEED (BEST for N > 1e11) 1.24ms for 12-digit prime
 *
 * SPEED (WORST for N < 1e10) 1.81ms for 10-digit prime
 *
 * SPEED (WORST for N < 1e6) 1.02ms for 6-digit prime
 *
 * SPEED (EQUAL for N < 1e3) 6.4e5ns for 3-digit prime
 */
fun Long.isPrimeMR(kRounds: List<Long> = listOf(2, 3, 5, 7, 11)): Boolean {
    if (this in 2L..3L) return true
    if (this < 2L || this % 2L == 0L) return false
    val one = BigInteger.ONE
    val n = BigInteger.valueOf(this)
    val nMinus1 = n - one
    // write n as 2^r * s + 1 by first getting r, the largest power of 2 that divides (this - 1)
    // by getting the index of the right-most one bit
    val r: Int = nMinus1.lowestSetBit
    // x * 2^y == x << y
    val s = nMinus1 / BigInteger.valueOf(2L shl r - 1)
    rounds@for (a in kRounds) {
        if (a > this - 2L) break
        // calculate a^s % n
        var x = BigInteger.valueOf(a).modPow(s, n)
        if (x == one || x == nMinus1) continue@rounds
        for (i in 0 until r) {
            x = x.modPow(BigInteger.TWO, n)
            if (x == one) break
            if (x == nMinus1) continue@rounds
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
 * The original solution kept [n] as Long but consistently underperformed compared to the current
 * solution that converts all [n] to BigInteger before reducing.
 *
 * SPEED 1.86ms VS 31.58ms for 2-element varargs with 4-digit result
 *
 * SPEED 1.33ms VS 33.15ms for 9-element varargs with 12-digit result
 *
 * @throws IllegalArgumentException if any [n] = 0.
 */
fun lcm(vararg n: Long): Long {
    require(n.all { it != 0L }) { "Parameter cannot be 0" }
    val nBI = n.map(BigInteger::valueOf)
    return nBI.reduce { acc, num ->
        (acc * num).abs() / acc.gcd(num)
    }.longValueExact()
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
 * @throws IllegalArgumentException if [n] <= 1.
 * @return map of prime factors (keys) and their exponents (values).
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
 * @throws IllegalArgumentException if arguments do not follow [m] > [n] > 0, or if both are odd,
 * or if they are not co-prime, i.e. gcd(m, n) != 1.
 */
fun pythagoreanTriplet(m: Int, n: Int, d: Int): Triple<Int, Int, Int> {
    require(n in 1 until m) { "Positive integers assumed to be m > n > 0" }
    require(!(m % 2 != 0 && n % 2 != 0)) { "Both integers cannot be odd" }
    require(gcd(m.toLong(), n.toLong()) == 1L) { "Positive integers must be co-prime" }
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