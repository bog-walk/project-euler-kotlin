package batch3

import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PandigitalProductsTest {
    private val tool = PandigitalProducts()

    @Test
    fun `sumPandigitalProducts correct for all but upper N`() {
        val expected = listOf(12, 52, 162, 0, 13458)
        for (n in 4..8) {
            assertEquals(expected[n - 4], tool.sumPandigitalProductsAllPerms(n))
            assertEquals(expected[n - 4], tool.sumPandigitalProductsBrute(n))
        }
    }

    @Test
    fun `sumPandigitalProducts speed for upper constraints`() {
        val n = 9
        val expected = 45228
        val solutions = mapOf(
            "All permutations" to tool::sumPandigitalProductsAllPerms,
            "Brute" to tool::sumPandigitalProductsBrute
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}