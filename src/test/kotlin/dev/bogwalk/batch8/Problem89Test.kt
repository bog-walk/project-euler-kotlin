package dev.bogwalk.batch8

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.getTestResource

internal class RomanNumeralsTest {
    private val tool = RomanNumerals()

    @ParameterizedTest(name="{0} should be {1}")
    @CsvSource(
        // more than 3 in a row and not M
        "IIIII, V", "MMMMMMMMMMMMMIIII, MMMMMMMMMMMMMIV", "MDCCCCII, MCMII", "XXXXVIIII, XLIX",
        // more than once in a row and V/L/D
        "VVVVVVVVV, XLV", "LLLXXXXX, CC", "VVVI, XVI",
        // incorrect subtraction
        "IM, CMXCIX",
        // already minimal & efficient
        "CCXX, CCXX", "XIV, XIV", "CMXCIX, CMXCIX", "MMMMDCLXXII, MMMMDCLXXII", "LI, LI"
    )
    fun `HR solution correct for all input`(input: String, expected: String) {
        assertEquals(expected, tool.getRomanNumber(input))
    }

    @Test
    fun `PE solution correct`() {
        val inputs = getTestResource("src/test/resources/RomanNumerals.txt")
        val expected = 743
        assertEquals(expected, tool.romanCharsSaved(inputs))
    }
}