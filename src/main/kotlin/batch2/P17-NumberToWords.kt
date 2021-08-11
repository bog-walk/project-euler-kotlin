package batch2

/**
 * Problem 17: Number to Words
 * Goal: Given a number N, such that 0 <= N <= 10^12, write
 * the number in words (each word capitalised).
 * e.g. 243 = Two Hundred Forty Three.
 */

class NumberToWords {
    private val baseNumbers = mapOf(
        0 to "Zero", 1 to "One", 2 to "Two", 3 to "Three", 4 to "Four", 5 to "Five",
        6 to "Six", 7 to "Seven", 8 to "Eight", 9 to "Nine", 10 to "Ten", 11 to "Eleven",
        12 to "Twelve", 13 to "Thirteen", 14 to "Fourteen", 15 to "Fifteen", 16 to "Sixteen",
        17 to "Seventeen", 18 to "Eighteen", 19 to "Nineteen",
        20 to "Twenty", 30 to "Thirty", 40 to "Forty", 50 to "Fifty", 60 to "Sixty",
        70 to "Seventy", 80 to "Eighty", 90 to "Ninety"
    )
    private val multiplesOfTen = listOf("Hundred", "Thousand", "Million", "Billion")

    fun numberWritten(n: Long, andIncluded: Boolean = false): String {
        val digits = n.toString().map(Char::digitToInt).reversed()
        var digit = 1
        var hundreds = 0
        val words = mutableListOf<String>()
        do {
            val nextDigit = digits[digit - 1]
            when {
                nextDigit == 0 -> {
                    if (digits.size == 1) {
                        words.add(baseNumbers.getValue(nextDigit))
                    }
                }
                digit % 3 == 0 -> {
                    if (andIncluded && words.isNotEmpty()) words.add("And")
                    if (nextDigit != 0) {
                        words.add(multiplesOfTen[0])
                        words.add(baseNumbers.getValue(nextDigit))
                    }
                }
                digit % 3 == 1 -> {
                    if (hundreds > 0) words.add(multiplesOfTen[hundreds])
                    words.add(baseNumbers.getValue(nextDigit))
                }
                else -> {
                    if (nextDigit == 1) {
                        if (words.isNotEmpty()) words.removeLast()
                        words.add(baseNumbers.getValue(digits[digit - 2] + 10))
                    } else {
                        words.add(baseNumbers.getValue(nextDigit * 10))
                    }
                }
            }
            digit++
            if (digit % 3 == 1) hundreds++
        } while (digit <= digits.size)
        return words.reversed().joinToString(" ")
    }

    fun countLetters(number: String): Int = number.count { it.isLetter() }
}

fun main() {
    val tool = NumberToWords()
    var count = 0
    for (i in 1..1000) {
        val numAsString = tool.numberWritten(1L * i, true)
        count += tool.countLetters(numAsString)
    }
    println(count)
}