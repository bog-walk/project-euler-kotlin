package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

internal class ArithmeticExpressionsTest {
    private val tool = ArithmeticExpressions()

    @Test
    fun `HR solution returns 1 or 0 for single digit sets`() {
        for (d in 0..9) {
            val input = listOf(d)
            val expected = if (d == 1) 1 else 0

            assertEquals(expected, tool.highestStreak(input))
            assertEquals(expected, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `HR solution returns 0 for sets with no streaks`() {
        val inputs = listOf(listOf(2, 8), listOf(1, 3), listOf(1, 4))

        for (input in inputs) {
            assertEquals(0, tool.highestStreak(input))
            assertEquals(0, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `HR solution correct for M = 2`() {
        val input = listOf(1, 2)
        val expected = 3

        assertEquals(expected, tool.highestStreak(input))
        assertEquals(expected, tool.highestStreakWithFractions(input))
    }

    @Test
    fun `HR solution correct for M = 3`() {
        val inputs = listOf(listOf(1, 2, 3), listOf(1, 2, 4), listOf(2, 5, 9))
        val expected = listOf(9, 10, 3)

        for ((input, e) in inputs.zip(expected)) {
            assertEquals(e, tool.highestStreak(input))
            assertEquals(e, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `HR solution correct for M = 4`() {
        val inputs = listOf(listOf(1, 2, 3, 4))
        val expected = listOf(28)

        for ((input, e) in inputs.zip(expected)) {
            assertEquals(e, tool.highestStreak(input))
            assertEquals(e, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `HR solution handles floating-point values correctly`() {
        val inputs = listOf(listOf(1, 5, 6, 7), listOf(1, 3, 4, 5), listOf(1, 3, 4, 7))
        val expected = listOf(15, 24, 10)

        for ((input, e) in inputs.zip(expected)) {
            assertEquals(e, tool.highestStreak(input))
            assertEquals(e, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `HR solution correct for M = 5`() {
        val inputs = listOf(listOf(1, 2, 3, 4, 5), listOf(4, 5, 6, 7, 8), listOf(5, 6, 7, 8, 9))
        val expected = listOf(75, 192, 102)

        for ((input, e) in inputs.zip(expected)) {
            assertEquals(e, tool.highestStreak(input))
            assertEquals(e, tool.highestStreakWithFractions(input))
        }
    }

    @Test
    fun `PE solution correct`() {
        val expectedSet = listOf(1, 2, 5, 8)
        val expectedHighest = 51
        val actual = tool.longestStreakSet().map(Char::digitToInt)

        assertContentEquals(expectedSet, actual)
        assertEquals(expectedHighest, tool.highestStreak(actual))
    }
}