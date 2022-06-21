package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class SmallestMultipleTest {
    private val tool = SmallestMultiple()

    @ParameterizedTest(name="1..{0} = {1}")
    @CsvSource(
       // lower constraints
        "1, 1", "2, 2", "3, 6",
        // mid constraints
        "6, 60", "10, 2520", "20, 232_792_560",
        // upper constraints
        "30, 2_329_089_562_800"
    )
    fun `lcmOfRange correct`(n: Int, expected: Long) {
        assertEquals(expected, tool.lcmOfRange(n))
        assertEquals(expected, tool.lcmOfRangeBI(n))
        assertEquals(expected, tool.lcmOfRangeUsingPrimes(n))
    }

    @Test
    fun `lcmOfRange speed`() {
        val n = 40
        val expected = 5_342_931_457_063_200
        val solutions = mapOf(
            "Original" to tool::lcmOfRange,
            "BigInteger" to tool::lcmOfRangeBI,
            "Primes" to tool::lcmOfRangeUsingPrimes
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, repeat = 100).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}