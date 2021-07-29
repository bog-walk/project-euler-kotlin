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
        val actual = tool.firstTrianglesBounded(1000)
        assertEquals(76576500, actual[500])
        assertEquals(842161320, actual[1000])
    }

}