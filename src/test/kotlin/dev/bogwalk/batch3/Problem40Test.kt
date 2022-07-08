package dev.bogwalk.batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ChampernownesConstantTest {
    private val tool = ChampernownesConstant()

    @ParameterizedTest(name="Digit {0}")
    @CsvSource(
        // lower indices
        "1, 1", "10, 1", "33, 1", "65, 7", "90, 5",
        // mid indices
        "145, 7", "200, 0", "298, 1",
        // higher indices
        "1000000000000, 1", "999999999934999995, 7", "1000000000000000000, 3"
    )
    fun `getConstant correct`(index: Long, expected: Int) {
        assertEquals(expected, tool.getConstant(index))
    }

    @Test
    fun `champernownesProduct correct for lower constraints`() {
        val inputs = listOf(
            listOf<Long>(1, 2, 3, 4, 5, 6, 7),
            listOf<Long>(8, 9, 10, 11, 12, 13, 14),
            listOf<Long>(1, 5, 10, 15, 20, 25, 30),
        )
        val expected = listOf(5040, 0, 140)
        for ((i, input) in inputs.withIndex()) {
            assertEquals(expected[i], tool.champernownesProduct(input))
        }
    }

    @Test
    fun `champernownesProduct correct for mid constraints`() {
        val inputs = listOf(
            listOf<Long>(10, 20, 30, 40, 50, 60, 70),
            listOf<Long>(11, 21, 31, 41, 51, 61, 71),
            listOf<Long>(1, 2, 4, 8, 16, 32, 64, 128),
        )
        val expected = listOf(144, 0, 2304)
        for ((i, input) in inputs.withIndex()) {
            assertEquals(expected[i], tool.champernownesProduct(input))
        }
    }

    @Test
    fun `champernownesProduct correct for upper constraints`() {
        val inputs = listOf(
            List(7) { e -> (10.0).pow(e).toLong() },
            List(7) { e -> (10.0).pow(e + 12).toLong() },
            listOf(
                9_999_999_999_999_995, 999_999_999_934_999_995, 999_992_599_999_999_996,
                999_991_999_999_998, 999_999_999_999_999_999, 999_123_999_999_999_999,
                1_000_000_000_000_000_000
            )
        )
        val expected = listOf(210, 5040, 370_440)
        for ((i, input) in inputs.withIndex()) {
            assertEquals(expected[i], tool.champernownesProduct(input))
        }
    }
}