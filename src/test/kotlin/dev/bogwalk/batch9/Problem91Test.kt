package dev.bogwalk.batch9

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class RightTrianglesIntegerCoordinatesTest {
    private val tool = RightTrianglesIntegerCoordinates()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 14", "3, 33", "4, 62", "5, 101", "6, 148", "7, 207",
        // mid constraints
        "30, 4764", "50, 14234", "100, 62848", "333, 812759",
        // upper constraints
        "1000, 8318030", "1999, 35734575", "2500, 57183752"
    )
    fun `correct for all constraints`(n: Int, expected: Int) {
        assertEquals(expected, tool.countRightTriangles(n))
    }
}