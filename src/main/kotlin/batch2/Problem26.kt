package batch2

import util.maths.primeNumbersOG
import java.math.BigInteger

/**
 * Problem 26: Reciprocal Cycles
 *
 * https://projecteuler.net/problem=26
 *
 * Goal: Find the value of the smallest d less than N for which 1/d contains the longest
 * recurring cycle in its decimal fraction part.
 *
 * Constraints: 4 <= N <= 1e4
 *
 * Repetend/Reptend: The infinitely repeated digit sequence of the decimal representation of a
 * number.
 *
 * e.g.: N = 10
 *       1/2 = 0.5
 *       1/3 = 0.(3) -> 1-digit recurring cycle
 *       1/4 = 0.25
 *       1/5 = 0.2
 *       1/6 = 0.1(6) -> 1-digit recurring cycle
 *       1/7 = 0.(142857) -> 6-digit recurring cycle
 *       1/8 = 0.125
 *       1/9 = 0.(1) -> 1-digit recurring cycle
 *       result = 7
 */

class ReciprocalCycles {
    /**
     * Solution based on the following:
     *
     *  - If a fraction contains a repetend, the latter's length (K) will never be greater than
     * the fraction's denominator minus 1.
     *
     *  - A denominator of 3 produces the first repetend, with K = 1.
     *
     *  - All fractions that are a power of 1/2 or the product of 1/5 times a power of 1/2 will
     *  have exact decimal equivalents, not repetends.
     *
     *  - Multiples of a denominator will have same K value (multiples of 7 are special in that
     *  both K and repetend will be equal).
     *
     *  - For each 1/p, where p is a prime number but not 2 or 5, for k in [1, p),
     *  10^k % p produces a repetend, when the remainder is 1.
     *
     *      - e.g. p = 11 -> (10^1 - 1) % 11 != 0, but (10^2 - 1) / 11 has 99 evenly divided by 11
     *      giving 9. Since k = 2, there must be 2 repeating digits, so repetend = 09.
     *
     * SPEED (WORST) 4.11s for N = 1e4
     */
    fun longestRepetendDenomUsingPrimes(n: Int): Int {
        // only primes considered as only the smallest N is required & anything larger would be
        // a multiple of a smaller prime with equivalent K
        val primes = primeNumbersOG(n - 1) - listOf(2, 3, 5)
        var denominator = 3
        var longestK = 1
        val one = BigInteger.ONE
        val ten = BigInteger.TEN
        for (p in primes) {
            for (k in 1 until p) {
                if (ten.modPow(k.toBigInteger(), p.toBigInteger()) == one) {
                    if (k > longestK) {
                        longestK = k
                        denominator = p
                    }
                    break
                }
            }
        }
        return denominator
    }

    /**
     * Solution using primes above is optimised based on the following:
     *
     *  - Full Repetend Primes are primes that, as 1/p, will have the longest repetend of
     *  k = p - 1. A prime qualifies if, for k in [1, p-1], only the last k returns True for
     *  10^k % p = 1.
     *
     *      - e.g. p = 7 -> for k in [1, 7), 10^k % p = [3, 2, 6, 4, 5, 1], so 7 is a full
     *      repetend prime.
     *
     *  - Other than N = 3 and N = 6 both having K = 1, repetend length increases as primes
     *  increase since the longest repetends will be produced by full repetend primes & not be
     *  repeated. So the loop can be started from the largest prime and broken once the first
     *  full repetend prime is found.
     *
     * SPEED (BETTER) 26.17ms for N = 1e4
     */
    fun longestRepetendDenomUsingPrimesImproved(n: Int): Int {
        if (n < 8) return 3
        val primes = primeNumbersOG(n - 1).reversed()
        var denominator = 3
        val one = BigInteger.ONE
        val ten = BigInteger.TEN
        for (p in primes) {
            var k = 1
            while (ten.modPow(k.toBigInteger(), p.toBigInteger()) != one) {
                k++
            }
            if (k == p - 1) {
                denominator = p
                break
            }
        }
        return denominator
    }

    /**
     * Repeatedly divides & stores decimal parts until a decimal part is repeated & compares
     * length of stored parts.
     *
     * SPEED (BEST) 1.38ms for N = 1e4
     */
    fun longestRepetendDenominator(n: Int): Int {
        var denominator = n
        var longestK = 0
        val upperN = if (n % 2 == 0) n - 1 else n - 2
        val lowerN = upperN / 2
        for (i in upperN downTo lowerN step 2) {
            if (longestK >= i) break
            val remainders = IntArray(i)
            var remainder = 1
            var position = 0
            while (remainders[remainder] == 0 && remainder != 0) {
                remainders[remainder] = position++
                remainder = remainder * 10 % i
            }
            if (position - remainders[remainder] >= longestK) {
                longestK = position - remainders[remainder]
                denominator = i
            }
        }
        return denominator
    }
}