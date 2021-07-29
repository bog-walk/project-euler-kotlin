package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class HighlyDivisibleTriangularNumberTest {
    @ParameterizedTest(name="{0} has {1} divisors")
    @CsvSource(
        "3, 2", "6, 4", "28, 6",
        "144, 15", "3455, 4", "10000, 25"
    )
    fun testCountDivisors(n: Int, expected: Int) {
        val tool = HighlyDivisibleTriangularNumber()
        assertEquals(expected, tool.countDivisors(n))
    }

    @Test
    fun testFirstTrianglesBounded() {
        val tool = HighlyDivisibleTriangularNumber()
        val expected = intArrayOf(
            1, 3, 6, 6, 28, 28, 36, 36, 36, 120,
            120, 120, 120, 120, 120, 120, 300,
            300, 528, 528, 630
        )
        assertTrue(expected.contentEquals(tool.firstTrianglesBounded(20)))
    }

    @Test
    fun testFirstTrianglesBounded_1000Divisors() {
        val tool = HighlyDivisibleTriangularNumber()
        //val actual = tool.firstTrianglesBounded(1000)
        //val actual = tool.firstTrianglesImproved(1000)
        //assertEquals(76576500, actual[500])
        //assertEquals(842161320, actual[1000])
        val triangle = tool.firstTriangleOverN(1000)
        assertEquals(842161320, triangle)
    }

    // Previous = 2806ms; Improved = 75ms
    @Test
    fun testSpeedDiff() {
        val tool = HighlyDivisibleTriangularNumber()
        val draftBefore = System.currentTimeMillis()
        val draftOutput = tool.firstTrianglesBounded(1000)
        val draftAfter = System.currentTimeMillis()
        val improvedBefore = System.currentTimeMillis()
        val improvedOutput = tool.firstTrianglesImproved(1000)
        val improvedAfter = System.currentTimeMillis()
        println("Previous solution took ${draftAfter - draftBefore}ms\n" +
                "Improved solution took ${improvedAfter - improvedBefore}ms")
        assertTrue(improvedOutput.contentEquals(draftOutput))
    }

    // Multi-output: 104ms; Single-output: 38ms
    @Test
    fun testSpeedDiff_pickSingle() {
        val tool = HighlyDivisibleTriangularNumber()
        val improvedMultiBefore = System.currentTimeMillis()
        val improvedMultiOutput = tool.firstTrianglesImproved(1000)
        val improvedMultiPick = improvedMultiOutput[1000]
        val improvedMultiAfter = System.currentTimeMillis()
        val improvedSingleBefore = System.currentTimeMillis()
        val improvedSinglePick = tool.firstTriangleOverN(1000)
        val improvedSingleAfter = System.currentTimeMillis()
        println("Multiple output solution took ${improvedMultiAfter - improvedMultiBefore}ms\n" +
                "Single output solution took ${improvedSingleAfter - improvedSingleBefore}ms")
        assertEquals(improvedMultiPick, improvedSinglePick)
    }
}