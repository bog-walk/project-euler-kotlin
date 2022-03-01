package batch6

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class DiophantineEquationTest {
    private val tool = DiophantineEquation()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "7, 5", "13, 13", "15, 13", "23, 13", "50, 46",
        // mid  constraints
        "100, 61", "128, 109",
        // upper constraints
        "1000, 661", "3000, 2389", "10000, 9949"
    )
    fun `largestDiophantineX correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.largestDiophantineX(n))
    }
}