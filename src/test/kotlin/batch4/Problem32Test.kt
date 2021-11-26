package batch4

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PandigitalProductsTest {
    private val tool = PandigitalProducts()

    @Test
    fun testSumPandigitalProducts() {
        val expected = listOf(12, 52, 162, 0, 13458, 45228)
        for (n in 4..9) {
            assertEquals(expected[n - 4], tool.sumPandigitalProducts(n))
        }
    }
}