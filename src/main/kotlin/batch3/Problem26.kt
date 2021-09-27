package batch3

import util.primeNumbers
import java.math.BigInteger

/**
 * Problem 26: Reciprocal Cycles
 *
 * https://projecteuler.net/problem=26
 *
 * Goal: Find the value of the smallest d less than N for which
 * 1/d contains the longest recurring cycle in its decimal fraction part.
 *
 * Constraints: 4 <= N <= 1e4
 *
 * Unit Fraction: A fraction with 1 as the numerator.
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
     * Repetend/Reptend: the infinitely repeated digit sequence of the
     * decimal representation of a number.
     * - If a fraction contains a repetend, the latter's length (K) will never
     * be greater than the fraction's denominator minus 1.
     * - A denominator of 3 produces the first repetend, with K = 1.
     * - All fractions that are a power of 1/2 or the product of 1/5 times a
     * power of 1/2 will have exact decimal equivalents, not repetends.
     * - Multiples of a denominator will have same K value (multiples of
     * 7 are special in that both K and repetend will be equal).
     * - For each 1/p, where p is a prime number but not 2 or 5, for
     * k = 1,2,3,...n, [(10^k) - 1] / p = repetend, when there is no
     * remainder.
     * e.g. for p = 11, [(10^1) - 1] % 11 != 0, but [(10^2) - 1] / 11
     * has 99 evenly divided by 11 giving 9. Since k = 2, there must be
     * 2 repeating digits, so repetend = 09.
     */
    fun longestRepetendDenominator(n: Int): Int {
        if (n <= 7) return 3
        var denominator = 3
        var longestK = 1
        // Only primes considered as only smallest N required & anything
        // larger would be a multiple of a smaller prime with equivalent K.
        val primes = primeNumbers(n - 1) - listOf(2, 3, 5)
        primes.forEach { p ->
            val pAsBigInt = BigInteger.valueOf(1L * p)
            for (k in 1 until p) {
                val stringOfNines = BigInteger.valueOf(10L).pow(k) - BigInteger.ONE
                if (stringOfNines.mod(pAsBigInt) == BigInteger.ZERO) {
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
}