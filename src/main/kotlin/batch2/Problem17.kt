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
    //private val powersOfTen = listOf("Hundred", "Thousand", "Million", "Billion", "Trillion")

    private val underTwenty = listOf(
        null, "One", "Two", "Three", "Four", "Five","Six", "Seven",
        "Eight", "Nine", "Ten", "Eleven","Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen"
    )
    private val twentyUp = listOf(
        null, null, "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    )
    private val powersOfTen = listOf(null, "Thousand", "Million", "Billion", "Trillion")

    fun numberWrittenImproved(number: Long): String {
        if (number == 0L) return "Zero"
        var n = number
        var words = ""
        var power = 0
        while (n > 0) {
            val modThousand = n % 1000
            if (modThousand != 0L) {
                words = numberUnderThousand(modThousand) +
                        (powersOfTen[power]?.replaceFirstChar { " $it" } ?: "") +
                        if (words.isEmpty()) "" else " $words"
            }
            n /= 1000
            power++
        }
        return words
    }

    private fun numberUnderHundred(n: Int): String? {
        return when {
            n == 0 -> null
            n < 20 -> underTwenty[n]
            else -> twentyUp[n / 10] +
                    (underTwenty[n % 10]?.replaceFirstChar { " $it" } ?: "")
        }
    }

    private fun numberUnderThousand(number: Long): String? {
        val n = number.toInt()
        return when {
            n == 0 -> null
            n < 100 -> numberUnderHundred(n)
            else -> "${underTwenty[n / 100]} Hundred" +
                    (numberUnderHundred(n % 100)?.replaceFirstChar { " $it" } ?: "")
        }
    }

    fun numberWritten(n: String, andIncluded: Boolean = false): String {
        val digits = n.map(Char::digitToInt).reversed()
        var digit = 1
        var hundreds = 0
        val words = mutableListOf<String>()
        do {
            val nextDigit = digits[digit - 1]
            when {
                digit % 3 == 0 -> {
                    if (andIncluded && words.isNotEmpty()
                        && words.last() !in powersOfTen) words.add("And")
                    if (nextDigit != 0) {
                        //words.add(powersOfTen[0])
                        words.add(baseNumbers.getValue(nextDigit))
                    }
                }
                digit % 3 == 1 -> {
                    if (hundreds > 0) {
                        if (words.isNotEmpty() && words.last() in powersOfTen) {
                            words.removeLast()
                        }
                        //words.add(powersOfTen[hundreds])
                    }
                    words.add(baseNumbers.getValue(nextDigit))
                }
                else -> {
                    if (words.last() == "Zero" || nextDigit == 1) words.removeLast()
                    if (nextDigit == 1) {
                        words.add(baseNumbers.getValue(digits[digit - 2] + 10))
                    } else if (nextDigit > 1) {
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