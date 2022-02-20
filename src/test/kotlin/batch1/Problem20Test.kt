package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class FactorialDigitSumTest {
    private val tool = FactorialDigitSum()

    @ParameterizedTest(name="{0}! = {1}")
    @CsvSource(
        // lower constraints
        "0, 1", "1, 1", "2, 2", "3, 6",
        // smaller N
        "4, 6", "5, 3", "6, 9", "10, 27",
        // larger N
        "100, 648", "333, 2862", "750, 7416",
        // upper constraints
        "946, 9675"
    )
    fun `factorialDigitSum correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.factorialDigitSum(n))
        assertEquals(expected, tool.factorialDigitSumAlt(n))
    }

    @Test
    fun `factorialDigitSum upper constraints & speed`() {
        val n = 1000
        val expected = 10539
        val solutions = mapOf(
            "Original" to tool::factorialDigitSum,
            "Alt" to tool::factorialDigitSumAlt
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