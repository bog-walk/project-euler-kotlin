package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class FactorialDigitSumTest {
    @ParameterizedTest(name="{0}! = {1}")
    @CsvSource(
        // lower constraints
        "0, 1", "1, 1", "2, 2", "3, 6",
        // smaller N
        "4, 6", "5, 3", "6, 9", "10, 27",
        // larger N
        "100, 648", "333, 2862", "750, 7416",
        // upper constraints
        "946, 9675", "1000, 10539"
    )
    fun testFactorialDigitSum(n: Int, expected: Int) {
        val tool = FactorialDigitSum()
        assertEquals(expected, tool.factorialDigitSum(n))
    }
}