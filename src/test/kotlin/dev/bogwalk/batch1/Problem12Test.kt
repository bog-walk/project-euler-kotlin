package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test

internal class HighlyDivisibleTriangularNumberTest {
    private val tool = HighlyDivisibleTriangularNumber()

    @ParameterizedTest(name="{0} has {1} divisors")
    @CsvSource(
        "2, 2", "3, 2", "6, 4", "28, 6", "144, 15", "3455, 4", "10_000, 25"
    )
    fun `countDivisors correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.countDivisors(n))
    }

    @Test
    fun `firstTrianglesCache correct`() {
        val expected = intArrayOf(
            1, 3, 6, 6, 28, 28, 36, 36, 36, 120, 120, 120, 120, 120, 120, 120, 300, 300,
            528, 528, 630
        )
        assertTrue(expected.contentEquals(tool.firstTrianglesCache(20)))
    }

    @ParameterizedTest(name="First t over {0} divisors = {1}")
    @CsvSource(
        // lower constraints
        "1, 3", "2, 6", "4, 28", "12, 120",
        // upper constraints
        "500, 76_576_500", "900, 842_161_320"
    )
    fun `firstTriangleOverN correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.firstTriangleBruteA(n))
        assertEquals(expected, tool.firstTriangleBruteB(n))
        assertEquals(expected, tool.firstTriangleBruteC(n))
        assertEquals(expected, tool.firstTriangleOverNOptimised(n))
        assertEquals(expected, tool.firstTriangleOverNUsingPrimes(n))
    }

    @Test
    fun `firstTriangleOverN speed`() {
        val n = 1000
        val expected = 842_161_320
        val solutions = mapOf(
            "Brute A - Cycle" to tool::firstTriangleBruteA,
            "Brute B - All" to tool::firstTriangleBruteB,
            "Brute C - Sequence" to tool::firstTriangleBruteC,
            "Prime" to tool::firstTriangleOverNUsingPrimes,
            "Optimised" to tool::firstTriangleOverNOptimised
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, repeat = 10).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        getSpeed(tool::firstTrianglesCache, n).run {
            speeds.add("Quick pick" to second)
            assertEquals(expected, first[1000], "Incorrect Quick pick -> $first")
        }
        compareSpeed(speeds)
    }
}