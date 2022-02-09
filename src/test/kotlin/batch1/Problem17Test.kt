package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.pow
import kotlin.test.Test

internal class NumberToWordsTest {
    private val tool = NumberToWords()

    @ParameterizedTest(name="{0}")
    @CsvSource(
        // single-digit pick
        "0, 'Zero'", "1, 'One'", "9, 'Nine'",
        //double-digit pick
        "12, 'Twelve'", "17, 'Seventeen'", "60, 'Sixty'",
        // double-digit double combo
        "21, 'Twenty One'", "84, 'Eighty Four'",
        // triple-digit double combo
        "200, 'Two Hundred'", "5000, 'Five Thousand'",
        // triple-digit triple combo
        "243, 'Two Hundred Forty Three'",
        // long combos
        "8004792, 'Eight Million Four Thousand Seven Hundred Ninety Two'",
        "10000000010, 'Ten Billion Ten'", "'1010000000', 'One Billion Ten Million'",
        "1000010000, 'One Billion Ten Thousand'",
        "101100000011, 'One Hundred One Billion One Hundred Million Eleven'",
        "104382426112, 'One Hundred Four Billion Three Hundred Eighty Two Million Four Hundred Twenty Six Thousand One Hundred Twelve'"
    )
    fun `numberWritten correct when And not included`(number: Long, expected: String) {
        assertEquals(expected, tool.numberWritten(number, andIncluded = false))
    }

    @Test
    fun `numberWritten correct for powers of ten`() {
        val expected = listOf(
            "Ten", "One Hundred", "One Thousand", "Ten Thousand", "One Hundred Thousand",
            "One Million", "Ten Million", "One Hundred Million", "One Billion"
        )
        for (e in 0..7) {
            val n = (10 * (10.0).pow(e)).toLong()
            assertEquals(expected[e], tool.numberWritten(n, andIncluded = false))
        }
    }

    @ParameterizedTest(name="{0} = {1}")
    @CsvSource(
        // 'and' not needed so should act the same
        "4, 'Four'", "11, 'Eleven'", "66, 'Sixty Six'", "1000000000, 'One Billion'",
        "10000000010, 'Ten Billion Ten'", "1010000000, 'One Billion Ten Million'",
        "1000010000, 'One Billion Ten Thousand'",
        // 'and' should be added
        "243, 'Two Hundred And Forty Three'", "696, 'Six Hundred And Ninety Six'",
        "8004792, 'Eight Million Four Thousand Seven Hundred And Ninety Two'",
        "101100000011, 'One Hundred And One Billion One Hundred Million Eleven'",
        "104382426112, 'One Hundred And Four Billion Three Hundred And Eighty Two Million Four Hundred And Twenty Six Thousand One Hundred And Twelve'"
    )
    fun `numberWritten correct when And included`(number: Long, expected: String) {
        assertEquals(expected, tool.numberWritten(number, andIncluded = true))
    }

    @Test
    fun `PE problem correct`() {
        val n = 1000L
        val expected = 21124
        assertEquals(expected, tool.countFirstNPositives(n))
    }
}