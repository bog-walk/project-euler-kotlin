package batch7

import util.tests.Benchmark
import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.assertContentEquals

internal class DigitFactorialChainsTest {
    private val tool = DigitFactorialChains()

    @Test
    fun `HR Problem correct for lower constraints`() {
        val input = listOf(
            10 to 1, 24 to 3, 29 to 2, 147 to 1, 175 to 7, 210 to 2, 221 to 7, 258 to 4,
            261 to 4, 265 to 8, 273 to 4
        )
        val expected = listOf(
            listOf(1, 2),  emptyList(), listOf(0, 10, 11), listOf(1, 2, 145),
            listOf(24, 42, 104, 114, 140, 141), listOf(0, 10, 11, 154),
            listOf(24, 42, 104, 114, 140, 141), listOf(78, 87, 196, 236), listOf(78, 87, 196, 236),
            listOf(4, 27, 39, 72, 93, 107, 117, 170, 171), listOf(78, 87, 196, 236, 263)
        )
        val solutions = listOf(
            tool::digitFactorialChainStarters,
            tool::digitFactorialChainStartersImproved,
            tool::digitFactorialChainStartersOptimised
        )
        for ((i, e) in expected.withIndex()) {
            val (limit, length) = input[i]
            for (solution in solutions) {
                assertContentEquals(
                    e, solution(limit, length), "Incorrect $limit, $length"
                )
            }
        }
    }

    @Test
    fun `HR Problem correct for mid constraints`() {
        val input = listOf(
            1999 to 50, 4000 to 60, 10_000 to 30
        )
        val expectedSizes = listOf(24, 6, 146)
        val expectedHeads = listOf(
            listOf(289, 298, 366, 466, 636, 646), listOf(1479, 1497, 1749, 1794, 1947, 1974),
            listOf(44, 126, 146, 162, 164, 206)
        )
        val solutions = listOf(
            tool::digitFactorialChainStarters,
            tool::digitFactorialChainStartersImproved,
            tool::digitFactorialChainStartersOptimised
        )
        for ((i, e) in expectedSizes.withIndex()) {
            val (limit, length) = input[i]
            for (solution in solutions) {
                val actual = solution(limit, length)
                assertEquals(e, actual.size, "Incorrect $limit, $length")
                assertContentEquals(
                    expectedHeads[i], actual.take(6), "Incorrect $limit, $length"
                )
            }
        }
    }

    @Test
    fun `HR problem correct for factorions`() {
        val limit = 1_000_000
        val length = 1
        val expected = listOf(1, 2, 145, 40585)
        assertContentEquals(expected, tool.digitFactorialChainStarters(limit, length))
        assertContentEquals(expected, tool.digitFactorialChainStartersImproved(limit, length))
        assertContentEquals(expected, tool.digitFactorialChainStartersOptimised(limit, length))
    }

    @Test
    fun `HR problem speed for upper constraints`() {
        val limit = 1_000_000
        val length = 10
        val expectedSize = 26837
        val solutions = mapOf(
            "Original" to tool::digitFactorialChainStarters,
            "Improved" to tool::digitFactorialChainStartersImproved,
            "Optimised" to tool::digitFactorialChainStartersOptimised
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, limit, length).run {
                speeds.add(name to second)
                assertEquals(expectedSize, first.size)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `PR problem correct`() {
        val limit = 1_000_000
        val length = 60
        val expectedSize = 402
        val actual = tool.digitFactorialChainStarters(limit, length)
        assertEquals(expectedSize, actual.size)
    }
}