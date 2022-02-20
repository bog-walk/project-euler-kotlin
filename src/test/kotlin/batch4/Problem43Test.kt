package batch4

import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SubstringDivisibilityTest {
    private val tool = SubstringDivisibility()

    @Test
    fun `sumOfPandigitalSubstrings correct for N less than 9`() {
        val expected = listOf<Long>(
            22212, 711_104, 12_444_480, 189_838_560, 1_099_210_170, 1_113_342_912
        )
        for (n in 3..8) {
            assertEquals(expected[n - 3], tool.sumOfPandigitalSubstrings(n))
            assertEquals(expected[n - 3], tool.sumOfPandigitalSubstringsImproved(n))
        }
    }

    @Test
    fun `PE problem speed`() {
        val n = 9
        val expected = 16_695_334_890
        val solutions = mapOf(
            "Windowed" to tool::sumOfPandigitalSubstrings,
            "Improved" to tool::sumOfPandigitalSubstringsImproved
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n). run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        getSpeed(tool::sumOf9PandigitalSubstrings).run {
            speeds.add("PE" to second)
            assertEquals(expected, first, "Incorrect PE -> $first")
        }
        compareSpeed(speeds)
    }
}