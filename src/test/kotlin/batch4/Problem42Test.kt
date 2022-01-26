package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CodedTriangleNumbersTest {
    private val tool = CodedTriangleNumbers()
    private val inputLocation = "src/test/resources/CodedTriangleNumbers"

    @Test
    fun testTriangleNumber_noneTriangles() {
        val nums = listOf<Long>(2, 5, 26, 54, 218)
        nums.forEach { n ->
            assertEquals(-1, tool.triangleNumber(n))
            assertEquals(-1, tool.triangleNumberAlt(n))
        }
    }

    @ParameterizedTest(name="t_n = {0}")
    @CsvSource(
        "1, 1", "3, 2", "6, 3", "10, 4",
        "55, 10", "210, 20", "5050, 100",
        "500500, 1000", "4999999950000000, 99999999"
    )
    fun testTriangleNumber(tN: Long, expected: Int) {
        assertEquals(expected, tool.triangleNumber(tN))
        assertEquals(expected, tool.triangleNumberAlt(tN))
    }

    @Test
    fun testCountTriangleWords() {
        val input = File(inputLocation).readLines()
        val expected = 162
        val actual = tool.countTriangleWords(input)
        assertEquals(expected, actual)
    }
}