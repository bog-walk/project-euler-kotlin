package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
}