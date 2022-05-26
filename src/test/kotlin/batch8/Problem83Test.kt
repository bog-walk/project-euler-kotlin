package batch8

import util.tests.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PathSum4WaysTest {
    private val tool = PathSum4Ways()

    @Test
    fun `HR problem correct for tiny grid`() {
        val n = 3
        val resource = "src/test/resources/PathSum2to4WaysWith3Rows.txt"
        val grid = getTestIntGrid(resource, n, ",")
        val expected = 20L
        assertEquals(expected, tool.minPathSumDijkstraImproved(n, grid))
        assertEquals(expected, tool.minPathSumDijkstra(n, grid))
    }

    @Test
    fun `HR problem correct for small grid with smaller values`() {
        val n = 5
        val resource = "src/test/resources/PathSum2to4WaysWith5SmallRows.txt"
        val grid = getTestIntGrid(resource, n, ",")
        val expected = 25L
        assertEquals(expected, tool.minPathSumDijkstraImproved(n, grid))
        assertEquals(expected, tool.minPathSumDijkstra(n, grid))
    }

    @Test
    fun `HR problem correct for small grid with larger values`() {
        val n = 5
        val resource = "src/test/resources/PathSum2to4WaysWith5LargeRows.txt"
        val grid = getTestIntGrid(resource, n, ",")
        val expected = 2297L
        assertEquals(expected, tool.minPathSumDijkstraImproved(n, grid))
        assertEquals(expected, tool.minPathSumDijkstra(n, grid))
    }

    @Test
    fun `PE problem correct & speed`() {
        val n = 80
        val resource = "src/test/resources/PathSum2to4WaysWith80Rows.txt"
        val grid = getTestIntGrid(resource, n, ",")
        val expected = 425_185L
        val solutions = mapOf(
            "Dijkstra Improved" to tool::minPathSumDijkstraImproved,
            "Dijkstra OG" to tool::minPathSumDijkstra
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, grid).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}