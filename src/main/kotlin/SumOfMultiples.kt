import java.math.BigInteger

/**
 * Problem 1: Multiples of 3 & 5
 * Goal: Find sum of all natural numbers less than N that are
 * multiples of any of the provided factors, with 1 <= N <= 1e9.
 * e.g. Sum of all multiples of 3 or 5 below 10 (i.e. 3, 5, 6, 9) = 23.
 */
// OutOfMemoryError for N == 1e9
fun Int.sumOfMultiples(vararg factors: Int): Int {
    return (1 until this).filter { num ->
        factors.any { factor ->
            num % factor == 0
        }
    }.sum()
}

class SumOfMultiples {

    // Overflow error for N == 1e9 or Time out
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

    fun sumOfMultiplesVersionD(number: Int, vararg factors: Int): BigInteger {
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