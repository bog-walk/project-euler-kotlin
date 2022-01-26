package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test

internal class SpecialPythagoreanTripletTest {
    private val tool = SpecialPythagoreanTriplet()

    @Test
    fun testMaxTriplet_noneFound() {
        val nums = listOf(1, 4, 6, 31, 99, 100)
        val solutions = listOf(
            tool::maxTripletBrute, tool::maxTripletParametrisation
        )
        nums.forEach { n ->
            solutions.forEach { solution ->
                assertNull(solution(n))
            }
        }
    }

    @ParameterizedTest(name="{0} has ({1}, {2}, {3})")
    @CsvSource(
        "12, 3, 4, 5", "24, 6, 8, 10",
        "30, 5, 12, 13", "90, 15, 36, 39",
        "650, 25, 312, 313", "1000, 200, 375, 425",
        "2214, 533, 756, 925", "3000, 750, 1000, 1250"
    )
    fun testFindTriplets_found(n: Int, a: Int, b: Int, c: Int) {
        val expected = Triple(a, b, c)
        val solutions = listOf(
            tool::maxTripletBrute, tool::maxTripletParametrisation
        )
        solutions.forEach { solution ->
            assertEquals(expected, solution(n))
        }
    }

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        "1, -1", "10, -1", "1231, -1",
        "12, 60", "90, 21060", "1000, 31875000",
        "3000, 937500000"
    )
    fun testMaxTripletsProduct(n: Int, expected: Long) {
        assertEquals(expected, tool.maxTripletProduct(n))
    }

    @Test
    fun testMaxTriplet_speedComparison() {
        val n = 3000
        val expected = Triple(750, 1000, 1250)
        val solutions = listOf(
            tool::maxTripletBrute, tool::maxTripletParametrisation
        )
        val times = mutableListOf<Long>()
        solutions.forEachIndexed { i, solution ->
            val time = measureNanoTime {
                repeat(10) {
                    val ans = solution(n)
                    if (it == 9) {
                        assertEquals(expected, ans)
                    }
                }
            }
            times.add(i, time)
        }
        println("Brute solution took: ${1.0 * times[0] / 1_000_000}ms\n" +
                "Optimised took: ${1.0 * times[1] / 1_000_000}ms\n")
    }
}