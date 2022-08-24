package dev.bogwalk.batch8

/**
 * Problem 89: Roman Numerals
 *
 * https://projecteuler.net/problem=89
 *
 * Goal: Given a string of Roman number symbols, output a valid (or more efficient) Roman number
 * that represents it, by following the rules below.
 *
 * Constraints: 1 <= length of input string <= 1000
 *
 * Roman Numerals: {I: 1, V: 5, X: 10, L: 50, C: 100, D: 500, M: 1000}
 *
 * Roman numbers are written in descending order of symbols to be added, with the appearance of a
 * symbol with lesser value before another symbol implying subtraction. This is why 14 is written
 * as XIV, not IVX.
 *
 * Notably, M is the only symbol that can appear more than 3 times in a row. This is why 9 is
 * written as IX and not VIII. Also, V, L, and D will not appear more than once.
 *
 * Subtraction Rules:
 *      - I can only be subtracted from V and X.
 *      - X can only be subtracted from L and C.
 *      - C can only be subtracted from D and M.
 *      - V, L, D, and M cannot be subtracted from another symbol.
 *      - Only one I, X, and C can be used as the leading numeral in part of a subtractive pair.
 * e.g. 999 is written as CMXCIX, not IM.
 *
 * e.g.: input = "IIIII"
 *       output = "V"
 *       input = "VVVVVVVVV"
 *       output = "XLV"
 */

class RomanNumerals {
    private val romanSymbols = mapOf(
        "M" to 1000, "CM" to 900, "D" to 500, "CD" to 400, "C" to 100, "XC" to 90, "L" to 50,
        "XL" to 40, "X" to 10, "IX" to 9, "V" to 5, "IV" to 4, "I" to 1
    )

    fun getRomanNumber(input: String) = parseInput(input).toRomanNumber()

    /**
     * Creates an integer from a string representation of a Roman Number by iterating through
     * every character and looking forward to see if the appropriate value must be added or
     * subtracted.
     *
     * This could be solved in the opposite way that Int.toRomanNumber() works, by iterating
     * through an alternative map sorted from the lowest value to highest (but with subtractive
     * pairs first). Every symbol could be removed from the original string & resulting lengths
     * compared to create an incrementing numerical value.
     */
    private fun parseInput(input: String): Int {
        var value = 0

        for ((i, ch) in input.withIndex()) {
            val current = romanSymbols.getOrDefault(ch.toString(), 0)
            value = if (i == input.lastIndex) {
                value + current
            } else {
                val next = romanSymbols.getOrDefault(input[i+1].toString(), 0)
                if (next <= current) {
                    value + current
                } else {
                    value - current
                }
            }
        }

        return value
    }

    /**
     * Creates a Roman Number by repeatedly dividing out all mapped symbols from the original
     * number.
     */
    private fun Int.toRomanNumber(): String {
        val romanNumeral = StringBuilder()
        var num = this

        for ((symbol, value) in romanSymbols) {
            if (num == 0 || symbol == "I") break

            romanNumeral.append(symbol.repeat(num / value))
            num %= value
        }

        romanNumeral.append("I".repeat(num))

        return romanNumeral.toString()
    }

    /**
     * Project Euler specific implementation that returns the number of characters saved by
     * writing all 1000 input strings in their more efficient minimal form.
     *
     * Note that test resource input strings are not guaranteed to have symbols in descending
     * value order.
     */
    fun romanCharsSaved(inputs: List<String>): Int {
        return inputs.sumOf { input ->
            // this should never be a negative value
            input.length - getRomanNumber(input).length
        }
    }
}