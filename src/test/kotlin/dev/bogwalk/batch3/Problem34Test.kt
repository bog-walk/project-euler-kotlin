package dev.bogwalk.batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DigitFactorialsTest {
    private val tool = DigitFactorials()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "10, 0", "20, 19", "30, 19", "40, 19", "50, 19",
        // larger values
        "100, 239", "200, 384", "500, 603"
    )
    fun `HR problem correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.sumOfDigitFactorialsHR(n))
    }

    @Test
    fun `PE problem correct`() {
        assertEquals(40730, tool.sumOfDigitFactorialsPE())
    }
}