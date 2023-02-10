package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.getSpeed
import dev.bogwalk.util.tests.compareSpeed

internal class LargeNonMersennePrimeTest {
    private val tool = LargeNonMersennePrime()

    @ParameterizedTest(name="{0} * {1}^{2} + {3}")
    @CsvSource(
        // lower constraints
        "1, 2, 3, 4, 000000000012", "2, 3, 4, 5, 000000000167", "6, 7, 8, 9, 000034588815",
        // mid constraints
        "12, 13, 14, 15, 516628391483", "21, 22, 23, 24, 250786295832",
        "41, 42, 43, 44, 663853662252", "100, 101, 102, 103, 925152020203",
        // upper constraints
        "500, 501, 502, 503, 719000501003", "1111, 2222, 3333, 4444, 154267476316",
        "111111, 222222, 333333, 444444, 045667596316"
    )
    fun `tailOfVeryBigNum() correct`(a: Int, b: Int, c: Int, d: Int, expected: String) {
        assertEquals(expected, tool.tailOfVeryLargeNum(a, b, c, d))
        assertEquals(expected, tool.tailOfVeryLargeNumBI(a, b, c, d))
    }

    @Test
    fun `HR problem correct`() {
        val inputs = listOf(
            listOf("1 2 3 4", "2 3 4 5", "3 4 5 6"),
            listOf("6 7 8 9", "12 13 14 15", "21 22 23 24", "41 42 43 44")
        )
        val expectedDigits = listOf("000000003257", "431302938382")
        for ((input, expected) in inputs.zip(expectedDigits)) {
            val args = input.map { it.split(" ") }
            assertEquals(expected, tool.tailSumOfVerlyLargeNums(args))
        }
    }

    @Test
    fun `speed comparison`() {
        val expected = "8739992577"
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::tailOfNonMersennePrime, repeat=10).run {
            speeds.add("Manual" to second)
            assertEquals(expected, first)
        }
        getSpeed(tool::tailOfNonMersennePrime, true, repeat=10).run {
            speeds.add("BigInteger" to second)
            assertEquals(expected, first)
        }
        compareSpeed(speeds)
    }
}