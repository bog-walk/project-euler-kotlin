package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.getTestResource

internal class AnagramicSquaresTest {
    private val tool = AnagramicSquares()

    @Test
    fun `HR problem correct`() {
        val expected = listOf(
            961, 9216, 96100, 501_264, 9_610_000, 73_462_041, 923_187_456, 9_814_072_356,
            98_310_467_025, 985_203_145_476, 9_831_140_766_225
        )
        for (n in 3..13) {
            assertEquals(expected[n-3], tool.findLargestAnagramicSquareByDigits(n))
        }
    }

    @Test
    fun `PE problem correct`() {
        val resource = getTestResource(
            "src/test/resources/AnagramicSquares.txt",
            lineTrim = charArrayOf(' ', '\"', '\n')
        )
        assertEquals(1, resource.size)
        val words = resource.single().split("\",\"")
        val output = tool.findLargestAnagramicSquareByWords(words)
        val expected = Triple("BOARD", "BROAD", 18769)
        assertEquals(expected, output)
    }
}