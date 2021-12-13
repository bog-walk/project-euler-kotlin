package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IntegerRightTrianglesTest() {
    private val tool = IntegerRightTriangles()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // low N
        "12, 12", "15, 12", "40, 12", "50, 12",
        // mid N
        "80, 60", "100, 60",
        // higher N
        "1000, 840"
    )
    fun testMostTripletSolutions(n: Int, expected: Int) {
        assertEquals(expected, tool.mostTripletSolutionsBrute(n))
        assertEquals(expected, tool.mostTripletSolutions(n))
        assertEquals(expected, tool.mostTripletSolutionsImproved(n))
    }

    @Test
    fun testMostTriplets_speedComparison() {
        val n = 100000
        val expected = 55440
        val solutions = listOf(
            tool::mostTripletSolutionsBrute,
            tool::mostTripletSolutions,
            tool::mostTripletSolutionsImproved
        )
        val times = mutableListOf<Long>()
        solutions.forEachIndexed { i, solution ->
            val time = measureNanoTime {
                assertEquals(expected, solution(n))
            }
            times.add(i, time)
        }
        println("Brute solution took: ${1.0 * times[0] / 1_000_000_000}s\n" +
                "Normal solution took: ${1.0 * times[1] / 1_000_000_000}s\n" +
                "Improved solution took: ${1.0 * times[2] / 1_000_000_000}s\n")
    }
}