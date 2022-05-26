package batch0

import util.maths.lcm
import util.maths.primeNumbersOG
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Problem 5: Smallest Multiple
 *
 * https://projecteuler.net/problem=5
 *
 * Goal: Find the smallest positive number that can be evenly divided by each number in the range
 * [1, N].
 *
 * Constraints: 1 <= N <= 40
 *
 * e.g.: N = 3
 *       {1, 2, 3} evenly divides 6 to give quotient {6, 3, 2}
 */

class SmallestMultiple {
    /**
     * The solution below needs to be removed as top-level function now uses BigInteger.
     *
     * Uses top-level helper to repeatedly calculate the lcm of 2 values (via reduce()), starting
     * from the largest and stepping backwards until the middle of the range, as the smaller half
     * of a range will already be factors of the larger half.
     *
     * SPEED (WORST) 61.10ms for N = 40
     */
    fun lcmOfRange(n: Int): Long {
        val rangeMax = n.toLong()
        val range = (rangeMax downTo (rangeMax / 2 + 1)).toList().toLongArray()
        return lcm(*range)
    }

    /**
     * Solution mimics the one above but uses BigInteger and its built-in methods instead of Long.
     *
     * The BigInteger class has built-in gcd() with optimum performance, as it uses the Euclidean
     * algorithm initially along with MutableBigInteger instances to avoid frequent memory
     * allocations, then switches to binary gcd algorithm at smaller values to increase speed.
     *
     * SPEED (BEST) 6.32ms for N = 40
     */
    fun lcmOfRangeBI(n: Int): Long {
        var lcm = n.toBigInteger()
        val range: IntProgression = (n - 1) downTo (n / 2 + 1)
        for (i in range) {
            val y = i.toBigInteger()
            val gcd = lcm.gcd(y)
            val numerator = lcm * y
            lcm = numerator / gcd
        }
        return lcm.toLong()
    }

    /**
     * Uses prime numbers to calculate the lcm of a range, based on the formula:
     *
     *      p_i^a_i = n
     *
     *      a_i * log(p_i) = log(n)
     *
     *      a_i = floor(log(n) / log(p_i))
     *
     *      e.g. N = 6, primes < N = {2, 3, 5};
     *      the exponent of the 1st prime will be 2 as 2^2 < 6 but 2^3 > 6;
     *      the exponent of the 2nd prime will be 1 as 3^1 < 6 but 3^2 > 6;
     *      the exponent of the 3rd prime will be 1 as 5^1 < 6 but 5^2 > 6;
     *      therefore, lcm = 2^2 * 3^1 * 5^1 = 60.
     *
     * SPEED (BETTER) 43.95ms for N = 40
     */
    fun lcmOfRangeUsingPrimes(n: Int): Long {
        var lcm = 1L
        val limit = sqrt(n.toDouble())
        val primes = primeNumbersOG(n)
        for (prime in primes) {
            val power = if (prime <= limit) {
                (log2(n.toDouble()) / log2(prime.toDouble())).toInt()
            } else 1
            lcm *= (prime.toDouble().pow(power)).toLong()
        }
        return lcm
    }
}