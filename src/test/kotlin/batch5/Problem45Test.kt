package batch5

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class TriPentHexTest {
    private val tool = TriPentHex()

    @Test
    fun testCommonNumbers_low() {
        val expected = listOf(1L)
        assertContentEquals(expected, tool.commonNumbers(2L, 3, 5))
        assertContentEquals(expected, tool.commonNumbers(2L, 5, 6))
        assertContentEquals(expected, tool.commonNumbers(10L, 3, 5))
    }

    @Test
    fun testCommonNumbers_mid() {
        assertContentEquals(
            listOf(1L, 210L), tool.commonNumbers(10000L, 3, 5)
        )
        assertContentEquals(
            listOf(1L, 40755L), tool.commonNumbers(100000L, 5, 6)
        )
    }

    @Test
    fun testCommonNumbers_high() {
        assertContentEquals(
            listOf(1L, 210L, 40755L),
            tool.commonNumbers(1000000L, 3, 5)
        )
        assertContentEquals(
            listOf(1L, 40755L), tool.commonNumbers(10000000L, 5, 6)
        )
    }

    @Test
    fun testNextPentHex() {
        assertEquals(1533776805L, tool.nextTripleType())
    }
}