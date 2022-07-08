package dev.bogwalk.batch3

import dev.bogwalk.util.strings.isPalindrome

/**
 * Problem 36: Double-Base Palindromes
 *
 * https://projecteuler.net/problem=36
 *
 * Goal: Find the sum of all natural numbers (without leading zeroes) less than N that are
 * palindromic in both base 10 and base K.
 *
 * Constraints: 10 <= N <= 1e6, 2 <= K <= 9
 *
 * e.g.: N = 10, K = 2
 *       result = {(1, 1), (3, 11), (5, 101), (7, 111), (9, 1001)}
 *       sum = 25
 */

class DoubleBasePalindromes {
    /**
     * SPEED (WORSE) 39.84s for N = 1e9, K = 2
     */
    fun sumOfPalindromesBrute(n: Int, k: Int): Int {
        return (1 until n).sumOf { num ->
            if (num.toString().isPalindrome() && num.switchBase(k).isPalindrome()) {
                num
            } else 0
        }
    }

    /**
     * In-built function that returns String representation of base 10 to base x conversion.
     */
    private fun Int.switchBase(base: Int) = this.toString(radix=base)

    /**
     * Solution is optimised by only iterating over generated base-k palindromes less than N.
     *
     * This also means that, unlike in the brute force solution, only 1 number (the base-10
     * result) needs to be checked as a palindrome.
     *
     * SPEED (BETTER) 16.45ms for N = 1e9, K = 2
     */
    fun sumOfPalindromes(n: Int, k: Int): Int {
        var sum = 0
        var oddTurn = true
        // generate both odd & even length palindromes
        repeat(2) {
            var i = 1
            do {
                // generate decimal representation of base-k palindrome
                val decimalRepr = getPalindrome(i, k, oddTurn)
                // check if decimal is also a palindrome
                if (decimalRepr.toString().isPalindrome()) {
                    sum += decimalRepr
                }
                i++
            } while (decimalRepr < n)
            oddTurn = false
        }
        return sum
    }

    /**
     * Returns the decimal representation of the nth odd-/even-length palindrome in the
     * specified [base].
     * e.g. The 2nd odd-length base 2 palindrome is 101 == 5.
     */
    private fun getPalindrome(num: Int, base: Int, oddLength: Boolean = true): Int {
        var palindrome = num
        var n = num
        if (oddLength) {
            // base 2 -> n shr 1
            n /= base
        }
        while (n > 0) {
            // base 2 -> palindrome shl 1 + n and 1
            palindrome = palindrome * base + n % base
            // base 2 -> n shr 1
            n /= base
        }
        return palindrome
    }
}