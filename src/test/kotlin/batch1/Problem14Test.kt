package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LongestCollatzSequenceTest {
    private val tool = LongestCollatzSequence()

    @Test
    fun testCollatz_oddStart() {
        val start = 13
        val expected = listOf(13, 40, 20, 10, 5, 16, 8, 4, 2, 1)
        assertEquals(expected, tool.collatzSequence(start))
    }

    @Test
    fun testCollatz_evenStart() {
        val start = 100
        val expected = listOf(100, 50, 25, 76, 38, 19, 58,
            29, 88, 44, 22, 11, 34, 17, 52, 26, 13, 40, 20,
            10, 5, 16, 8, 4, 2, 1)
        assertEquals(expected, tool.collatzSequence(start))
    }

    @ParameterizedTest(name="{0} has {1} steps")
    @CsvSource(
        "1, 1", "2, 2", "3, 8", "4, 3", "5, 6", "6, 9",
        "7, 17", "8, 4", "9, 20", "10, 7", "11, 15",
        "12, 10", "15, 18", "17, 13", "19, 21", "27, 112"
    )
    fun testCollatzLength(start: Int, expected: Int) {
        assertEquals(expected, tool.collatzLength(start))
    }

    @Test
    fun testLongestCollatzMemoization() {
        tool.generateLongestSequences()
        val starters = listOf(1, 2, 3, 5, 10, 15, 19, 20, 26, 100, 1000, 10000, 100000, 1000000)
        val expected = listOf(1, 2, 3, 3, 9, 9, 19, 19, 25, 97, 871, 6171, 77031, 837799)
        for ((i, starter) in starters.withIndex()) {
            assertEquals(expected[i], tool.longestCollatzMemo(starter))
        }
    }
}