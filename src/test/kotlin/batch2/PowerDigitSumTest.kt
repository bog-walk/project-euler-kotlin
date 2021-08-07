package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PowerDigitSumTest {
    @ParameterizedTest(name="2^{0} -> {1}")
    @CsvSource(
        "1, 2", "2, 4", "3, 8", "4, 7", "7, 11",
        "15, 26", "99, ", "1000, ", "10000, "
    )
    fun testExpDigSum(n: Int, expected: Int) {
        val tool = PowerDigitSum()
        assertEquals(expected, tool.expDigSum(n))
    }
}