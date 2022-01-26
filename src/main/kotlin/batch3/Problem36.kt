package batch3

import util.isPalindrome

/**
 * Problem 36: Double-Base Palindromes
 *
 * https://projecteuler.net/problem=36
 *
 * Goal: Find the sum of all natural numbers (without leading zeroes)
 * less than N that are palindromic in both base 10 and base K.
 *
 * Constraints: 10 <= N <= 1e6 & 2 <= K <= 9
 *
 * e.g.: N = 10, K = 2
 *       result = {(1, 1), (3, 11), (5, 101), (7, 111), (9, 1001)}
 *       sum = 25
 */

class DoubleBasePalindromes {

    /**
     * In-built function that returns base 10 to base x conversion as a String.
     */
    private fun Int.switchBase(base: Int) = this.toString(radix=base)

    /**
     * Naive/Exhaustive iterative approach.
     *
     * SPEED: 69.6194s for N = 1e9, K = 2
     */
    fun sumOfPalindromesBrute(n: Int, k: Int): Int {
        return (1 until n).sumOf { num ->
            if (num.toString().isPalindrome() && num.switchBase(k).isPalindrome()) {
                num
            } else 0
        }
    }

    /**
     * Generates the nth palindrome in the given base.
     * e.g. The 2nd odd-length base 2 palindrome is 101 == 5.
     *
     * @return  Decimal representation of the nth odd-/even- length
     * palindrome in the specified base.
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

    /**
     * Using the helper method above avoids the need to iterate through every
     * natural number less than N. Instead, loop elements are reduced to only
     * generated base-k palindromes less than N. This also means that only 1
     * number needs to be checked as a palindrome (the base-10 result).
     *
     * SPEED (BEST): 0.0202s for N = 1e9, K = 2
     */
    fun sumOfPalindromes(n: Int, k: Int): Int {
        var sum = 0
        var oddTurn = true
        // generate both odd & even length palindromes
        repeat(2) {
            var i = 1
            do {
                // generate decimal repr of base-k palindrome
                val palindrome = getPalindrome(i, k, oddTurn)
                // check if decimal is also a palindrome
                if (palindrome.toString().isPalindrome()) {
                    sum += palindrome
                }
                i++
            } while (palindrome < n)
            oddTurn = false
        }
        return sum
    }
}