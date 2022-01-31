package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class EvenFibonacciTest {
    private val tool = EvenFibonacci()

    @ParameterizedTest(name="N={0}: sum={1}")
    @CsvSource(
        // lower constraints
        "10, 10",
        // normal values
        "30, 10", "200, 188", "2200, 798",
        // N is an even fibonacci
        "34, 10", "10946, 3382",
        // N is next to even fibonacci
        "9, 10", "2583, 798",
        // large values
        "4_000_000, 4_613_732"
    )
    fun `sumOfEvenFibs correct`(n: Long, expected: Long) {
        assertEquals(expected, tool.sumOfEvenFibsBrute(n))
        assertEquals(expected, tool.sumOfEvenFibsRolling(n))
        assertEquals(expected, tool.sumOfEvenFibsFormula(n))
    }

    @Test
    fun `sumOfEvenFibs speed`() {
        val n = 4e16.toLong()
        val expected = 49_597_426_547_377_748
        val solutions = mapOf(
            "Brute" to tool::sumOfEvenFibsBrute,
            "Formula" to tool::sumOfEvenFibsFormula,
            "Custom" to tool::sumOfEvenFibsRolling
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to this.second)
                assertEquals(expected, this.first)
            }
        }
        compareSpeed(speeds)
    }
}