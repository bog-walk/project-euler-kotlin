import util.getPrimeFactors

/**
 * Problem 4: Largest Palindrome Product
 * Goal: Find the largest palindrome less than N, with 101101 < N < 1000000,
 * made from the product of two 3-digit numbers.
 * e.g. The largest palindrome less than 1000000 from the product of two 3-digit
 * numbers (913 * 993) is 906609.
 */

fun Int.isPalindrome(): Boolean {
    val s = this.toString()
    if (s.length == 1) return true
    val mid = s.lastIndex / 2
    val range = if (s.length % 2 == 1) (0 until mid) else (0..mid)
    for (i in range) {
        if (s[i] != s[s.lastIndex - i]) return false
    }
    return true
}

class LargestPalindromeProduct {
    fun getPrevPalindrome(max: Int): Int {
        for (i in max-1 downTo 101101) {
            if (i.isPalindrome()) return i
        }
        return 101101
    }

    fun is3DigProduct(palindrome: Int): Boolean {
        val range = 101..999
        val primeFactors = getPrimeFactors(palindrome)
        val distinctPrimes = primeFactors.distinct()
        val validFactors = distinctPrimes.filter { it in range }.toMutableSet()
        distinctPrimes.forEach { p1 ->
            primeFactors.forEach { p2 ->
                val multiple = if (p1 == p2) 0 else p1 * p2
                if (multiple in range) validFactors.add(multiple)
            }
        }
        return validFactors.any {
            palindrome % it == 0 && (palindrome / it) in range
        }
    }

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
     * one of the integers must have a factor of 11, based on the following analysis:
     * P = 100000x + 10000y + 1000z + 100z + 100y + x
     * P = 11*(9091x + 910y + 100z).
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
                if (product.isPalindrome() && product < max) {
                    largest = product
                }
                y -= deltaY
            }
            x--
        }
        return largest
    }
}