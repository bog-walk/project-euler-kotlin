package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class LargestPrimeFactorTest {
    private val tool = LargestPrimeFactor()

    @ParameterizedTest(name="N={0} = {1}")
    @CsvSource(
        // lower constraints
        "10, 5",
        // N is small prime
        "17, 17", "23, 23",
        // mid constraints
        "48, 3", "147, 7", "330, 11",
        // larger mid constraints
        "13195, 29", "200_000, 5", "600851475143, 6857",
        // large prime numbers, including Euler's
        "7919, 7919", "2_147_483_647, 2_147_483_647"
    )
    fun `largestPrimeFactor correct`(n: Long, expected: Long) {
        assertEquals(expected, tool.largestPrimeFactor(n))
        assertEquals(expected, tool.largestPrimeFactorRecursive(n))
    }

    @Test
    fun `speed when N has large factors`() {
        val n = 600_851_475_143
        val expected = 6857L
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::largestPrimeFactor, n).run {
            speeds.add("Decomposition" to second)
            assertEquals(expected, first, "Incorrect Decomposition -> $first")
        }
        getSpeed(tool::largestPrimeFactorRecursive, n).run {
            speeds.add("Recursive" to second)
            assertEquals(expected, first, "Incorrect Recursive -> $first")
        }
        compareSpeed(speeds)
    }

    @Test
    fun `speed when N has small factors`() {
        val n = 1e12.toLong()
        val expected = 5L
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::largestPrimeFactor, n).run {
            speeds.add("Decomposition" to second)
            assertEquals(expected, first, "Incorrect Decomposition -> $first")
        }
        getSpeed(tool::largestPrimeFactorRecursive, n).run {
            speeds.add("Recursive" to second)
            assertEquals(expected, first, "Incorrect Recursive -> $first")
        }
        compareSpeed(speeds)
    }
}