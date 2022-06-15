package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class LargestPalindromeProductTest {
    private val tool = LargestPalindromeProduct()

    @ParameterizedTest(name="N={0}")
    @CsvSource(
        // lower constraints
        "101_102, 101_101", "101_110, 101_101",
        // mid constraints
        "794_000, 793_397", "650_001, 649_946",
        // palindrome value
        "332_233, 330_033"
    )
    fun `largestPalindromeProduct correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.largestPalindromeProductBrute(n))
        assertEquals(expected, tool.largestPalindromeProductBruteBackwards(n))
        assertEquals(expected, tool.largestPalindromeProductAlt(n))
        assertEquals(expected, tool.largestPalindromeProduct(n))
    }

    @Test
    fun `largestPalindromeProduct speed`() {
        val n = 1e6.toInt() - 1
        val expected = 906_609
        val solutions = mapOf(
            "Brute" to tool::largestPalindromeProductBrute,
            "Brute Backwards" to tool::largestPalindromeProductBruteBackwards,
            "Valid Palindrome" to tool::largestPalindromeProductAlt,
            "Improved" to tool::largestPalindromeProduct
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, repeat = 1000).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}