package dev.bogwalk.batch7

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CountingFractionsTest {
    private val tool = CountingFractions()
    private lateinit var allFareyLengths: LongArray

    @BeforeAll
    fun setup() {
        allFareyLengths = tool.generateAllFareyLengths(1_000_000)
    }

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 1", "3, 3", "4, 5", "5, 9", "6, 11", "7, 17", "8, 21",
        // mid constraints
        "100, 3043", "200, 12231", "678, 139897", "1999, 1215787",
        "8888, 24015245", "10000, 30397485",
        // upper constraints
        "51999, 821903377", "300000, 27356748483"
    )
    fun `correct for all constraints`(n: Int, expected: Long) {
        assertEquals(expected, tool.fareySequenceLengthFormula(n))
        assertEquals(expected, tool.fareySequenceLengthSieve(n))
        assertEquals(expected, allFareyLengths[n])
    }

    @Test
    fun `speed for upper constraint`() {
        val n = 1_000_000
        val expected = 303_963_552_391
        val solutions = mapOf(
            "Formula" to tool::fareySequenceLengthFormula,
            "Sieve" to tool::fareySequenceLengthSieve
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, repeat = 10).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        getSpeed(tool::generateAllFareyLengths, n).run {
            speeds.add("Quick draw" to second)
            assertEquals(expected, first[n])
        }
        compareSpeed(speeds)
    }
}