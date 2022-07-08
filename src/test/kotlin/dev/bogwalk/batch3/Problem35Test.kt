package dev.bogwalk.batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class CircularPrimesTest {
    private val tool = CircularPrimes()

    @Test
    fun `getCircularPrimes correct for lower constraints`() {
        val n = 100
        val expected = listOf(2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, 97)
        assertContentEquals(expected, tool.getCircularPrimes(n).sorted())
    }

    @Test
    fun `getCircularPrimes correct for upper constraints`() {
        val n = 1_000_000
        val expectedLength = 55
        assertEquals(expectedLength, tool.getCircularPrimes(n).size)
    }

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "10, 17", "50, 126", "100, 446", "200, 1086",
        // upper constraint
        "1_000_000, 8_184_200"
    )
    fun `sumOfCircularPrimes correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.sumOfCircularPrimes(n))
    }
}