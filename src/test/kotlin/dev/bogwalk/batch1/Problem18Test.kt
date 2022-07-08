package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.system.measureNanoTime

internal class MaximumPathSum1Test {
    private val tool = MaximumPathSum1()

    private fun getNestedPyramid(rows: Int, vararg elements: Int): Array<IntArray> {
        val pyramid = Array(rows) { intArrayOf() }
        var start = 0
        for (row in 0 until rows) {
            val end = start + row
            pyramid[row] = elements.sliceArray(start..(start + row))
            start = end + 1
        }
        return pyramid
    }

    @Test
    fun `maxPathSum correct for small tree`() {
        val n = 4
        val elements = intArrayOf(3, 7, 4, 2, 4, 6, 8, 5, 9, 3)
        val expected = 23
        val nested = getNestedPyramid(n, *elements)
        assertEquals(expected, tool.maxPathSum(n, *elements))
        assertEquals(expected, tool.maxPathSumDynamic(n, nested))
    }

    @Test
    fun `maxPathSum correct for large tree with speed`() {
        val n = 15
        val elements = intArrayOf(
            75, 95, 64, 17, 47, 82, 18, 35, 87, 10, 20, 4, 82, 47, 65, 19, 1, 23, 75, 3, 34,
            88, 2, 77, 73, 7, 63, 67, 99, 65, 4, 28, 6, 16, 70, 92, 41, 41, 26, 56, 83, 40,
            80, 70, 33, 41, 48, 72, 33, 47, 32, 37, 16, 94, 29, 53, 71, 44, 65, 25, 43, 91,
            52, 97, 51, 14, 70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57, 91, 71, 52, 38,
            17, 14, 91, 43, 58, 50, 27, 29, 48, 63, 66, 4, 68, 89, 53, 67, 30, 73, 16, 69,
            87, 40, 31, 4, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 4, 23
        )
        val expected = 1074
        val nested = getNestedPyramid(n, *elements)
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::maxPathSumDynamic, n, nested).run {
            speeds.add("Dynamic" to second)
            assertEquals(expected, first)
        }
        val customActual: Int
        val customTime = measureNanoTime {
            customActual = tool.maxPathSum(n, *elements)
        }
        compareSpeed("Custom" to customTime)
        assertEquals(expected, customActual)
        compareSpeed(speeds)
    }
}