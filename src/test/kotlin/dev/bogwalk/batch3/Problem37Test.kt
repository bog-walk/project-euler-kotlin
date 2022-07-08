package dev.bogwalk.batch3

import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TruncatablePrimesTest {
    private val tool = TruncatablePrimes()

    @Test
    fun `sumOfTruncPrimes correct`() {
        val expected = listOf(186, 1986, 8920, 8920, 748_317)
        for (e in 2..6) {
            val n = (10.0).pow(e).toInt()
            assertEquals(expected[e - 2], tool.sumOfTruncPrimes(n))
        }
    }
}