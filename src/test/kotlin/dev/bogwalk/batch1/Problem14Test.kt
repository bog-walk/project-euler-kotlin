package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LongestCollatzSequenceTest {
    private val tool = LongestCollatzSequence()

    @ParameterizedTest(name="{0} has {1} steps")
    @CsvSource(
        "1, 1", "2, 2", "3, 8", "4, 3", "5, 6", "6, 9", "7, 17", "8, 4", "9, 20", "10, 7",
        "11, 15", "12, 10", "15, 18", "17, 13", "19, 21", "27, 112"
    )
    fun `collatzLength correct`(start: Int, expected: Int) {
        assertEquals(expected, tool.collatzLength(start))
    }

    @Test
    fun `longestCollatzUnderN correct using memoisation`() {
        tool.generateLongestStarters()
        val starters = listOf(
            1, 2, 3, 5, 10, 15, 19, 20, 26, 100, 1000, 10_000, 100_000, 1_000_000
        )
        val expected = listOf(
            1, 2, 3, 3, 9, 9, 19, 19, 25, 97, 871, 6171, 77031, 837_799
        )
        for ((i, starter) in starters.withIndex()) {
            assertEquals(expected[i], tool.largestCollatzStarter(starter))
        }
    }
}