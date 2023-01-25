package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertNull

internal class ArrangedProbabilityTest {
    private val tool = ArrangedProbability()

    @ParameterizedTest(name = "D = {0}, P/Q = {1}/{2}")
    @CsvSource(
        // lower constraints
        "2, 1, 2, 3, 4", "5, 1, 2, 15, 21", "100, 1, 2, 85, 120", "450, 1, 2, 493, 697",
        "500, 3, 8, 931, 1520", "100, 5, 9, 141, 189",
        // mid constraints
        "1000, 3, 8, 931, 1520", "5000, 3, 8, 3871, 6321", "5000, 5, 9, 6601, 8856"
    )
    fun `getNextArrangement() correct for all constraints`(
        d: Long, p: Int, q: Int, blue: String, total: String
    ) {
        val expected = blue to total
        assertEquals(expected, tool.getNextArrangement(d, p, q))
        if (p == 1 && q == 2) {
            assertEquals(expected, tool.getNextHalfArrangement(d))
        }
    }

    @Test
    fun `getNextArrangement() returns null when no solution possible`() {
        val inputs = listOf(
            Triple(100L, 1, 4)
        )

        for ((d, p, q) in inputs) {
            assertNull(tool.getNextArrangement(d, p, q))
        }
    }
}