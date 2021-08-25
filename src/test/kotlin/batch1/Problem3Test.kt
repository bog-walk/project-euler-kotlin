package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestPrimeFactorTest {
    @Test
    fun testGetPrimeFactorsBrute() {
        val nums = listOf<Long>(10, 12, 17, 48, 147, 330, 13195, 1000000)
        val expected = listOf<List<Long>>(
            listOf(2, 5), listOf(2, 2, 3), listOf(17), listOf(2, 2, 2, 2, 3),
            listOf(3, 7, 7), listOf(2, 3, 5, 11), listOf(5, 7, 13, 29),
            listOf(2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5)
        )
        val tool = LargestPrimeFactor()
        nums.forEachIndexed { index, num ->
            assertEquals(expected[index], tool.getPrimeFactorsBrute(num))
        }
    }

    @Test
    fun testGetPrimeFactorsRecursive() {
        val nums = listOf(
            2, 3, 12, 17, 48, 147, 330, 13195, 200000, 600851475143
        )
        val expected = listOf<List<Long>>(
            listOf(2), listOf(3), listOf(2, 2, 3), listOf(17), listOf(2, 2, 2, 2, 3),
            listOf(3, 7, 7), listOf(2, 3, 5, 11), listOf(5, 7, 13, 29),
            listOf(2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5),
            listOf(71, 839, 1471, 6857)
        )
        val tool = LargestPrimeFactor()
        nums.forEachIndexed { index, num ->
            assertEquals(expected[index], tool.getPrimeFactorsRecursive(num))
        }
    }

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        // lower constraint
        "10, 5",
        // normal values
        "17, 17", "48, 3", "147, 7", "330, 11",
        // larger values
        "13195, 29", "200000, 5", "600851475143, 6857",
        // large prime numbers, including Euler's
        "7919, 7919", "2147483647, 2147483647", "67280421310721, 67280421310721"
    )
    fun testAlgorithm_largestPrime(n: Long, expected: Long) {
        val tool = LargestPrimeFactor()
        // Recursive solution
        val primesRecursive = tool.getPrimeFactorsRecursive(n)
        assertEquals(expected, tool.largestPrime(primesRecursive))
        // Exponent solution
        assertEquals(expected, tool.getLargestPrimeExponent(n))
    }

    @Test
    fun testLargestPrimeUpperConstraints() {
        val upperN = listOf(9007199254740991, 10000000000000000)
        val expected = listOf(20394401, 5)
        val tool = LargestPrimeFactor()
        upperN.forEachIndexed { index, n ->
            // Recursive solution
            val primesRecursive = tool.getPrimeFactorsRecursive(n)
            assertEquals(expected[index], tool.largestPrime(primesRecursive))
            // Exponent solution
            assertEquals(expected[index], tool.getLargestPrimeExponent(n))
        }
    }
}