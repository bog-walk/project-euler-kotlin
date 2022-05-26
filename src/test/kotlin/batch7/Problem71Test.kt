package batch7

import util.tests.Benchmark
import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed

internal class OrderedFractionsTest {
    private val tool = OrderedFractions()

    @Test
    fun `HR problem correct for lower constraints`() {
        val limit = 8L
        val numerators = listOf<Long>(1, 1, 2, 3, 1, 5, 5, 7)
        val denominators = listOf<Long>(7, 6, 7, 7, 2, 8, 6, 8)
        val expected = listOf(
            1L to 8L, 1L to 7L, 1L to 4L, 2L to 5L, 3L to 7L, 3L to 5L, 4L to 5L, 6L to 7L
        )
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.leftFareyNeighbour(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourImproved(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourOptimised(limit, numerators[i], denominators[i]))
        }
    }

    @Test
    fun `HR problem correct for lower mid constraints`() {
        val limit = 10_000L
        val numerators = listOf<Long>(1, 3, 1, 3)
        val denominators = listOf<Long>(5, 7, 2, 4)
        val expected = listOf(
            1999L to 9996L, 4283L to 9994L, 4999L to 9999L, 7499L to 9999L
        )
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.leftFareyNeighbour(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourImproved(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourOptimised(limit, numerators[i], denominators[i]))
        }
    }

    @Test
    fun `HR speed for mid constraints`() {
        val limit = 10_000_000L
        val n = 3L
        val d = 7L
        val expected = 4_285_712L to 9_999_995L
        val solutions = mapOf(
            "Original" to tool::leftFareyNeighbour,
            "Improved" to tool::leftFareyNeighbourImproved,
            "Optimised" to tool::leftFareyNeighbourOptimised
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, limit, n, d).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `HR problem correct for upper mid constraints`() {
        val limit = 1_000_000_000L
        val numerators = listOf<Long>(1, 3, 7)
        val denominators = listOf<Long>(7, 7, 8)
        val expected = listOf(
            142_857_142L to 999_999_995L, 428_571_428L to 999_999_999L,
            874_999_999L to 999_999_999L
        )
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.leftFareyNeighbourImproved(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourOptimised(limit, numerators[i], denominators[i]))
        }
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val limit = 1_000_000_000_000
        val numerators = listOf<Long>(1, 1, 4)
        val denominators = listOf<Long>(6, 2, 5)
        val expected = listOf(
            166_666_666_666 to 999_999_999_997, 499_999_999_999 to 999_999_999_999,
            799_999_999_999 to 999_999_999_999
        )
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.leftFareyNeighbourImproved(limit, numerators[i], denominators[i]))
            assertEquals(e, tool.leftFareyNeighbourOptimised(limit, numerators[i], denominators[i]))
        }
    }

    @Test
    fun `HR speed for upper constraints`() {
        val limit = 1_000_000_000_000_000
        val n = 100_010_627L
        val d = 100_010_633L
        val expected = 999_999_842_024_434 to 999_999_902_018_049
        val solutions = mapOf(
            "Improved" to tool::leftFareyNeighbourImproved,
            "Optimised" to tool::leftFareyNeighbourOptimised
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, limit, n, d).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `PR problem correct`() {
        val limit = 1_000_000L
        val n = 3L
        val d = 7L
        val expected = 428_570L to 999_997L // only numerator required for PR problem
        assertEquals(expected, tool.leftFareyNeighbour(limit, n, d))
    }
}