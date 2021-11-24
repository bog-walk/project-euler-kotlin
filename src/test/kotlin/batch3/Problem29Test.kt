package batch3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test

internal class DistinctPowersTest {
    private val tool = DistinctPowers()

    @ParameterizedTest(name="N={0} -> {1}")
    @CsvSource(
        // lower constraints
        "2, 1", "3, 4", "4, 8", "5, 15",
        // normal values
        "10, 69", "20, 324", "50, 2184", "100, 9183"
    )
    fun testDistinctPowers(n: Int, expected: Int) {
        assertEquals(expected, tool.distinctPowersBrute(n))
        assertEquals(expected, tool.distinctPowers(n))
    }

    @Test
    fun testDistinctPowers_speedComparison() {
        val n = 500
        val ansBrute: Int
        val ansImproved: Int
        val timeBrute = measureNanoTime {
            ansBrute = tool.distinctPowersBrute(n)
        }
        val timeImproved = measureNanoTime {
            ansImproved = tool.distinctPowers(n)
        }
        println("Brute solution took: ${timeBrute / 1_000_000}ms\n" +
                "Improved solution took: ${timeImproved}ns")
        assertEquals(ansBrute, ansImproved)
    }
}