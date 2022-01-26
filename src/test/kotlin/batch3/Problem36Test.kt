package batch3

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals

internal class DoubleBasePalindromesTest {
    private val tool = DoubleBasePalindromes()

    @ParameterizedTest(name="N={0} K={1}")
    @CsvSource(
        // lower constraints
        "10, 2, 25", "10, 4, 11", "10, 6, 22", "10, 8, 37",
        // normal values
        "1000, 3, 2638", "1000, 5, 1940", "1000, 7, 855",
        // upper constraint
        "1000000, 2, 872187", "1000000, 9, 782868"
    )
    fun testSumOfPalindromes(n: Int, k: Int, expected: Int) {
        assertEquals(expected, tool.sumOfPalindromesBrute(n, k))
        assertEquals(expected, tool.sumOfPalindromes(n, k))
    }

    @Test
    fun testSumOfPalindromes_speedComparison() {
        val n = 1_000_000_000
        val k = 2
        val ansBrute: Int
        val ansImproved: Int
        val timeBrute = measureNanoTime {
            ansBrute = tool.sumOfPalindromesBrute(n, k)
        }
        val timeImproved = measureNanoTime {
            ansImproved = tool.sumOfPalindromes(n, k)
        }
        println("Normal solution took: ${1.0 * timeBrute / 1_000_000_000}s\n" +
                "Improved solution took: ${1.0 * timeImproved / 1_000_000_000}s")
        assertEquals(ansBrute, ansImproved)
    }
}