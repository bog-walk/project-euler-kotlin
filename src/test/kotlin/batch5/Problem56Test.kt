package batch5

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PowerfulDigitSumTest {
    private val tool = PowerfulDigitSum()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "5, 13", "6, 13", "7, 27", "8, 36", "9, 37", "10, 45",
        // mid constraints
        "20, 127", "30, 224", "50, 406", "100, 972",
        // upper constraints
        "200, 2205"
    )
    fun `maxDigitSum correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.maxDigitSum(n))
    }
}