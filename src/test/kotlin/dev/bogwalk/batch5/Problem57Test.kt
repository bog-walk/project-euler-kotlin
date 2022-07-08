package dev.bogwalk.batch5

import dev.bogwalk.util.tests.Benchmark
import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.assertContentEquals

internal class SquareRootConvergentsTest {
    private val tool = SquareRootConvergents()

    @Test
    fun `squareRootFractions correct for lower constraints`() {
        val nums = listOf(8, 10, 14, 20)
        val expected = listOf(
            listOf(8), listOf(8), listOf(8, 13), listOf(8, 13)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(
                expected[i], tool.squareRootFractionsManual(n), "Manual incorrect n=$n"
            )
            assertContentEquals(
                expected[i], tool.squareRootFractions(n), "Optimised incorrect n=$n"
            )
        }
    }

    @Test
    fun `squareRootFractions correct for mid constraints`() {
        val nums = listOf(50, 60, 100)
        val expected = listOf(
            listOf(8, 13, 21, 26, 34, 39, 47),
            listOf(8, 13, 21, 26, 34, 39, 47, 55, 60),
            listOf(8, 13, 21, 26, 34, 39, 47, 55, 60, 68, 73, 81, 86, 89, 94)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(
                expected[i], tool.squareRootFractionsManual(n), "Manual incorrect n=$n"
            )
            assertContentEquals(
                expected[i], tool.squareRootFractions(n), "Optimised incorrect n=$n"
            )
        }
    }

    @Test
    fun `squareRootFractions correct for upper constraints`() {
        val n = 1000
        val expectedSize = 153
        assertEquals(expectedSize, tool.squareRootFractionsManual(n).size)
        assertEquals(expectedSize, tool.squareRootFractions(n).size)
    }

    @Test
    fun `squareRootFractions speed`() {
        val n = 2000
        val solutions = mapOf(
            "Manual" to tool::squareRootFractionsManual,
            "Optimised" to tool::squareRootFractions
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        val results = mutableListOf<List<Int>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        assertContentEquals(results[0], results[1])
    }
}