package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class DistinctPowersTest {
    private val tool = DistinctPowers()

    @ParameterizedTest(name="N={0} = {1}")
    @CsvSource(
        // lower constraints
        "2, 1", "3, 4", "4, 8", "5, 15", "10, 69",
        // normal values
        "20, 324", "50, 2184", "100, 9183", "200, 37774"
    )
    fun `distinctPowers correct`(n: Int, expected: Long) {
        assertEquals(expected, tool.distinctPowersBrute(n))
        assertEquals(expected, tool.distinctPowers(n))
    }

    @Test
    fun `distinctPowers speed`() {
        val n = 1000
        val expected = 977_358L
        val solutions = mapOf(
            "Brute" to tool::distinctPowersBrute,
            "Improved" to tool::distinctPowers
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}