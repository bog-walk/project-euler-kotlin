package dev.bogwalk.batch8

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CountingRectanglesTest {
    private val tool = CountingRectangles()

    @ParameterizedTest(name="R = {0}")
    @CsvSource(
        // lower constraints
        "2, 2", "11, 4", "18, 6", "20, 6", "25, 7", "33, 9", "49, 10", "66, 11",
        // mid constraints
        "100, 16", "333, 30", "1000, 56",
        // upper constraints
        "12000, 178", "1000000, 1632"
    )
    fun `HR problem correct for all constraints`(target: Int, expected: Int) {
        assertEquals(expected, tool.findClosestContainingArea(target))
    }

    @Test
    fun `PE problem correct`() {
        val target = 2_000_000
        val expected = 2772
        assertEquals(expected, tool.findClosestContainingArea(target))
    }
}