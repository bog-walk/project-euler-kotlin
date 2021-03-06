package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import dev.bogwalk.util.custom.intMatrixOf
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import dev.bogwalk.util.tests.getTestIntGrid
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargestProductInGridTest {
    private val tool = LargestProductInGrid()

    private val smallGrid: Array<IntArray> = getTestIntGrid(
        "src/test/resources/LargestProductInGrid4By4.txt", 4
    )
    private val midGrid: Array<IntArray> = getTestIntGrid(
        "src/test/resources/LargestProductInGrid6By6.txt", 6
    )
    private val largeGrid: Array<IntArray> = getTestIntGrid(
        "src/test/resources/LargestProductInGrid20By20.txt", 20
    )

    @Test
    fun `setup correctly`() {
        assertEquals(20, largeGrid.size)
        assertEquals(20, largeGrid[0].size)
        assertEquals(8, largeGrid[0][0])
        assertEquals(48, largeGrid[19][19])
        assertEquals(72, largeGrid[15][7])
    }

    @Test
    fun `largestProductInGrid correct for small & mid size`() {
        val grids = listOf(smallGrid, midGrid)
        val expected = listOf(6, 15)
        for ((i, grid) in grids.withIndex()) {
            val matrix = intMatrixOf(grid)
            assertEquals(expected[i], tool.largestProductInGrid(grid))
            assertEquals(expected[i], tool.largestProductInGridCustom(matrix))
        }
    }

    @Test
    fun `largestProductInGrid speed`() {
        val expected = 70_600_674
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::largestProductInGrid, largeGrid).run {
            speeds.add("All-in-one" to second)
            assertEquals(expected, first, "Incorrect All-in-one -> $first")
        }
        val matrix = intMatrixOf(largeGrid)
        getSpeed(tool::largestProductInGridCustom, matrix).run {
            speeds.add("Custom" to second)
            assertEquals(expected, first, "Incorrect Custom -> $first")
        }
        compareSpeed(speeds)
    }
}