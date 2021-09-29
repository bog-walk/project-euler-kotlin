package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberSpiralDiagonalsTest {
    private val tool = NumberSpiralDiagonals()

    @ParameterizedTest(name="{0}x{0} sum = {1}")
    @CsvSource(
        // lower constraints
        "1, 1", "3, 25", "5, 101", "7, 261",
        // large values
        "1001, 669171001", "7001, 789195405"
    )
    fun testSpiralDiagSum(n: Int, expected: Int) {
        val modulus = 1000000000.toBigInteger() + BigInteger.valueOf(7)
        val solutions = listOf(
            tool::spiralDiagSumBrute,
            tool::spiralDiagSumRecursive,
            tool::spiralDiagSumFormula
        )
        solutions.forEach { solution ->
            val actual = solution(n.toBigInteger()).mod(modulus).toInt()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testSpiralDiagSum_huge() {
        val n = 1_000_000_000.toBigInteger()
        val startBrute = System.currentTimeMillis()
        val ansBrute = tool.spiralDiagSumBrute(n)
        val stopBrute = System.currentTimeMillis()
        val startFormula = System.currentTimeMillis()
        val ansFormula = tool.spiralDiagSumFormula(n)
        val stopFormula = System.currentTimeMillis()
        println("Brute took: ${stopBrute - startBrute}ms" +
                "\nFormula took: ${stopFormula - startFormula}ms")
        assertEquals(ansBrute, ansFormula)
    }
}