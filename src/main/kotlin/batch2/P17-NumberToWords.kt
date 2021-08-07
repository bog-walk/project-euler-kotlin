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

    fun numberWritten(n: Long, andIncluded: Boolean = false): String {
        var digit = 1
        val digits = n.toString().map(Char::digitToInt)
        val words = mutableListOf<String>()
        do {
            when {
                digit % 3 == 0 -> {
                    words.add("And")
                    if (digits[digit - 1] != 0) {
                        words.add("Hundred")
                        words.add(baseNumbers.getValue(digits[digit - 1]))
                    }
                }
                digit == 4 -> {
                    words.add("Thousand")
                }
                digit == 7 -> {
                    words.add("Million")
                }
                digit == 10 -> {
                    words.add("Billion")
                }
                else -> {
                    if (digits[digit] == 1) {
                        words.add(baseNumbers.getValue(digits[digit - 1] + 10))
                    } else {
                        words.add(baseNumbers.getValue(digits[digit]* 10))
                        if (digits[digit - 1] != 0) {
                            words.add(baseNumbers.getValue(digits[digit - 1]))
                        }
                    }
                    digit++
                }
            }
            digit++
        } while (digit <= digits.size)
        return words.reversed().joinToString(" ")
    }

    fun countLetters(number: String): Int = number.count { it.isLetter() }
}