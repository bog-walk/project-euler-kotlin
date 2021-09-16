package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class NDigitFibonacciNumberTest {
    private val tool = NDigitFibonacciNumber()

    @ParameterizedTest(name="1st {0} digits = F({1})")
    @CsvSource(
        // lower constraints
        "2, 7", "3, 12", "4, 17",
        // normal values
        "9, 40", "1000, 4782",
        // upper constraints
        "5000, 23922"
    )
    fun testNDigitFibTerm(digits: Int, expected: Int) {
        assertEquals(expected, tool.nDigitFibTerm(digits))
    }

    @Test
    fun testFibTermByDigits() {
        val expected = listOf(7, 12, 17, 21, 26, 31, 36, 40)
        assertEquals(expected, tool.fibTermByDigits(9))
    }
}