package batch6

import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.assertContentEquals

internal class PowerfulDigitCountsTest {
    private val tool = PowerfulDigitCounts()

    @Test
    fun `HR problem correct for lower constraints`() {
        val expected = listOf(
            listOf<Long>(1, 2, 3, 4, 5, 6, 7, 8, 9), listOf<Long>(16, 25, 36, 49, 64, 81),
            listOf<Long>(125, 216, 343, 512, 729), listOf<Long>(1296, 2401, 4096, 6561),
        )
        for (n in 1..4) {
            assertContentEquals(expected[n-1], tool.nDigitNthPowers(n))
        }
    }

    @Test
    fun `HR problem correct for mid constraints`() {
        val expected = listOf(
            listOf(1_073_741_824, 3_486_784_401), listOf(31_381_059_609), listOf(282_429_536_481)
        )
        for (n in 10..12) {
            assertContentEquals(expected[n-10], tool.nDigitNthPowers(n))
        }
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val n = 19
        val expected = listOf(1_350_851_717_672_992_089)
        assertContentEquals(expected, tool.nDigitNthPowers(n))
    }

    @Test
    fun `PE problem correct & speed`() {
        val expected = 49
        val solutions = mapOf(
            "Brute" to tool::allNDigitNthPowers, "Formula" to tool::allNDigitNthPowersFormula
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}