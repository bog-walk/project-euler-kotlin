package dev.bogwalk.batch1

/**
 * Problem 17: Number to Words
 *
 * https://projecteuler.net/problem=17
 *
 * Goal #1: Print the given number N as capitalised words (with/without "And").
 *
 * Goal #2: Count the total number of letters used when the first N positive numbers are
 * converted to words, with "And" used in compliance with British usage.
 *
 * Constraints: 0 <= N <= 1e12
 *
 * e.g.: Goal #1 -> N = 243
 *       output = "Two Hundred And Forty Three"
 *       Goal #2 -> N = 5 -> {"One", "Two", "Three", "Four", "Five"}
 *       output = 19
 */

class NumberToWords {
    private val underTwenty = listOf(
        null, "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
        "Eighteen", "Nineteen"
    )
    private val tens = listOf(
        null, null, "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    )
    private val powersOfTen = listOf(null, "Thousand", "Million", "Billion", "Trillion")

    /**
     * Project Euler specific implementation that sums the amount of letters (excludes whitespace
     * & punctuations) in the written forms of the first N positive numbers.
     */
    fun countFirstNPositives(n: Long): Int {
        return (1L..n).sumOf { num ->
            numberWritten(num).count { it.isLetter() }
        }
    }

    fun numberWritten(number: Long, andIncluded: Boolean = true): String {
        if (number == 0L) return "Zero"
        var n = number
        var words = ""
        var power = 0
        while (n > 0) {
            val modThousand = (n % 1000).toInt()
            if (modThousand > 0L) {
                words = numberUnderThousand(modThousand, andIncluded) +
                        (powersOfTen[power]?.replaceFirstChar { " $it" } ?: "") +
                        if (words.isEmpty()) "" else " $words"
            }
            n /= 1000
            power++
        }
        return words
    }

    private fun numberUnderThousand(n: Int, andIncluded: Boolean): String? {
        return if (n < 100) {
            numberUnderHundred(n)
        } else {
            val extra = if (andIncluded) " And " else " "
            "${underTwenty[n / 100]} Hundred" +
                    (numberUnderHundred(n % 100)?.replaceFirstChar { "$extra$it" } ?: "")
        }
    }

    private fun numberUnderHundred(n: Int): String? {
        return if (n < 20) {
            underTwenty[n]
        } else {
            tens[n / 10] + (underTwenty[n % 10]?.replaceFirstChar { " $it" } ?: "")
        }
    }
}