package dev.bogwalk.batch5

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

internal class PermutedMultiplesTest {
    private val tool = PermutedMultiples()

    @Test
    fun `HR problem correct for lower constraints`() {
        val n = 125_875
        val k = 2
        val expected = listOf(listOf(125_874, 251_748))
        assertContentEquals(expected, tool.permutedMultiples(n, k))
    }

    @Test
    fun `HR problem correct for mid constraints`() {
        val n = 1_000_000
        val k = 4
        val expected = listOf(listOf(142_857, 285_714, 428_571, 571_428))
        assertContentEquals(expected, tool.permutedMultiples(n, k))
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val n = 2_000_000
        val k = 6
        val expected = listOf(
            listOf(142_857, 285_714, 428_571, 571_428, 714_285, 857_142),
            listOf(1_428_570, 2_857_140, 4_285_710, 5_714_280, 7_142_850, 8_571_420),
            listOf(1_429_857, 2_8597_14, 4_289_571, 5_7194_28, 7_149_285, 8_579_142)
        )
        assertContentEquals(expected, tool.permutedMultiples(n, k))
    }

    @Test
    fun `PE problem correct`() {
        val expected = 142_857
        assertEquals(expected, tool.smallestPermutedMultiple())
    }
}