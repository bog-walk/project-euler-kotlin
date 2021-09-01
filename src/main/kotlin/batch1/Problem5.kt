package batch1

import util.primeFactors
import util.getPrimeNumbers
import util.lcm
import java.math.BigInteger
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Problem 5: Smallest Multiple
 *
 * https://projecteuler.net/problem=5
 *
 * Goal: Find the smallest positive number that can be evenly divided by each number
 * in the range 1..N.
 *
 * Constraints: 1 <= N <= 40
 *
 * e.g.: N = 3
 *       {1, 2, 3} evenly divides 6 to give quotient {6, 3, 2}
 */

class SmallestMultiple {

    fun lcmBrute(rangeMax: Int): Long {
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

    fun lcmUsingGCD(rangeMax: Int): Long {
        var lcm = rangeMax.toLong()
        val range = (rangeMax - 1) downTo (rangeMax / 2 + 1)
        for (i in range) {
            lcm = lcm(lcm, i.toLong())
        }
        return lcm
    }

    fun lcmUsingGCDAndReduce(rangeMax: Int): Long {
        val range: LongProgression = rangeMax.toLong() downTo (rangeMax / 2 + 1)
        return range.reduce { acc, i ->
            lcm(acc, i)
        }
    }

    /**
     * Get the prime decomposition of all range members.
     * Use the max power of all representations of every factor to
     * calculate the final lcm.
     */
    fun lcmUsingPrimeFactors(rangeMax: Int): Long {
        if (rangeMax == 1) return 1L
        val lcmPrimes = mutableMapOf<Long, Int>()
        val range = rangeMax downTo (rangeMax / 2 + 1)
        for (i in range) {
            val primes = primeFactors(i.toLong())
            for ((prime, exp) in primes.entries) {
                lcmPrimes[prime] = maxOf(
                    lcmPrimes.getOrDefault(prime, 0),
                    exp
                )
            }
        }
        var lcm = 1L
        lcmPrimes.forEach { (k, v) ->
            lcm *= ((1.0 * k).pow(v)).toLong()
        }
        return lcm
    }

    /**
     * This improves on the prime factorisation method, using the formula:
     * p[i]^a[i] = k;
     * a[i] * log(p[i]) = log(k);
     * a[i] = floor(log(k) / log(p[i]))
     * e.g. k = 6 [max target], primes < k = {2, 3, 5}
     * the exponent of the 1st prime, p[1] = 2, will be 2 as 2^2 < 6 but 2^3 > 6;
     * the exponent of the 2nd prime, p[2] = 3, will be 1 as 3^1 < 6 but 3^2 > 6;
     * the exponent of the 3rd prime, p[3] = 5, will be 2 as 5^1 < 6 but 5^2 > 6;
     * lcm = 2^2 * 3^1 * 5^1 = 60.
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

    /**
     * BigInteger class has in-built gcd() with optimum performance,
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

}