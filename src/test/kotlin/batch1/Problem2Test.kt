package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test

internal class EvenFibonacciTest {
    private val tool = EvenFibonacci()

    @ParameterizedTest(name="Max {0}: sum {1}")
    @CsvSource(
        // lower constraint max
        "10, 10",
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
        assertEquals(expected, tool.sumOfFibonacciBrute(max))
        assertEquals(expected, tool.evenFibonacciRollingSum(max))
        assertEquals(expected, tool.evenFibonacciFourVariables(max))
        assertEquals(expected, tool.sumOfEvenFibonacci(max))
    }

    @Test
    fun testMaxSpeed() {
        val solutions = listOf(
            tool::sumOfFibonacciBrute, tool::sumOfEvenFibonacci,
            tool::evenFibonacciRollingSum, tool::evenFibonacciFourVariables
        )
        val max = 40000000000000000L
        val expected = 49597426547377748L
        for (solution in solutions) {
            val elapsedTime = measureNanoTime {
                assertEquals(expected, solution(max))
            }
            println("${solution.name}: ${elapsedTime}ns")
        }
    }
}