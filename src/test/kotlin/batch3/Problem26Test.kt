package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class ReciprocalCyclesTest {
    private val tool = ReciprocalCycles()

    @ParameterizedTest(name="Longest <{0} = {1}")
    @CsvSource(
        // lower constraints
        "5, 3", "10, 7", "14, 7", "19, 17", "25, 23",
        // normal values
        "46, 29", "50, 47", "70, 61",
        // upper constraints
        "1000, 983"
    )
    fun testLongestReciprocalCycle(n: Int, expected: Int) {
        assertEquals(expected, tool.longestRepetendDenominatorUsingPrimes(n))
        assertEquals(expected, tool.longestRepetendDenominator(n))
    }

    @Test
    fun testLongestRepetend_speedComparison() {
        val n = 10000
        val startNoPrime = System.currentTimeMillis()
        val ansNoPrime = tool.longestRepetendDenominator(n)
        val stopNoPrime = System.currentTimeMillis()
        val startPrime = System.currentTimeMillis()
        val ansPrime = tool.longestRepetendDenominatorUsingPrimes(n)
        val stopPrime = System.currentTimeMillis()
        println("Using primes took: ${stopPrime - startPrime}ms" +
                "\nNot using primes took: ${stopNoPrime - startNoPrime}ms")
        assertEquals(ansNoPrime, ansPrime)
    }
}