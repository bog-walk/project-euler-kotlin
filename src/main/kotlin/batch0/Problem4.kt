package batch0

import util.maths.primeFactors
import util.strings.isPalindrome

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
     * Brute iteration through all palindromes less than [n] checks if each palindrome found could
     * be a valid product of two 3-digit numbers.
     *
     * SPEED (WORSE) 102.00ms for N = 1e6
     */
    fun largestPalindromeProductBrute(n: Int): Int {
        var largest = 101_101
        for (num in n - 1 downTo 101_102) {
            if (num.toString().isPalindrome() && num.is3DigProduct()) {
                largest = num
                break
            }
        }
        return largest
    }

    /**
     * Determines if [this], an Int palindrome, can be a product of two 3-digit numbers by finding
     * all 3-digit multiples of [this]'s prime factors.
     */
    private fun Int.is3DigProduct(): Boolean {
        val range = 101..999
        var distinctPrimes: Set<Long>
        val primeFactors: List<Long> = primeFactors(this.toLong()).also{
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

    /**
     * A palindrome of the product of two 3-digit integers must be 6-digits long & one of the
     * integers must have a factor of 11, based on the following algebra:
     *
     * P = 100_000x + 10_000y + 1000z + 100z + 100y + x
     *
     * P = 100_001x + 10_010y + 1100z
     *
     * P = 11*(9091x + 910y + 100z)
     *
     * Rather than stepping down to each palindrome & searching for a valid product, this
     * solution tries all product combos that involve one of the integers being a multiple of 11.
     *
     * SPEED (BETTER) 1.46ms for N = 1e6
     */
    fun largestPalindromeProduct(n: Int): Int {
        var largest = 0
        var x = 999
        while (x > 100) {
            var (y, deltaY) = if (x % 11 == 0) 999 to 1 else 990 to 11
            inner@while (y >= x) {
                val product = y * x
                // combo will be too small to pursue further
                if (product <= largest) break@inner
                if (product < n && product.toString().isPalindrome()) {
                    largest = product
                }
                y -= deltaY
            }
            x--
        }
        return largest
    }
}