package batch1

import util.gcd
import java.math.BigInteger

/**
 * Problem 1: Multiples of 3 or 5
 *
 * https://projecteuler.net/problem=1
 *
 * Goal: Find the sum of all natural numbers less than N that are
 * multiples of either of the provided factors K1 or K2.
 *
 * Constraints: 1 <= N <= 1e9, 1 <= K < N
 *
 * e.g.: N = 10, K1 = 3, K2 = 5
 *       multiples of K1 || K2 < N = {3, 5, 6, 9}
 *       sum = 23
 */

class MultiplesOf3Or5 {
    /**
     * Brute iteration through all possible values under N.
     *
     * OutOfMemoryError for N = 1e9.
     */
    fun sumOfMultiplesBruteA(number: Int, factor1: Int, factor2: Int): Long {
        return (1L until number.toLong()).filter { num ->
            num % factor1 == 0L || num % factor2 == 0L
        }.sum()
    }

    /**
     * Multiplies all factors in a loop until multiple exceeds N.
     *
     * OutOfMemoryError for N = 1e9.
     */
    fun sumOfMultiplesBruteB(number: Int, factor1: Int, factor2: Int): Long {
        var count = 1
        val multiples = mutableSetOf<Long>()
        var factors = listOf(factor1, factor2)
        while (factors.isNotEmpty()) {
            factors = factors.filter {
                it * count < number
            }.also { filtered ->
                multiples.addAll(filtered.map { it.toLong() * count })
            }
            count++
        }
        return multiples.sum()
    }

    /**
     * As only 2 factors are provided, find the sum of the arithmetic progressions
     * of each factor then subtract the sum of the arithmetic progression of the
     * factors' lowest common multiple (to remove duplicates).
     */
    fun sumOfMultiples(number: Int, factor1: Int, factor2: Int): BigInteger {
        val max = number - 1
        val lcm = (factor1 * factor2) / gcd(factor1, factor2)
        return sumOfArithProgression(max, factor1)
            .plus(sumOfArithProgression(max, factor2))
            .minus(sumOfArithProgression(max, lcm))
    }

    /**
     * Sum of arithmetic progression uses formula:
     * 3+6+9+12+...999 = 3*(1+2+3+4+...333) = f*(0.5*d*(d+1)),
     * with f being the factor (delta in sequence) & d being the number
     * of terms in sequence (final sequence element / factor).
     */
    private fun sumOfArithProgression(number: Int, factor: Int): BigInteger {
        val terms = (number / factor).toBigInteger()
        val parentheses = terms * (terms + BigInteger.ONE) / BigInteger.TWO
        return parentheses.times(factor.toBigInteger())
    }
}

fun main() {
    val tool = MultiplesOf3Or5()
    val ans = tool.sumOfMultiplesBruteB(1_000_000_000,3, 5)
    println(ans)
}