package batch8

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.math.exp

internal class MonopolyOddsTest {
    private val tool = MonopolyOdds()

    @ParameterizedTest(name="N={0}, K={1}")
    @CsvSource(
        // lower constraints
        "4, 3, 101524", "5, 4, 10242519", "6, 5, 1024001925", "7, 6, 102400190525"
    )
    fun `monteCarloOdds correct`(n: Int, k: Int, expected: String) {
        assertEquals(expected, tool.monteCarloOdds(n, k))
    }
}