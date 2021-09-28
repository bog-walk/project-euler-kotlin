package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberSpiralDiagonalsTest {
    private val tool = NumberSpiralDiagonals()

    @Test
    fun testGetSprialDiagonals() {
        val rows = listOf(1, 3, 5, 7)
        val diagonals = listOf(
            listOf(1), listOf(1, 3, 5, 7, 9),
            listOf(1, 3, 5, 7, 9, 13, 17, 21, 25),
            listOf(1, 3, 5, 7, 9, 13, 17, 21, 25, 31, 37, 43, 49)
        )
        rows.forEachIndexed { index, n ->
            assertEquals(diagonals[index], tool.getSpiralDiagonals(n))
        }
    }

    @ParameterizedTest(name="{0}x{0} sum = {1}")
    @CsvSource(
        "1, 1", "3, 25", "5, 101", "7, 261"
    )
    fun testSpiralDiagSum(n: Int, expected: Int) {
        assertEquals(expected, tool.spiralDiagSum(n))
    }
}