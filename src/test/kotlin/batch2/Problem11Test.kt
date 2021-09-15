package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import util.intMatrixOf
import java.io.File
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargestProductInGridTest {
    private val tool = LargestProductInGrid()
    private val smallGrid: Array<IntArray> = getTestGrid(
        "src/test/resources/LargestProductInGrid4By4", 4
    )
    private val midGrid: Array<IntArray> = getTestGrid(
        "src/test/resources/LargestProductInGrid6By6", 6
    )
    private val largeGrid: Array<IntArray> = getTestGrid(
        "src/test/resources/LargestProductInGrid20By20", 20
    )

    @Test
    fun testMaxFromGrid() {
        assertEquals(6, tool.maxFromGrid(smallGrid))
        assertEquals(15, tool.maxFromGrid(midGrid))
        assertEquals(70600674, tool.maxFromGrid(largeGrid))
    }

    @Test
    fun testMaxProduct_smallest() {
        val matrix = intMatrixOf(smallGrid)
        val tool = LargestProductInGrid()
        assertEquals(6, tool.maxProductSmallest(matrix))
    }

    @Test
    fun testAssessRows() {
        val tool = LargestProductInGrid()
        assertEquals(3, tool.assessRows(smallGrid))
        assertEquals(15, tool.assessRows(midGrid))
    }

    @Test
    fun testAssessCols() {
        val tool = LargestProductInGrid()
        assertEquals(6, tool.assessCols(smallGrid))
        assertEquals(5, tool.assessCols(midGrid))
    }

    @Test
    fun testAssessDiagonals() {
        val tool = LargestProductInGrid()
        assertEquals(3, tool.assessDiagonals(smallGrid))
        assertEquals(1, tool.assessDiagonals(midGrid))
    }

    @Test
    fun testGetTestGrid() {
        assertEquals(20, largeGrid.size)
        assertEquals(20, largeGrid[0].size)
        assertEquals(89, largeGrid[0][0])
        assertEquals(48, largeGrid[19][19])
        assertEquals(72, largeGrid[15][7])
    }

    private fun getTestGrid(filename: String, size: Int): Array<IntArray> {
        val grid = Array(size) { IntArray(size) }
        var i = 0
        File(filename).forEachLine { line ->
            grid[i] = line.trim().split(" ").map {
                it.toInt()
            }.toIntArray()
            i++
        }
        return grid
    }
}