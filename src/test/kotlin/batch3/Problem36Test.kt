package batch3

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.assertEquals

internal class DoubleBasePalindromesTest {
    private val tool = DoubleBasePalindromes()

    @ParameterizedTest(name="N={0} K={1}")
    @CsvSource(
        // lower constraints
        "10, 2, 25", "10, 4, 11", "10, 6, 22", "10, 8, 37",
        // normal values
        "1000, 3, 2638", "1000, 5, 1940", "1000, 7, 855",
        // upper constraints
        "1_000_000, 2, 872_187", "1_000_000, 9, 782_868"
    )
    fun `sumOfPalindromes correct`(n: Int, k: Int, expected: Int) {
        assertEquals(expected, tool.sumOfPalindromesBrute(n, k))
        assertEquals(expected, tool.sumOfPalindromes(n, k))
    }

    @Test
    fun `sumOfPalindromes speed`() {
        val n = 1_000_000_000
        val k = 2
        val solutions = mapOf(
            "Brute" to tool::sumOfPalindromesBrute, "Improved" to tool::sumOfPalindromes
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        val results = mutableListOf<Int>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        assertEquals(results[0], results[1])
    }
}