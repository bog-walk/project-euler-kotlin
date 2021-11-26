package batch4

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class DigitCancellingFractionsTest {
    private val tool = DigitCancellingFractions()

    @Test
    fun testFindNonTrivials_K1() {
        val expected = listOf(
            listOf(16 to 64, 19 to 95, 26 to 65, 49 to 98)
        )
        for (n in 2..4) {
            assertContentEquals(expected[n - 2], tool.findNonTrivials(n))
        }
    }

    @Test
    fun testProductOfNonTrivials() {
        assertEquals(100, tool.productOfNonTrivials())
    }
}