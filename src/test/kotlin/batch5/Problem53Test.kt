package batch5

import util.tests.Benchmark
import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed

internal class CombinatoricSelectionsTest {
    private val tool = CombinatoricSelections()

    @Test
    fun `correct for lower constraints`() {
        val k = 5L
        val expected = listOf(0, 0, 1, 3, 8, 14)
        for (n in 2..7) {
            assertEquals(expected[n-2], tool.countLargeCombinatorics(n, k))
            assertEquals(expected[n-2], tool.countLargeCombinatoricsImproved(n, k))
        }
    }

    @Test
    fun `correct for mid constraints`() {
        val nums = listOf(2, 23, 100, 1000)
        val k = 1_000_000L
        val expected = listOf(0, 4, 4075, 494_861)
        for ((i, n) in nums.withIndex()) {
            assertEquals(expected[i], tool.countLargeCombinatorics(n, k))
            assertEquals(expected[i], tool.countLargeCombinatoricsImproved(n, k))
        }
    }

    @Test
    fun `correct for upper constraints`() {
        val n = 1000
        val k = 1e10.toLong()
        val expected = 490_806
        assertEquals(expected, tool.countLargeCombinatorics(n, k))
        assertEquals(expected, tool.countLargeCombinatoricsImproved(n, k))
    }

    @Test
    fun `countLargeCombinatorics speed`() {
        val n = 1000
        val k = 1000L
        val expected = 497_376
        val solutions = mapOf(
            "Original" to tool::countLargeCombinatorics,
            "Improved" to tool::countLargeCombinatoricsImproved
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}