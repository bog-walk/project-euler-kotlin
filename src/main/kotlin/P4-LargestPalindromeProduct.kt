import util.getPrimeFactors

/**
 * Problem 4: Largest Palindrome Product
 * Goal: Find the largest palindrome less than N, with 101101 < N < 1000000,
 * made from the product of two 3-digit numbers.
 * e.g. The largest palindrome from the product of two 3-digit
 * numbers (91 * 99) is 9009.
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
}