package dev.bogwalk.batch7

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SingularIntegerRightTrianglesTest {
    private val tool = SingularIntegerRightTriangles()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "12, 1", "13, 1", "15, 1", "30, 3", "41, 5", "50, 6", "99, 11",
        // mid constraints
        "800, 87", "9999, 1119", "60000, 6619", "1500000, 161667",
        // upper constraints
        "3146789, 336929", "5000000, 534136"
    )
    fun `singularTriplets correct for all constraints`(n: Int, expected: Int) {
        assertEquals(expected, tool.singularTriplets(n))
    }
}