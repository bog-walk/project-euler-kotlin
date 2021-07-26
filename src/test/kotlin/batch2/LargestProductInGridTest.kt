package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargestProductInGridTest {
    private val testGrid: Array<IntArray> = getTestGrid(
        "src/test/resources/LargestProductInGrid20By20", 20
    )

    @Test
    fun testGetTestGrid() {
        assertEquals(20, testGrid.size)
        assertEquals(20, testGrid[0].size)
        assertEquals(89, testGrid[0][0])
        assertEquals(48, testGrid[19][19])
        assertEquals(72, testGrid[15][7])
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