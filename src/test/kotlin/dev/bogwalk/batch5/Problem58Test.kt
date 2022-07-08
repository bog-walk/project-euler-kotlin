package dev.bogwalk.batch5

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SpiralPrimesTest {
    private val tool = SpiralPrimes()

    @ParameterizedTest(name="< {0}%")
    @CsvSource(
        // lower constraints
        "8, 238733", "9, 74373", "10, 26241", "11, 12201", "12, 6523", "15, 981",
        // mid constraints
        "20, 309", "25, 99", "30, 49", "40, 31",
        // upper constraints
        "50, 11", "55, 9", "60, 5"
    )
    fun `spiralPrimeRatio correct`(percent: Int, expected: Int) {
        assertEquals(expected, tool.spiralPrimeRatio(percent))
    }
}