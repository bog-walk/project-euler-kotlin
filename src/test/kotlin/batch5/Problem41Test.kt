package batch5

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PandigitalPrimeTest {
    private val tool = PandigitalPrime()

    @Test
    fun testLargestPandigitalPrimePE() {
        val expected = 7652413
        assertEquals(expected, tool.largestPandigitalPrimePE())
    }

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        "10, -1", "100, -1",
        "2140, 1423", "10000, 4231", "100000, 4231", "1000000, 4231",
        "7652412, 7642513", "10000000, 7652413"
    )
    fun testLargestPandigitalPrimeHR(n: Long, expected: Int) {
        assertEquals(expected, tool.largestPandigitalPrimeHR(n))
    }
}