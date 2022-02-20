package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IntegerRightTrianglesTest() {
    private val tool = IntegerRightTriangles()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lowest constraints
        "12, 12", "15, 12", "40, 12", "50, 12",
        // less low constraints
        "80, 60", "100, 60",
        // lower constraints
        "1000, 840"
    )
    fun `mostTripletSolutions correct for lower constraints`(n: Int, expected: Int) {
        assertEquals(expected, tool.mostTripletSolutionsBrute(n))
        assertEquals(expected, tool.mostTripletSolutions(n))
        assertEquals(expected, tool.mostTripletSolutionsImproved(n))
    }

    @Test
    fun `mostTripletSolutions speed`() {
        val n = 100_000
        val expected = 55440
        val solutions = mapOf(
            "Brute" to tool::mostTripletSolutionsBrute,
            "Original" to tool::mostTripletSolutions,
            "Improved" to tool::mostTripletSolutionsImproved
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}