package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReciprocalCyclesTest {
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
        val ansNoPrime: Int
        val ansPrime: Int
        val timeNoPrime = measureNanoTime {
            ansNoPrime = tool.longestRepetendDenominator(n)
        }
        val timePrime = measureNanoTime {
            ansPrime = tool.longestRepetendDenominatorUsingPrimes(n)
        }
        println("Not using primes took: ${timeNoPrime / 1_000_000}ms\n" +
                "Using primes took: ${timePrime / 1_000_000}ms")
        assertEquals(ansNoPrime, ansPrime)
    }
}