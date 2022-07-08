package dev.bogwalk.batch6

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.math.pow

internal class TotientMaximumTest {
    private val tool = TotientMaximum()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "3, 2", "5, 2", "10, 6", "15, 6", "20, 6", "25, 6", "31, 30",
        // mid constraints
        "100, 30", "200, 30", "399, 210", "450, 210", "1000, 210", "6000, 2310", "100000, 30030"
    )
    fun `correct for lower & mid constraints`(n: Long, expected: Long) {
        assertEquals(expected, tool.maxTotientRatio(n))
        assertEquals(expected, tool.maxTotientRatioPrimorial(n))
    }

    @Test
    fun `maxTotientRatio speed`() {
        val n = 1_000_000L
        val expected = 510_510L
        val solutions = mapOf(
            "Totient" to tool::maxTotientRatio, "Primorial" to tool::maxTotientRatioPrimorial
        )
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
    fun `maxTotientRatioPrimorial correct when N is max n`() {
        val n = 614_889_782_588_491_410 // max n less than N = 1e18
        val expected = 13_082_761_331_670_030
        assertEquals(expected, tool.maxTotientRatioPrimorial(n))
    }

    @Test
    fun `correct for upper constraints`() {
        val expected = listOf(
            223_092_870, 200_560_490_130, 304_250_263_527_210, 614_889_782_588_491_410
        )
        for ((i, e) in (9..18 step 3).withIndex()) {
            val n = (10.0).pow(e).toLong()
            assertEquals(expected[i], tool.maxTotientRatioPrimorial(n))
        }
    }
}