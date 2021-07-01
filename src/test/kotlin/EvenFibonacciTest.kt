import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class EvenFibonacciTest {
    @ParameterizedTest(name="Max {0}: sum {1}")
    @CsvSource(
        // lower constraint max
        "1, 0", "2, 0", "3, 2", "10, 10",
        // normal max values
        "30, 10", "200, 188", "2200, 798",
        // max value is an even fibonacci
        "34, 10", "10946, 3382",
        // max is next to even fibonacci
        "9, 10", "2583, 798",
        // large max value
        "4000000, 4613732",
        // upper constraint max
        "40000000000000000, 49597426547377748"
    )
    fun testAlgorithm(max: Long, expected: Long) {
        val tool = EvenFibonacci()
        assertEquals(expected, tool.sumOfEvenFibonacci(max))
    }
}