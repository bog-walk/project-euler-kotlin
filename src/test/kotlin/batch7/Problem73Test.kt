package batch7

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class CountingFractionsInARangeTest {
    private val tool = CountingFractionsInARange()

    @ParameterizedTest(name="D={0} A={1}")
    @CsvSource(
        // lower constraints
        "3, 2, 0", "5, 2, 1", "8, 2, 3", "8, 3, 1", "8, 6, 0",
        // mid constraints
        "100, 2, 505", "100, 4, 150"
    )
    fun `correct for lower to mid constraints`(d: Int, a: Int, expected: Long) {
        assertEquals(expected, tool.fareyRangeCountRecursive(d, a))
        assertEquals(expected, tool.fareyRangeCountIterative(d, a))
        assertEquals(expected, tool.fareyRangeCountSieve(d, a))
    }

    @ParameterizedTest(name="D={0} A={1}")
    @CsvSource(
        "49999, 3, 63325374", "50000, 2, 126654024", "200000, 5, 405286681",
        "1000000, 2, 50660592050", "2000000, 100, 120381464"
    )
    fun `correct for upper constraints`(d: Int, a: Int, expected: Long) {
        assertEquals(expected, tool.fareyRangeCountSieve(d, a))
    }

    @Test
    fun `speed for lower constraints`() {
        val d = 12000
        val a = 2
        val expected = 7_295_372L
        val solutions = mapOf(
            "Recursive count" to tool::fareyRangeCountRecursive,
            "Iterative count" to tool::fareyRangeCountIterative,
            "Inclusion-Exclusion" to tool::fareyRangeCountIE,
            "Rank" to tool::fareyRangeCountSieve
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, d, a).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `speed for lower mid constraints`() {
        val d = 100_000
        val a = 100
        val expected = 300_980L
        val solutions = mapOf(
            "Recursive count" to tool::fareyRangeCountRecursive,
            "Iterative count" to tool::fareyRangeCountIterative
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, d, a).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `speed for upper constraints`() {
        val d = 2_000_000
        val a = 2
        val expected = 202_642_449_955L
        val solutions = mapOf(
            "Inclusion-Exclusion" to tool::fareyRangeCountIE,
            "Rank" to tool::fareyRangeCountSieve
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, d, a).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}