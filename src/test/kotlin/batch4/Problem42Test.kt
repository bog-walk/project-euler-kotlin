package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CodedTriangleNumbersTest {
    private val tool = CodedTriangleNumbers()

    private val inputLocation = "src/test/resources/CodedTriangleNumbers.txt"

    @Test
    fun `triangleNumber returns -1 for non-triangular numbers`() {
        val nums = listOf<Long>(2, 5, 26, 54, 218)
        val expected = -1
        for (n in nums) {
            assertEquals(expected, tool.triangleNumber(n))
            assertEquals(expected, tool.triangleNumberImproved(n))
        }
    }

    @ParameterizedTest(name="tN = {0}")
    @CsvSource(
        // lower constraints
        "1, 1", "3, 2", "6, 3", "10, 4",
        // mid constraints
        "55, 10", "210, 20", "5050, 100",
        // upper constraints
        "500500, 1000", "4999999950000000, 99999999"
    )
    fun `triangleNumber returns correct triangle term`(tN: Long, expected: Int) {
        assertEquals(expected, tool.triangleNumber(tN))
        assertEquals(expected, tool.triangleNumberImproved(tN))
    }

    @Test
    fun `HR problem speed`() {
        val tN = 1_000_000_000_000_000_000
        val solutions = mapOf(
            "Original" to tool::triangleNumber, "Improved" to tool::triangleNumberImproved
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        val results = mutableListOf<Int>()
        for ((name, solution) in solutions) {
            getSpeed(solution, tN).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        assertEquals(results[0], results[1])
    }

    @Test
    fun `PE problem correct`() {
        val input = getTestResource(inputLocation)
        val expected = 162
        assertEquals(expected, tool.countTriangleWords(input))
    }
}