package batch4

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PandigitalMultiplesTest {
    private val tool = PandigitalMultiples()

    @Test
    fun testFindPandigitalMultipliers() {
        val nums = listOf(100, 1000, 10000)
        val expected = listOf(
            listOf(listOf(18, 78), listOf(9)),
            listOf(listOf(18, 78), listOf(9, 192, 219, 273, 327)),
            listOf(
                listOf(18, 78, 1728, 1764, 1782, 1827, 2178, 2358, 2718, 2817, 3564,
                    3582, 4176, 4356),
                listOf(9, 192, 219, 273, 327, 6729, 6792, 6927, 7269, 7293, 7329,
                    7692, 7923, 7932, 9267, 9273, 9327)
            )
        )
        nums.forEachIndexed { i, n ->
            var k = 8
            repeat(2) { j ->
                assertEquals(expected[i][j], tool.findPandigitalMultipliers(n, k))
                k = 9
            }
        }
    }

    @Test
    fun testLargest9Pandigital() {
        val expected = "932718654"
        assertEquals(expected, tool.largest9Pandigital())
    }
}