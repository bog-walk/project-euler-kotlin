package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NDigitFibonacciNumberTest {
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
        assertEquals(expected, tool.nDigitFibTermBrute(digits))
        assertEquals(expected, tool.nDigitFibTermByDigitsGolden(digits))
    }

    @Test
    fun testNthFibUsingGoldenRatio() {
        val expected = listOf(
            0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597
        )
        val actual = List(18) { n -> tool.nthFibUsingGoldenRatio(n) }
        assertEquals(expected, actual)
    }

    @Test
    fun testNDigitFibTermSlowGolden() {
        val expected = listOf(7, 12, 17, 21, 26, 31, 36, 40)
        for (n in 2..9) {
            assertEquals(expected[n - 2], tool.nDigitFibTermUsingGoldenRatio(n))
        }
    }

    @Test
    fun testGetFibTermSpeedComparison() {
        val n = 5000
        val bruteStart = System.currentTimeMillis()
        val ansBrute = tool.nDigitFibTermBrute(n)
        val bruteStop = System.currentTimeMillis()
        val goldSingleStart = System.currentTimeMillis()
        val ansGoldSingle = tool.nDigitFibTermByDigitsGolden(n)
        val goldSingleStop = System.currentTimeMillis()
        println("Brute listing took: ${bruteStop - bruteStart}\n" +
                "Gold single took: ${goldSingleStop - goldSingleStart}")
        assertEquals(ansBrute, ansGoldSingle)
    }
}