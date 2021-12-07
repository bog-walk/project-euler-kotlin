package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertContentEquals
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
    fun testGetFibTermSpeedComparison_lowN() {
        val n = 10
        val expected = 45
        val solutions = listOf(
            tool::nDigitFibTermBrute,
            tool::nDigitFibTermByDigitsGolden,
            tool::nDigitFibTermUsingGoldenRatio
        )
        val times = mutableListOf<Long>()
        solutions.forEachIndexed { i, solution ->
            val time = measureNanoTime {
                assertEquals(expected, solution(n))
            }
            times.add(i, time)
        }
        println("Brute solution took: ${times[0]}ns\n" +
                "Gold digits took: ${times[1]}ns\n" +
                "Gold ratio took: ${times[2]}ns")
    }

    @Test
    fun testGetFibTermSpeedComparison_highN() {
        val n = 5000
        val ansBrute: Int
        val ansGoldDigits: Int
        val timeBrute = measureNanoTime {
            ansBrute = tool.nDigitFibTermBrute(n)
        }
        val timeGoldDigits = measureNanoTime {
            ansGoldDigits = tool.nDigitFibTermByDigitsGolden(n)
        }
        println("Brute solution took: ${timeBrute / 1_000_000}ms\n" +
                "Gold digits took: ${timeGoldDigits}ns")
        assertEquals(ansBrute, ansGoldDigits)
    }
}