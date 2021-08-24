package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class EvenFibonacciTest {
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
        val tool = EvenFibonacci()
        assertEquals(expected, tool.sumOfFibonacciBrute(max))
        assertEquals(expected, tool.evenFibonacciRollingSum(max))
        assertEquals(expected, tool.evenFibonacciFourVariables(max))
        assertEquals(expected, tool.sumOfEvenFibonacci(max))
    }

    /**
     * evenFibonacciFourVariables: 24_500ms (FASTEST)
     * sumOfEvenFibonacci: 2_332_300ms
     * evenFibonacciRollingSum: 3_795_000ms
     * sumOfFibonacciBrute: 4_612_700ms (SLOWEST)
     */
    @Test
    fun testMaxSpeed() {
        val tool = EvenFibonacci()
        val solutions = listOf(
            tool::sumOfFibonacciBrute, tool::sumOfEvenFibonacci,
            tool::evenFibonacciRollingSum, tool::evenFibonacciFourVariables
        )
        val max = 40000000000000000L
        val expected = 49597426547377748L
        for (solution in solutions) {
            val startTime = System.nanoTime()
            assertEquals(expected, solution(max))
            val elapsedTime = System.nanoTime() - startTime
            println("${solution.name}: ${elapsedTime}ms")
        }
    }
}