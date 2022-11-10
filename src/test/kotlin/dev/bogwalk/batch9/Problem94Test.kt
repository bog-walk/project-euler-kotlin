package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.getSpeed
import dev.bogwalk.util.tests.compareSpeed
import kotlin.math.pow

internal class AlmostEquilateralTrianglesTest {
    private val tool = AlmostEquilateralTriangles()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "15, 0", "16, 16", "17, 16", "51, 66", "200, 262", "1000, 984", "2705, 3688",
        // mid constraints
        "38000, 51406", "100_000, 51406", "500_000, 191_856", "1_000_000, 716_032"
    )
    fun `HR solution correct for lower-mid constraints`(n: Long, expected: Long) {
        assertEquals(expected, tool.perimeterSumOfAlmostEquilaterals(n))
        assertEquals(expected, tool.perimeterSumOfSequence(n))
    }

    @Test
    fun `HR solution speed`() {
        val n = 10_000_000L
        val solutions = mapOf(
            "Brute" to tool::perimeterSumOfAlmostEquilaterals,
            "Sequence" to tool::perimeterSumOfSequence
        )
        val expected = 9_973_078L
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `HR solution correct for upper constraints`() {
        val expected = listOf(
            518_408_346, 375_326_930_086,
            1_014_133_226_193_376, 734_231_055_024_833_850
        )

        for ((i, e) in (9..18 step 3).withIndex()) {
            val n = 10.0.pow(e).toLong()
            assertEquals(expected[i], tool.perimeterSumOfSequence(n))
        }
    }
}