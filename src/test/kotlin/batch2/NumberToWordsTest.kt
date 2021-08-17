package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class NumberToWordsTest {
    @ParameterizedTest(name="{0} = {1}")
    @CsvSource(
        // single digit pick
        "'0', 'Zero'", "'1', 'One'", "'9', 'Nine'",
        //double digit single pick
        "'10', 'Ten'", "'17', 'Seventeen'", "'60', 'Sixty'",
        // double digit double combo
        "'21', 'Twenty One'", "'84', 'Eighty Four'",
        // triple digit double combo
        "'100', 'One Hundred'", "'5000', 'Five Thousand'",
        // triple digit triple combo
        "'243', 'Two Hundred Forty Three'",
        // powers of ten
        "'1000', 'One Thousand'", "'10000', 'Ten Thousand'", "'100000', 'One Hundred Thousand'",
        "'1000000', 'One Million'", "'10000000', 'Ten Million'", "'100000000', 'One Hundred Million'",
        "'1000000000', 'One Billion'",
        // long combos
        "'8004792', 'Eight Million Four Thousand Seven Hundred Ninety Two'",
        "'10000000010', 'Ten Billion Ten'", "'1010000000', 'One Billion Ten Million'",
        "'1000010000', 'One Billion Ten Thousand'",
        "'101100000011', 'One Hundred One Billion One Hundred Million Eleven'",
        "'104382426112', 'One Hundred Four Billion Three Hundred Eighty Two Million Four Hundred Twenty Six Thousand One Hundred Twelve'"
    )
    fun testNumberWritten_withoutAnd(number: String, expected: String) {
        val tool = NumberToWords()
        //assertEquals(expected, tool.numberWritten(number))
        assertEquals(expected, tool.numberWrittenImproved(number.toLong()))
    }

    @ParameterizedTest(name="{0} = {1}")
    @CsvSource(
        // 'and' not needed but should act the same
        "'4', 'Four'", "'11', 'Eleven'", "'66', 'Sixty Six'", "'1000000000', 'One Billion'",
        // 'and' should be added
        "'243', 'Two Hundred And Forty Three'",
        "'10000000010', 'Ten Billion And Ten'",
        "'8004792', 'Eight Million And Four Thousand Seven Hundred And Ninety Two'",
        "'104382426112', 'One Hundred And Four Billion Three Hundred And Eighty Two Million Four Hundred And Twenty Six Thousand One Hundred And Twelve'"
    )
    fun testNumberWritten_withAnd(number: String, expected: String) {
        val tool = NumberToWords()
        assertEquals(expected, tool.numberWritten(number, andIncluded = true))
    }

    @ParameterizedTest(name="{0} has {1} letters")
    @CsvSource(
        "'Four', 4",
        // whitespace not counted
        "'One Hundred And Fifteen', 20",
        // hyphens not counted
        "'Three Hundred And Forty-Two', 23"
    )
    fun testCountLetters(number: String, expected: Int) {
        val tool = NumberToWords()
        assertEquals(expected, tool.countLetters(number))
    }

    @Test
    fun testCountFirstThousandPositives() {
        val tool = NumberToWords()
        var count = 0
        for (i in 1..1000) {
            val numAsString = tool.numberWritten(i.toString(), true)
            count += tool.countLetters(numAsString)
        }
        assertEquals(21124, count)
    }
}