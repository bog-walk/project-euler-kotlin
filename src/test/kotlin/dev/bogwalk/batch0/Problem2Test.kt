package dev.bogwalk.batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test

internal class EvenFibonacciTest {
    private val tool = EvenFibonacci()

    @ParameterizedTest(name="N={0}: sum={1}")
    @CsvSource(
        // lower constraints
        "10, 10",
        // mid constraints
        "30, 10", "200, 188", "2200, 798",
        // N is an even fibonacci
        "34, 10", "10946, 3382",
        // N is next to even fibonacci
        "9, 10", "2583, 798",
        // larger mid constraints
        "4_000_000, 4_613_732"
    )
    fun `sumOfEvenFibs correct`(n: Long, expected: Long) {
        assertEquals(expected, tool.sumOfEvenFibsNaive(n))
        assertEquals(expected, tool.sumOfEvenFibsBrute(n))
        assertEquals(expected, tool.sumOfEvenFibsRolling(n))
        assertEquals(expected, tool.sumOfEvenFibsFormula(n))
    }

    @Test
    fun `sumOfEvenFibs speed`() {
        val n = 4e16.toLong()
        val expected = 49_597_426_547_377_748
        val solutions = mapOf(
            "Naive" to tool::sumOfEvenFibsNaive,
            "Brute" to tool::sumOfEvenFibsBrute,
            "Formula" to tool::sumOfEvenFibsFormula,
            "Custom" to tool::sumOfEvenFibsRolling
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, warmup = 1, repeat = 1000).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}