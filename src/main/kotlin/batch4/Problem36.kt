package batch4

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
     * In-built alternative to manual implementation in Problem 4.
     */
    private fun String.isPalindrome() = this == this.reversed()

    /**
     * In-built function that returns base 10 to base x conversion as a String.
     */
    private fun Int.switchBase(base: Int) = this.toString(radix=base)

    /**
     * Naive/Exhaustive iterative approach.
     *
     * SPEED: 69.6194s for N = 1e9, K = 2
     */
    fun sumOfPalindromes(n: Int, k: Int): Int {
        return (1 until n).sumOf { num ->
            if (num.toString().isPalindrome() && num.switchBase(k).isPalindrome()) {
                num
            } else 0
        }
    }

    /**
     * Using this helper method avoids the need to iterate through every natural number
     * less than N, and instead generates fewer palindromes in the given base that
     * only need to be checked as a palindrome in the other given base.
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
     * Improved solution increases performance by reducing loop elements
     * to only palindromes generated less than N.
     *
     * SPEED (BEST): 0.0202s for N = 1e9, K = 2
     */
    fun sumOfPalindromesImproved(n: Int, k: Int): Int {
        var sum = 0
        var oddTurn = true
        // Perform for both even and odd length palindromes
        repeat(2) {
            var i = 1
            do {
                val palindrome = getPalindrome(i, k, oddTurn)
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