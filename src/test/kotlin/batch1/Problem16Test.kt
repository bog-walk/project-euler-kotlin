package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class PowerDigitSumTest {
    private val tool = PowerDigitSum()

    @ParameterizedTest(name="2^{0} -> {1}")
    @CsvSource(
        // lower constraint
        "1, 2", "2, 4", "3, 8", "4, 7", "7, 11", "9, 8",
        // mid constraints
        "15, 26", "99, 107",
        // upper constraints
        "1000, 1366"
    )
    fun `expDigSum correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.expDigSumBuiltin(n))
        assertEquals(expected, tool.expDigSumIterative(n))
    }

    @Test
    fun `expDigSum speed`() {
        val n = 10_000
        val expected = 13561
        val solutions = mapOf(
            "Iterative" to tool::expDigSumIterative, "Built-in" to tool::expDigSumBuiltin
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}