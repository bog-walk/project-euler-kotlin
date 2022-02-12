package batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
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
        "1000, 983", "5000, 4967"
    )
    fun `longestRepetendDenom correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.longestRepetendDenomUsingPrimes(n))
        assertEquals(expected, tool.longestRepetendDenomUsingPrimesImproved(n))
        assertEquals(expected, tool.longestRepetendDenominator(n))
    }

    @Test
    fun `longestRepetendDenom speed`() {
        val n = 10_000
        val expected = 9967
        val solutions = mapOf(
            "Original prime" to tool::longestRepetendDenomUsingPrimes,
            "Improved prime" to tool::longestRepetendDenomUsingPrimesImproved,
            "Non-prime" to tool::longestRepetendDenominator
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}