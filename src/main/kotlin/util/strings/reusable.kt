package util.strings

/**
 * SPEED (WORST) 11.62ms for 18-digit N
 */
fun String.isPalindromeBuiltIn() = this == this.reversed()

/**
 * SPEED (BETTER) 3.16ms for 18-digit N
 */
fun String.isPalindromeManual(): Boolean {
    if (length == 1) return true
    val mid = lastIndex / 2
    val range = if (length % 2 == 1) (0 until mid) else (0..mid)
    for (i in range) {
        if (this[i] != this[lastIndex - i]) return false
    }
    return true
}

/**
 * SPEED (BETTER) 49900ns for 18-digit N
 */
fun String.isPalindromeNumber(): Boolean {
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
 * This recursive version will be used in future solutions.
 *
 * SPEED (BEST) 46000ns for 18-digit N
 */
fun String.isPalindrome(): Boolean {
    return when {
        length < 2 -> true
        first() == last() -> substring(1, lastIndex).isPalindrome()
        else -> false
    }
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