package dev.bogwalk.batch6

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ConvergentsOfETest {
    private val tool = ConvergentsOfE()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "1, 2", "2, 3", "3, 8", "4, 2", "5, 10", "6, 15", "7, 7", "8, 13", "9, 13", "10, 17",
        // mid constraints
        "100, 272", "200, 597", "300, 980", "400, 1397", "500, 1849",
        // upper constraints
        "4999, 25652", "9999, 55534", "30000, 187838"
    )
    fun `nthConvergentDigitSum correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.nthConvergentDigitSum(n))
    }
}