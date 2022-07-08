package dev.bogwalk.batch5

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

internal class PrimeDigitReplacementsTest {
    private val tool = PrimeDigitReplacements()

    @Test
    fun `HR problem correct`() {
        val args = listOf(
            Triple(2, 1, 3), Triple(2, 1, 6),
            Triple(5, 2, 7), Triple(6, 3, 8),
            Triple(5, 1, 2), Triple(3, 2, 1),
            Triple(7, 4, 6)
        )
        val expected = listOf(
            listOf(11, 13 ,17), listOf(13, 23, 43, 53, 73, 83),
            listOf(56003, 56113, 56333, 56443, 56663, 56773, 56993),
            listOf(12_1313, 222_323, 323_333, 424_343, 525_353, 626_363, 828_383, 929_393),
            listOf(10007, 10009), listOf(101),
            listOf(2_422_027, 3_433_037, 5_455_057, 6_466_067, 8_488_087, 9_499_097)
        )
        for ((i, arg) in args.withIndex()) {
            val (n, k, length) = arg
            assertContentEquals(
                expected[i],
                tool.smallestPrimeDigitRepl(n, k, length),
                "Incorrect -> $n $k $length")
        }
    }

    @Test
    fun `PE problem correct`() {
        val expected= 121_313
        assertEquals(expected, tool.smallest8PrimeFamily())
    }
}