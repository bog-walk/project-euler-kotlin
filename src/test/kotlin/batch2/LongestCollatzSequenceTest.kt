package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LongestCollatzSequenceTest {
    @Test
    fun testCollatz_oddStart() {
        val tool = LongestCollatzSequence()
        val start = 13
        val expected = listOf(13, 40, 20, 10, 5, 16, 8, 4, 2, 1)
        assertEquals(expected, tool.collatzSequence(start))
    }

    @Test
    fun testCollatz_evenStart() {
        val tool = LongestCollatzSequence()
        val start = 100
        val expected = listOf(100, 50, 25, 76, 38, 19, 58,
            29, 88, 44, 22, 11, 34, 17, 52, 26, 13, 40, 20,
            10, 5, 16, 8, 4, 2, 1)
        assertEquals(expected, tool.collatzSequence(start))
    }

    @ParameterizedTest(name="Longest under {0} starts at {1}")
    @CsvSource(
        "2, 1", "3, 2", "5, 3", "10, 9", "15, 9", "26, 25"
    )
    fun testLongestCollatz(max: Int, expected: Int) {
        val tool = LongestCollatzSequence()
        assertEquals(expected, tool.longestCollatz(max))
    }
}