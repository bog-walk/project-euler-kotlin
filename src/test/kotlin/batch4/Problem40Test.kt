package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ChampernownesConstantTest {
    private val tool = ChampernownesConstant()

    @ParameterizedTest(name="Digit {0}")
    @CsvSource(
        "1, 1", "10, 1", "33, 1", "65, 7", "90, 5",
        "145, 7", "200, 0", "298, 1"
    )
    fun testGetConstant(index: Long, expected: Int) {
        assertEquals(expected, tool.getConstant(index))
    }

    @Test
    fun testChampernownesProduct_low() {
        val inputs = listOf(
            listOf<Long>(1, 2, 3, 4, 5, 6, 7),
            listOf<Long>(8, 9, 10, 11, 12, 13, 14),
            listOf<Long>(1, 5, 10, 15, 20, 25, 30),
        )
        val expected = listOf(5040, 0, 140)
        inputs.forEachIndexed { i, input ->
            assertEquals(expected[i], tool.champernownesProduct(input))
        }
    }

    @Test
    fun testChampernownesProduct_mid() {
        val inputs = listOf(
            listOf<Long>(10, 20, 30, 40, 50, 60, 70),
            listOf<Long>(11, 21, 31, 41, 51, 61, 71),
            listOf<Long>(1, 2, 4, 8, 16, 32, 64, 128),
        )
        val expected = listOf(144, 0, 2304)
        inputs.forEachIndexed { i, input ->
            assertEquals(expected[i], tool.champernownesProduct(input))
        }
    }

    @Test
    fun testChampernownesProduct_high() {
        val input = listOf<Long>(1, 10, 100, 1000, 10_000, 100_000, 1_000_000)
        val expected = 210
        assertEquals(expected, tool.champernownesProduct(input))
    }
}