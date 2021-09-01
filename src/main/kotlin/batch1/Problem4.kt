package batch1

import util.primeFactors

/**
 * Problem 4: Largest Palindrome Product
 *
 * https://projecteuler.net/problem=4
 *
 * Goal: Find the largest palindrome less than N that is made from the
 * product of two 3-digit numbers.
 *
 * Constraints: 101101 < N < 1000000
 *
 * e.g.: N = 800000
 *       869 * 913 = 793397
 */

class LargestPalindromeProduct {

    fun isPalindrome(num: Int): Boolean {
        val s = num.toString()
        if (s.length == 1) return true
        val mid = s.lastIndex / 2
        val range = if (s.length % 2 == 1) (0 until mid) else (0..mid)
        for (i in range) {
            if (s[i] != s[s.lastIndex - i]) return false
        }
        return true
    }

    /**
     * Returns first number less than given max that is a palindrome.
     * 101101 is the smallest 6-digit palindrome that is a product of two
     * 3-digit numbers. The next palindrome would be 100001 (not a product
     * of two 3-digit numbers).
     */
    fun getPrevPalindrome(max: Int): Int {
        for (i in max-1 downTo 101102) {
            if (isPalindrome(i)) return i
        }
        return 101101
    }

    /**
     * Determines if palindrome can be a product of two 3-digit numbers by
     * finding all 3-digit multiples of the palindrome's prime factors.
     */
    fun is3DigProduct(palindrome: Int): Boolean {
        val range = 101..999
        var distinctPrimes: Set<Long>
        val primeFactors: List<Long> = primeFactors(palindrome.toLong()).also{
            distinctPrimes = it.keys
        }.flatMap { (k, v) ->
            List(v) { k }
        }
        val validFactors = distinctPrimes.filter { it in range }
        distinctPrimes.forEach { p1 ->
            primeFactors.forEach { p2 ->
                if (p1 != p2) {
                    val multiple = p1 * p2
                    if (multiple in range) {
                        if (palindrome / multiple in range) {
                            return true
                        }
                    }
                }
            }
        }
        return validFactors.any {
            palindrome / it in range
        }
    }

    /**
     * Find successive palindromes less than provided max & check if
     * it could be a valid product of two 3-digit numbers.
     */
    fun largestPalindromeProduct(max: Int): Int {
        var target = max
        while (target > 101101) {
            val prevPalindrome = getPrevPalindrome(target)
            if (is3DigProduct(prevPalindrome)) {
                return prevPalindrome
            } else {
                target = prevPalindrome
            }
        }
        return 101101
    }

    /**
     * A palindrome of the product of two 3-digit integers must be 6-digits long &
     * one of the integers must have a factor of 11, based on the following algebra:
     * Palindrome = 100000x + 10000y + 1000z + 100z + 100y + x;
     * Palindrome = 11 * (9091x + 910y + 100z).
     * Rather than stepping down to each palindrome & searching for a valid product,
     * this solution tries all product combos that involve one of the integers
     * being a multiple of 11.
     */
    fun largestPalindromeProductCountingDown(max: Int): Int {
        var largest = 0
        var x = 999
        while (x > 100) {
            var y = if (x % 11 == 0) 999 else 990
            val deltaY = if (x % 11 == 0) 1 else 11
            inner@while (y >= x) {
                val product = y * x
                // Combo will be too small to pursue further
                if (product <= largest) break@inner
                if (product < max && isPalindrome(product)) {
                    largest = product
                }
                y -= deltaY
            }
            x--
        }
        return largest
    }
}