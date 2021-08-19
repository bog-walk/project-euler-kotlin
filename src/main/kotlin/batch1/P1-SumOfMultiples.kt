package batch1

import java.math.BigInteger

/**
 * Problem 1: Multiples of 3 or 5
 *
 * https://projecteuler.net/problem=1
 *
 * * Goal: Find the sum of all natural numbers less than N that are
 * multiples of any of the provided factors Ki, with 1 <= N <= 1e9 and
 * 1 <= i <= 5 and 1 <= K <= N.
 *
 * e.g.: Sum of all multiples of 3 or 5 below 10 (i.e. 3, 5, 6, 9) = 23.
 */

class SumOfMultiples {
    // OutOfMemoryError for N == 1e9
    fun sumOfMultiples(number: Int, vararg factors: Int): Int {
        return (1 until number).filter { num ->
            factors.any { factor ->
                num % factor == 0
            }
        }.sum()
    }

    // OutOfMemoryError & Time out for N == 1e9
    fun sumOfMultiplesVersionB(number: Int, vararg factors: Int): Int {
        var sum = 0
        outer@for (n in 1 until number) {
            for (factor in factors) {
                if (n % factor == 0) {
                    sum += n
                    continue@outer
                }
            }
        }
        return sum
    }

    // OutOfMemoryError & Time out for N == 1e9
    fun sumOfMultiplesVersionC(number: Int, vararg factors: Int): Int {
        var count = 1
        val multiples = mutableSetOf<Int>()
        var factorsList = factors.toList()
        while (factorsList.isNotEmpty()) {
            factorsList = factorsList.filter {
                it * count < number
            }.also { filtered ->
                multiples.addAll(filtered.map { it * count })
            }
            count++
        }
        return multiples.sum()
    }

    fun sumOfMultiplesFinal(number: Int, vararg factors: Int): BigInteger {
        val factorsList = getAllFactors(factors)
        val sums: List<BigInteger> = factorsList.map { list ->
            list.fold(BigInteger.ZERO) { acc, factor ->
                acc + sumOfModulus(number - 1, factor)
            }
        }
        return sums[0] - sums[1]
    }

    // 3+6+9+12+...999 = 3*(1+2+3+4+...333) == f*(0.5*d*(d+1))
    private fun sumOfModulus(number: Int, factor: Int): BigInteger {
        // final multiple divided = d
        val final = (number / factor).toBigInteger()
        val parentheses = final * (final + BigInteger.ONE) / BigInteger.TWO
        return parentheses.times(factor.toBigInteger())
    }

    // e.g. multiples of 3 & 5 would overlap with multiples of 15
    private fun getAllFactors(factors: IntArray): List<List<Int>> {
        val factorsDistinct = factors.distinct()
        val common = mutableSetOf<Int>()
        for (i in 0 until factorsDistinct.lastIndex) {
            val x = factorsDistinct[i]
            for (j in (i + 1)..factorsDistinct.lastIndex) {
                common.add(x * factorsDistinct[j])
            }
        }
        return listOf(
            factorsDistinct,
            (common - factorsDistinct).sorted()
        )
    }
}