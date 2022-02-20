package batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberSpiralDiagonalsTest {
    private val tool = NumberSpiralDiagonals()

    @ParameterizedTest(name="{0}x{0} = {1}")
    @CsvSource(
        // lower constraints
        "1, 1", "3, 25", "5, 101", "7, 261",
        // mid constraints
        "1001, 669_171_001", "7001, 789_195_405"
    )
    fun `spiralDiagSum correct`(n: Long, expected: Int) {
        val solutions = listOf(
            tool::spiralDiagSumBrute,
            tool::spiralDiagSumFormulaBrute,
            tool::spiralDiagSumFormulaDerived
        )
        solutions.forEach { solution ->
            assertEquals(expected, solution(n))
        }
    }

    @Test
    fun `spiralDiagSum speed`() {
        val n = 1_000_001L
        val expected = 4_315_867
        val solutions = mapOf(
            "Brute" to tool::spiralDiagSumBrute,
            "Formula brute" to tool::spiralDiagSumFormulaBrute,
            "Formula derived" to tool::spiralDiagSumFormulaDerived
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}