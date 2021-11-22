package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PowerDigitSumTest {
    private val tool = PowerDigitSum()

    @ParameterizedTest(name="2^{0} -> {1}")
    @CsvSource(
        // lower constraint
        "1, 2", "2, 4", "3, 8",
        "4, 7", "7, 11", "9, 8", "15, 26", "99, 107",
        // higher constraint
        "1000, 1366", "10000, 13561"
    )
    fun testExpDigSum(n: Int, expected: Int) {
        assertEquals(expected, tool.expDigSumUsingString(n))
        assertEquals(expected, tool.expDigSumUsingMath(n))
    }
}