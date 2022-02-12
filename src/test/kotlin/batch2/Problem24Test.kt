package batch2

import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LexicographicPermutationsTest {
    private val tool = LexicographicPermutations()

    @Test
    fun `lexicographicPerm correct for short strings`() {
        val input = "abc"
        val expected = listOf("abc", "acb", "bac", "bca", "cab", "cba")
        for (n in 0..5) {
            assertEquals(expected[n], tool.lexicographicPerm(n.toLong(), input))
            assertEquals(expected[n], tool.lexicographicPermImproved(n.toLong(), input))
        }
    }

    @Test
    fun `lexicographicPerm correct for long strings`() {
        val input = "0123456789"
        // 10! = 3_628_800 permutations, already 0-indexed
        val permutations = listOf<Long>(0, 362_880, 1_088_640, 3_628_799)
        val expected = listOf(
            "0123456789", "1023456789", "3012456789", "9876543210"
        )
        permutations.forEachIndexed { i, p ->
            assertEquals(expected[i], tool.lexicographicPerm(p, input))
            assertEquals(expected[i], tool.lexicographicPermImproved(p, input))
        }
    }

    @Test
    fun `lexicographicPerm speed`() {
        val input = "0123456789"
        val permutation = 999_999L // the millionth permutation
        val expected = "2783915460"
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::lexicographicPerm, permutation, input).run {
            speeds.add("Original" to second)
            assertEquals(expected, first)
        }
        getSpeed(tool::lexicographicPermImproved, permutation, input).run {
            speeds.add("Improved" to second)
            assertEquals(expected, first)
        }
        compareSpeed(speeds)
    }
}