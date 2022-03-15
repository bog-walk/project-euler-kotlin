package batch7

import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed

internal class CoinPartitionsTest {
    private val tool = CoinPartitions()

    @Test
    fun `HR problem correct for lower constraints`() {
        val limit = 100
        val nums = listOf(2, 3, 4, 5, 6, 7, 10, 100)
        val expected = listOf(2, 3, 5, 7, 11, 15, 42, 190_569_292)
        val dynamicActual = tool.coinPileCombos(limit)
        val theoremActual = tool.coinPileCombosTheorem(limit)
        for ((i, n) in nums.withIndex()) {
            assertEquals(expected[i], dynamicActual[n])
            assertEquals(expected[i], theoremActual[n])
        }
    }

    @Test
    fun `HR problem speed for lower constraints`() {
        val n = 1000
        val expected = 709_496_666
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::coinPileCombos, n).run {
            speeds.add("Dynamic" to second)
            assertEquals(expected, first[n])
        }
        getSpeed(tool::coinPileCombosTheorem, n).run {
            speeds.add("Theorem" to second)
            assertEquals(expected, first[n])
        }
        compareSpeed(speeds)
    }

    @Test
    fun `HR problem correct for mid to upper constraints`() {
        val limit = 60_000
        val nums = listOf(500, 8432, 30_000, 60_000)
        val expected = listOf(
            168_879_716, 720_964_692, 977_046_096, 168_497_963
        )
        val theoremActual = tool.coinPileCombosTheorem(limit)
        for ((i, n) in nums.withIndex()) {
            assertEquals(expected[i], theoremActual[n])
        }
    }

    @Test
    fun `PE problem correct`() {
        val expected = 55374
        assertEquals(expected, tool.firstCoinCombo())
    }
}