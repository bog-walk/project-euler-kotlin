package dev.bogwalk.batch7

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class PrimeSummationsTest {
    private val tool = PrimeSummations()
    private val allCounts = tool.allPrimeSumCombos(1000)

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 1", "3, 1", "4, 1", "5, 2", "6, 2", "7, 3", "10, 5", "35, 175", "71, 5007",
        // mid constraints
        "80, 10003", "100, 40899", "350, 3791614657",
        //upper constraints
        "880, 4205301095670916", "1000, 48278613741845757"
    )
    fun `HR problem correct for all constraints`(n: Int, expected: Long) {
        assertEquals(expected, allCounts[n])
    }

    @Test
    fun `PE problem correct`() {
        val expected = 71
        assertEquals(expected, tool.firstPrimeSumCombo())
    }
}