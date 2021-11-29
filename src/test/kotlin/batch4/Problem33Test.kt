package batch4

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class DigitCancellingFractionsTest {
    private val tool = DigitCancellingFractions()

    @Test
    fun testFindNonTrivials_K1() {
        val expected = listOf(
            listOf(16 to 64, 19 to 95, 26 to 65, 49 to 98),
            listOf(166 to 664, 199 to 995, 217 to 775, 249 to 996, 266 to 665, 499 to 998),
            listOf(
                1249 to 9992, 1666 to 6664, 1999 to 9995, 2177 to 7775,
                2499 to 9996, 2666 to 6665, 4999 to 9998
            )
        )
        for (n in 2..4) {
            assertContentEquals(expected[n - 2], tool.findNonTrivials(n))
        }
    }

    @Test
    fun testFindNonTrivials_K2() {
        val expected = listOf(166 to 664, 199 to 995, 266 to 665, 484 to 847, 499 to 998)
        val actual = tool.findNonTrivials(3, k=2)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testProductOfNonTrivials() {
        assertEquals(100, tool.productOfNonTrivials())
    }
}