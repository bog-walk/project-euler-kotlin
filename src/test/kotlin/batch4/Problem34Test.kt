package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DigitFactorialsTest {
    private val tool = DigitFactorials()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "10, 0", "20, 19", "30, 19", "40, 19", "50, 19",
        // larger values
        "100, 239", "200, 384", "500, 603"
    )
    fun testSumOfDigitFactorials_HR(n: Int, expected: Int) {
        assertEquals(expected, tool.sumOfDigitFactorialsHR(n))
    }

    @Test
    fun testSumOfDigitFactorials_PE() {
        assertEquals(40730, tool.sumOfDigitFactorialsPE())
    }
}