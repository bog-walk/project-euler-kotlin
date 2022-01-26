package batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberSpiralDiagonalsTest {
    private val tool = NumberSpiralDiagonals()

    @ParameterizedTest(name="{0}x{0} sum = {1}")
    @CsvSource(
        // lower constraints
        "1, 1", "3, 25", "5, 101", "7, 261",
        // large values
        "1001, 669171001", "7001, 789195405"
    )
    fun testSpiralDiagSum(n: Int, expected: Int) {
        val solutions = listOf(
            tool::spiralDiagSumBrute,
            tool::spiralDiagSumFormula,
            tool::spiralDiagSumFormulaDerived
        )
        solutions.forEach { solution ->
            assertEquals(expected, solution(n.toBigInteger()))
        }
    }

    @Test
    fun testSpiralDiagSum_huge() {
        val n = 1_000_000_000.toBigInteger()
        val solutions = listOf(
            tool::spiralDiagSumBrute,
            tool::spiralDiagSumFormula,
            tool::spiralDiagSumFormulaDerived
        )
        val times = mutableListOf<Long>()
        val answers = mutableListOf<Int>()
        solutions.forEachIndexed { i, solution ->
            val time = measureNanoTime {
                answers.add(i, solution(n))
            }
            times.add(i, time)
        }
        println("Brute took: ${times[0] / 1_000_000}ms\n" +
                "Formula took: ${times[1] / 1_000_000}ms\n" +
                "Derived formula took: ${times[2]}ns")
        assertEquals(answers[0], answers[1])
        assertEquals(answers[1], answers[2])
    }
}