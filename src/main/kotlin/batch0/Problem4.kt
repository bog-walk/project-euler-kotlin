package batch0

import util.maths.primeFactors
import util.strings.isPalindrome
import kotlin.math.sqrt

/**
 * Problem 4: Largest Palindrome Product
 *
 * https://projecteuler.net/problem=4
 *
 * Goal: Find the largest palindrome less than N that is made from the product of two 3-digit
 * numbers.
 *
 * Constraints: 101_101 < N < 1e6
 *
 * e.g.: N = 800_000
 *       869 * 913 = 793_397
 */

class LargestPalindromeProduct {
    /**
     * Brute iteration through all products of 3-digit numbers while storing the largest
     * confirmed palindrome product so far.
     *
     * SPEED (WORST) 2.20ms for N = 999_999
     *
     */
    fun largestPalindromeProductBrute(n: Int): Int {
        var largest = 0
        for (x in 101..999) {
            inner@for (y in x..999) {
                val product = x * y
                if (product >= n) break@inner
                if (product > largest && product.toString().isPalindrome()) {
                    largest = product
                }
            }
        }
        return largest
    }

    /**
     * Brute iteration through all products of 3-digit numbers starting with the largest numbers
     * and terminating the inner loop early if product starts to get too small.
     *
     * SPEED (BETTER) 1.8e+05ns for N = 999_999
     */
    fun largestPalindromeProductBruteBackwards(n: Int): Int {
        var largest = 0
        outer@for (x in 999 downTo 101) {
            inner@for (y in 999 downTo x) {
                val product = x * y
                // combo will be too small to pursue further
                if (product <= largest) continue@outer
                if (product < n && product.toString().isPalindrome()) {
                    largest = product
                    break@inner
                }
            }
        }
        return largest
    }

    /**
     * A palindrome of the product of two 3-digit integers can be at most 6-digits long & one of the
     * integers must have a factor of 11, based on the following algebra:
     *
     *      P = 100_000x + 10_000y + 1000z + 100z + 10y + x
     *
     *      P = 100_001x + 10_010y + 1100z
     *
     *      P = 11*(9091x + 910y + 100z)
     *
     * Rather than stepping down to each palindrome & searching for a valid product, this
     * solution tries all product combos that involve one of the integers being a multiple of 11.
     *
     * SPEED (BETTER) 1.3e+05ns for N = 999_999
     */
    fun largestPalindromeProduct(n: Int): Int {
        var largest = 0
        for (x in 990 downTo 110 step 11) {
            for (y in 999 downTo 101) {
                val product = x * y
                // combo will be too small to pursue further
                if (product <= largest) break
                if (product < n && product.toString().isPalindrome()) {
                    largest = product
                    break
                }
            }
        }
        return largest
    }

    /**
     * Brute iteration through all palindromes less than [n] checks if each palindrome could
     * be a valid product of two 3-digit numbers.
     *
     * SPEED (BEST) 1.9e+04ns for N = 999_999 [using fast helper function]
     */
    fun largestPalindromeProductAlt(n: Int): Int {
        var num = if ((n / 1000).toPalindrome() > n) n / 1000 - 1 else n / 1000
        while (num > 101) {
            val palindrome = num.toPalindrome()
            if (palindrome.is3DigProductFast()) {
                return palindrome
            }
            num--
        }
        return 101_101
    }

    /**
     * Converts a 3-digit integer to a 6-digit palindrome.
     */
    private fun Int.toPalindrome(): Int {
        return this * 1000 + this % 10 * 100 + this / 10 % 10 * 10 + this / 100
    }

    private fun Int.is3DigProductFast(): Boolean {
        for (factor1 in 999 downTo sqrt(1.0 * this).toInt()) {
            if (this % factor1 == 0 && this / factor1 in 101..999) return true
        }
        return false
    }

    /**
     * Determines if [this], an Int palindrome, can be a product of two 3-digit numbers by finding
     * all 3-digit multiples of [this]'s prime factors.
     */
    private fun Int.is3DigProductSlow(): Boolean {
        val range = 101..999
        val distinctPrimes: Set<Long>
        val primeFactors: List<Long> = primeFactors(this.toLong()).also {
            distinctPrimes = it.keys
        }.flatMap { (k, v) ->
            List(v) { k }
        }
        val validFactors = distinctPrimes.filter { it in range }
        for (p1 in distinctPrimes) {
            inner@for (p2 in primeFactors) {
                if (p2 <= p1) continue@inner
                val multiple = p1 * p2
                if (multiple in range) {
                    if (this / multiple in range) {
                        return true
                    }
                }
            }
        }
        return validFactors.any {
            this / it in range
        }
    }
}