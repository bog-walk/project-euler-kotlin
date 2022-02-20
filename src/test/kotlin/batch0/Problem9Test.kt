package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class SpecialPythagoreanTripletTest {
    private val tool = SpecialPythagoreanTriplet()

    @Test
    fun `maxTriplet returns null when none found`() {
        val nums = listOf(4, 6, 31, 99, 100)
        val solutions = listOf(
            tool::maxTripletBruteBC, tool::maxTripletBruteA, tool::maxTripletOptimised
        )
        nums.forEach { n ->
            solutions.forEach { solution ->
                assertNull(solution(n))
            }
        }
    }

    @ParameterizedTest(name="{0} = ({1}, {2}, {3})")
    @CsvSource(
        // lower constraints
        "12, 3, 4, 5", "24, 6, 8, 10", "30, 5, 12, 13",
        // mid constraints
        "90, 15, 36, 39", "650, 25, 312, 313", "1000, 200, 375, 425",
        // large values
        "2214, 533, 756, 925", "3000, 750, 1000, 1250"
    )
    fun `maxTriplet returns triple when found`(n: Int, a: Int, b: Int, c: Int) {
        val expected = Triple(a, b, c)
        val solutions = listOf(
            tool::maxTripletBruteBC, tool::maxTripletBruteA, tool::maxTripletOptimised
        )
        solutions.forEach { solution ->
            assertEquals(expected, solution(n))
        }
    }

    @Test
    fun `maxTriplet speed`() {
        val n = 3000
        val expected = 937_500_000
        val solutions = mapOf(
            "BC Loop" to tool::maxTripletBruteBC,
            "A Loop" to tool::maxTripletBruteA,
            "Optimised" to tool::maxTripletOptimised
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(tool::maxTripletProduct, n, solution).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }

    @ParameterizedTest(name="N={0} returns {1}")
    @CsvSource(
        // none found
        "1, -1", "10, -1", "1231, -1",
        // lower constraints
        "12, 60",
        // mid constraints
        "90, 21060", "1000, 31_875_000"
    )
    fun `maxTripletProduct correct`(n: Int, expected: Int) {
        val solutions = listOf(
            tool::maxTripletBruteBC, tool::maxTripletBruteA, tool::maxTripletOptimised
        )
        solutions.forEach { solution ->
            assertEquals(expected, tool.maxTripletProduct(n, solution))
        }
    }
}