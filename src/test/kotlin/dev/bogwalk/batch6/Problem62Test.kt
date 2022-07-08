package dev.bogwalk.batch6

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

internal class CubicPermutationsTest {
    private val tool = CubicPermutations()

    @Test
    fun `HR problem correct for lower constraints`() {
        val n = 1000
        val k = 3
        val expected = listOf(listOf<Long>(41_063_625, 56_623_104, 66_430_125))
        assertContentEquals(expected, tool.cubicPermutations(n, k))
    }

    @Test
    fun `HR problem correct for mid constraints`() {
        val n = 100_000
        val k = 10
        val expectedSize = 3
        val expectedSmallests = listOf(
            109_867_826_442_375, 150_178_532_496_264, 238_604_875_149_824
        )
        val actual = tool.cubicPermutations(n, k)
        assertEquals(expectedSize, actual.size)
        assertContentEquals(expectedSmallests, actual.map { it.first() })
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val n = 1_000_000
        val k = 49
        val expectedSize = 1
        val expectedSmallest = 101_740_954_697_838_253
        val actual = tool.cubicPermutations(n, k)
        assertEquals(expectedSize, actual.size)
        assertEquals(expectedSmallest, actual[0][0])
    }

    @Test
    fun `PE problem correct`() {
        val expected = listOf(
            127_035_954_683, 352_045_367_981, 373_559_126_408, 569_310_543_872, 589_323_567_104
        )
        assertContentEquals(expected, tool.smallest5CubePerm())
    }
}