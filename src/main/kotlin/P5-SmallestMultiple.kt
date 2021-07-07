import util.getPrimeFactors
import util.getPrimeNumbers
import java.math.BigInteger
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Problem 5: Smallest Multiple
 * Goal: Find the smallest positive number that can be divided by each number
 * in the range 1..N, with 1 <= N <= 40.
 * e.g. The smallest number that can be evenly divided by every number from 1 to 10
 * is 2520.
 */

class SmallestMultiple {

    fun lcmIterative(rangeMax: Int): Long {
        var lcm = rangeMax.toLong()
        var step = rangeMax.toLong()
        val range = (rangeMax - 1) downTo (rangeMax / 2 + 1)
        for (i in range) {
            while (lcm % i != 0L) {
                lcm += step
            }
            step = lcm
        }
        return lcm
    }

    /**
     * Get lcm from prime decomposition of all range members.
     * Use the max power of all representations of every factor.
     */
    fun lcmPrimeFactors(rangeMax: Int): Long {
        val primeFactors = mutableMapOf<Long, Int>()
        val range = rangeMax downTo (rangeMax / 2 + 1)
        for (i in range) {
            val primes = getPrimeFactors(i.toLong())
            for ((prime, freq) in primes.entries) {
                primeFactors[prime] = maxOf(
                    primeFactors.getOrDefault(prime, 0),
                    freq
                )
            }
        }
        var lcm = 1.0
        primeFactors.forEach { (k, v) ->
            lcm *= (1.0 * k).pow(v)
        }
        return lcm.toLong()
    }

    /**
     * gcd(x, y) = gcd(|x * y|, |x|); where |x| >= |y|
     * &
     * gcd(x, 0) = gcd(0, x) = |x|
     */
    private fun gcdEuclidean(x: Long, y: Long): Long {
        if (x == 0L || y == 0L) return x + y
        val bigger = maxOf(x, y)
        val smaller = minOf(x, y)
        return gcdEuclidean(bigger % smaller, smaller)
    }

    /**
     * lcm(x, y) = |x * y| / gcd(x, y)
     */
    private fun lcmTwoNumbers(x: Long, y: Long): Long {
        return x * y / gcdEuclidean(x, y)
    }

    fun lcmUsingGCD(rangeMax: Int): Long {
        var lcm = rangeMax.toLong()
        val range: LongProgression = (rangeMax - 1).toLong() downTo (rangeMax / 2 + 1)
        for (i in range) {
            lcm = lcmTwoNumbers(lcm, i)
        }
        return lcm
    }

    /**
     * BigInteger class has in-built gcd function with optimum performance,
     * as it uses Euclidean algorithm initially along with MutableBigInteger
     * instances to avoid frequent memory allocations, then switches to binary
     * gcd algorithm at smaller values to increase speed.
     */
    fun lcmUsingBigInteger(rangeMax: Int): BigInteger {
        var lcm = rangeMax.toBigInteger()
        val range: IntProgression = (rangeMax - 1) downTo (rangeMax / 2 + 1)
        for (i in range) {
            val y = i.toBigInteger()
            val gcd = lcm.gcd(y)
            val numerator = lcm.multiply(y)
            lcm = numerator.divide(gcd)
        }
        return lcm
    }

    /**
     * An improved form of the prime factorisation method. e.g. when max = 6,
     * the exponent of p[1] = 2 will be 2 as 2^3 > 6, p[2] = 3 will be 1, &
     * p[3] = 5 will be 1, so 2^2 * 3^1 * 5^1 = 60. Based on formula ->
     * p[i]^a[i] = k
     * a[i] * log(p[i]) = log(k)
     * a[i] = floor(log(k) / log(p[i]))
     */
    fun lcmUsingPrimes(rangeMax: Int): Long {
        var lcm = 1L
        val limit = sqrt(rangeMax.toDouble())
        val primes = getPrimeNumbers(rangeMax)
        for (prime in primes) {
            val power = if (prime <= limit) {
                floor(log2(rangeMax.toDouble()) / log2(prime.toDouble()))
            } else 1.0
            lcm *= ((prime.toDouble()).pow(power)).toLong()
        }
        return lcm
    }

}