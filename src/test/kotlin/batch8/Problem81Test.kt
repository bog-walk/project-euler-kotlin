package batch8

import util.tests.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PathSum2WaysTest {
    private val tool = PathSum2Ways()

    @Test
    fun `HR problem correct for tiny grid`() {
        val n = 3
        val resource = "src/test/resources/PathSum2to4WaysWith3Rows.txt"
        val longGrid = getTestLongGrid(resource, n, ",")
        val intGrid = getTestIntGrid(resource, n, ",")
        val expected = 20L
        assertEquals(expected, tool.minPathSum(n, longGrid))
        assertEquals(expected, tool.minPathSumDijkstra(n, intGrid))
    }

    @Test
    fun `HR problem correct for small grid with smaller values`() {
        val n = 5
        val resource = "src/test/resources/PathSum2to4WaysWith5SmallRows.txt"
        val longGrid = getTestLongGrid(resource, n, ",")
        val intGrid = getTestIntGrid(resource, n, ",")
        val expected = 25L
        assertEquals(expected, tool.minPathSum(n, longGrid))
        assertEquals(expected, tool.minPathSumDijkstra(n, intGrid))
    }

    @Test
    fun `HR problem correct for small grid with larger values`() {
        val n = 5
        val resource = "src/test/resources/PathSum2to4WaysWith5LargeRows.txt"
        val longGrid = getTestLongGrid(resource, n, ",")
        val intGrid = getTestIntGrid(resource, n, ",")
        val expected = 2427L
        assertEquals(expected, tool.minPathSum(n, longGrid))
        assertEquals(expected, tool.minPathSumDijkstra(n, intGrid))
    }

    @Test
    fun `PE problem correct & speed`() {
        val n = 80
        val resource = "src/test/resources/PathSum2to4WaysWith80Rows.txt"
        val longGrid = getTestLongGrid(resource, n, ",")
        val intGrid = getTestIntGrid(resource, n, ",")
        val expected = 427_337L
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::minPathSum, n, longGrid).run {
            speeds.add("Dynamic" to second)
            assertEquals(expected, first)
        }
        getSpeed(tool::minPathSumDijkstra, n, intGrid).run {
            speeds.add("Dijkstra" to second)
            assertEquals(expected, first)
        }
        compareSpeed(speeds)
    }
}