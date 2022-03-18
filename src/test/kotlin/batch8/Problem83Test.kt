package batch8

import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource

internal class PathSum4WaysTest {
    private val tool = PathSum4Ways()

    @Test
    fun `HR problem correct for tiny grid`() {
        val n = 4
        val grid = arrayOf(
            longArrayOf(2, 0, 3, 5),
            longArrayOf(8, 0, 9, 9),
            longArrayOf(0, 3, 9, 9),
            longArrayOf(0, 1, 1, 1)
        )
        val expected = 8L
        //assertEquals(expected, tool.minPathSum(n, grid))
        assertEquals(expected, tool.minPathSumBFS(n, grid))
    }

    @Test
    fun `HR problem correct for small grid with smaller values`() {
        val n = 5
        val grid = arrayOf(
            longArrayOf(1, 8, 8, 2, 8),
            longArrayOf(3, 4, 8, 2, 7),
            longArrayOf(3, 8, 0, 5, 2),
            longArrayOf(2, 2, 6, 4, 4),
            longArrayOf(0, 2, 4, 2, 8)
        )
        val expected = 25L
        //assertEquals(expected, tool.minPathSum(n, grid))
        assertEquals(expected, tool.minPathSumBFS(n, grid))
    }

    @Test
    fun `HR problem correct for small grid with larger values`() {
        val n = 5
        val grid = arrayOf(
            longArrayOf(131, 673, 234, 103, 18),
            longArrayOf(201, 96, 342, 965, 150),
            longArrayOf(630, 803, 746, 422, 111),
            longArrayOf(537, 699, 497, 121, 956),
            longArrayOf(805, 732, 524, 37, 331)
        )
        val expected = 2297L
        //assertEquals(expected, tool.minPathSum(n, grid))
        assertEquals(expected, tool.minPathSumBFS(n, grid))
    }

    @Test
    fun `PE problem correct & speed`() {
        val n = 80
        val input = getTestResource(
            "src/test/resources/PathSum2to4Ways.txt", lineSplit = ","
        ) { it.toLong() }
        val grid = Array(80) { input[it].toLongArray() }
        val expected = 425_185L
        val solutions = mapOf(
            //"Dynamic" to tool::minPathSum,
            "BFS" to tool::minPathSumBFS
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, grid).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}