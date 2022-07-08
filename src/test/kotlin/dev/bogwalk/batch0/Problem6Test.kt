package dev.bogwalk.batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test

internal class SumSquareDifferenceTest {
    private val tool = SumSquareDifference()

    @ParameterizedTest(name="N={0} = {1}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 4", "3, 22",
        // mid constraints
        "10, 2640", "51, 1_712_750", "100, 25_164_150",
        // larger mid constraints
        "2256, 6_477_756_566_600", "7000, 600_307_154_415_500"
    )
    fun `sumSquareDiff correct`(n: Int, expected: Long) {
        assertEquals(expected, tool.sumSquareDiffBruteOG(n))
        assertEquals(expected, tool.sumSquareDiffBrute(n))
        assertEquals(expected, tool.sumSquareDiff(n))
    }

    @Test
    fun `sumSquareDiff speed`() {
        val n = 1e4.toInt()
        val expected = 2_500_166_641_665_000
        val solutions = mapOf(
            "Brute OG" to tool::sumSquareDiffBruteOG,
            "Brute Alt" to tool::sumSquareDiffBrute,
            "Improved" to tool::sumSquareDiff
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, repeat = 10).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}