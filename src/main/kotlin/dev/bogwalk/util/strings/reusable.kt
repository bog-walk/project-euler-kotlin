package dev.bogwalk.util.strings

import dev.bogwalk.util.maths.log10
import java.math.BigInteger

/**
 * Counts the number of digits in a BigInteger using that number's base 10 logarithm, by
 * incrementing the characteristic/integer portion of the floored logarithm.
 *
 * This method is faster than simply using BigInteger.toString().length, as shown when a
 * 500-digit number is provided, resulting in a 7.3e4ns performance instead of 4.56ms with the
 * string cast method.
 *
 * N.B. This method originally failed for values above 1e14 that consist of only the digit 9. e.g.
 * if n = 1e19 - 1 (i.e. 19 9's), log10(n) = 19.0, so this method would return 20 as the digit
 * count, even though the expected answer is 19. Comparing the BigInteger value to what should be
 * its upper limit based on the returned logarithm prevents these failures.
 */
fun BigInteger.digitCount(): Int {
    if (this == BigInteger.ZERO) return 1
    val digits = log10().toInt() + 1
    return if (BigInteger.TEN.pow(digits - 1) <= this) digits else digits - 1
}

/**
 * SPEED (WORST) 5548ns for 19-digit N
 */
private fun String.isPalindromeBuiltIn() = this == this.reversed()

/**
 * SPEED (BETTER) 1458ns for 19-digit N
 */
private fun String.isPalindromeNumber(): Boolean {
    if (length == 1) return true
    var num = this.toLong()
    var rev = 0L
    while (num > 0L) {
        rev = rev * 10L + num % 10L
        num /= 10L
    }
    return this == rev.toString()
}

/**
 * SPEED (BETTER) 2931ns for 19-digit N
 */
private fun String.isPalindromeRecursive(): Boolean {
    return when {
        length < 2 -> true
        first() != last() -> false
        else -> substring(1, lastIndex).isPalindromeRecursive()
    }
}

/**
 * This manual version will be used in future solutions.
 *
 * SPEED (BEST) 573ns for 19-digit N
 */
fun String.isPalindrome(): Boolean {
    for (i in 0 until length / 2) {
        if (this[i] != this[lastIndex - i]) return false
    }
    return true
}

/**
 * Checks if [this] contains all digits between 1 and [n] inclusive.
 *
 * While its default argument clears all leading/trailing whitespace, trim(*chars) removes
 * characters from the left & right of [this] until none in the argument match the
 * left-/right-most character.
 *
 * This provides a trick to check whether a string contains only characters in another string.
 * e.g. "1234".trim("4231") == "".
 */
fun String.isPandigital(n: Int): Boolean {
    val digits = "1234567890".substring(0 until n)
    return length == n && digits.trim(*this.toCharArray()).isEmpty()
}