package batch3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class CoinSumsTest {
    private val tool = CoinSums()

    @ParameterizedTest(name="N={0}->{1} combos")
    @CsvSource(
        // lower constraints
        "1, 1", "2, 2", "3, 2", "4, 3", "5, 4",
        "6, 5", "7, 6", "8, 7", "9, 8",
        // lower mid constraints
        "10, 11", "15, 22", "20, 41",
        // upper mid constraints
        "50, 451", "200, 73682", "500, 6295434",
        // upper constraints
        "10000, 296710490"
    )
    fun `countCoinCombos correct for all but upper N`(n: Int, expected: Int) {
        assertEquals(expected, tool.countCoinCombos(n))
        assertEquals(expected, tool.countCoinCombosRecursive(n))
    }

    @Test
    fun `countCoinCombos speed for upper constraints`() {
        val n = 100_000
        val expected = 836_633_026
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::countCoinCombosRecursive, n).run {
            speeds.add("Recursive" to second)
            assertEquals(expected, first)
        }
        getSpeed(tool::countCoinCombos, n).run {
            speeds.add("Optimised" to second)
            assertEquals(expected, first)
        }
        compareSpeed(speeds)
    }
}