package batch5

import util.strings.isPalindrome

/**
 * Problem 55: Lychrel Numbers
 *
 * https://projecteuler.net/problem=55
 *
 * Goal: Given N, find the palindrome to which the maximum positive numbers <= N converge if
 * non-Lychrel and return both the palindrome and the maxCount.
 *
 * Constraints: 100 <= N <= 1e5
 *
 * Lychrel Number: A number that, theoretically, never produces a palindrome through a reverse
 * and add process, known as the 196 Algorithm (2-digit minimum).
 * e.g. 349 + 943 = 1292
 *      1292 + 2921 = 4213
 *      4213 + 3124 = 7337, a palindrome in 3 iterations.
 *
 * So far, 10677 is the 1st number shown to require over 50 iterations to produce a palindrome
 * (in 53 iterations it produces a 28-digit palindrome). All numbers less than 10677 will either
 * become a palindrome in under 50 iterations or have not been proven to not be Lychrel, e.g. 196.
 *
 * Note that palindromic numbers can themselves be Lychrel numbers, e.g. 4994, but, for this
 * problem, it is assumed that palindromes are non-Lychrel in the 0th iteration.
 *
 * e.g.: N = 130
 *       palindrome = 121
 *       18 numbers <= 121 converge ->
 *       [19, 28, 29, 37, 38, 46, 47, 56, 64, 65, 73, 74, 82, 83, 91, 92, 110, 121]
 */

class LychrelNumbers {
    /**
     * Solution caches all numbers in [1, N], regardless if they are Lychrel numbers or not, to
     * avoid re-iterating over them. Converged-upon palindromes are stored in a dictionary as
     * keys with the amount of converging positive integers as values.
     *
     * N.B. HackerRank specific implementation pushes upper constraints to 1e5, so the amount of
     * iterations to surpass to be a Lychrel number becomes 60.
     *
     * SPEED (WORSE) 1.35s for N = 1e5
     * The cache grows to contain 99_990 elements, so searching through and performing set union
     * under performs simply doing the same arithmetic and palindrome check for every n,
     * regardless of repetitions. A binary search through the cache did nothing to improve
     * performance.
     *
     * @return pair of (palindrome to which maximum positive integers converge, the latter
     * maximum count).
     */
    fun maxPalindromeConvergenceCached(n: Int): Pair<String, Int> {
        val nBI = n.toBigInteger()
        val palindromes = mutableMapOf<String, Int>()
        val visited = mutableSetOf<Int>()
        val limit = if (n < 10677) 50 else 60
        for (i in 11..n) {
            if (i in visited) continue
            var num = i.toBigInteger()
            val nums = mutableSetOf<Int>()
            for (j in 0 until limit) {
                if (num <= nBI) nums.add(num.intValueExact())
                val numString = num.toString()
                if (numString.isPalindrome()) {
                    val newNums = nums - visited
                    // ignore 0th iteration palindromes, e.g. 55
                    if (j > 0) {
                        palindromes[numString] = palindromes.getOrDefault(numString, 0) +
                                newNums.size
                    }
                    break
                }
                val reverseNum = numString.reversed()
                if (reverseNum.toBigInteger() <= nBI && reverseNum.first() > '0') {
                    nums.add(reverseNum.toInt())
                }
                num += reverseNum.toBigInteger()
            }
            // cache both lychrel & non-lychrel numbers assessed
            visited += nums
        }
        val palindrome = palindromes.maxByOrNull { (_, v) -> v }!!
        return palindrome.key to palindrome.value
    }

    /**
     * Solution is identical to the one above, but is optimised by simply not using a cache to
     * reduce iterations (explained in speed section above).
     *
     * N.B. HackerRank specific implementation pushes upper constraints to 1e5, so the amount of
     * iterations to surpass to be a Lychrel number becomes 60.
     *
     * SPEED (BETTER) 931.63ms for N = 1e5
     *
     * @return pair of (palindrome to which maximum positive integers converge, the latter
     * maximum count).
     */
    fun maxPalindromeConvergence(n: Int): Pair<String, Int> {
        val palindromes = mutableMapOf<String, Int>()
        val limit = if (n < 10677) 50 else 60
        for (i in 11..n) {
            var num = i.toBigInteger()
            for (j in 0 until limit) {
                val numString = num.toString()
                if (numString.isPalindrome()) {
                    palindromes[numString] = palindromes.getOrDefault(numString, 0) + 1
                    break
                }
                num += numString.reversed().toBigInteger()
            }
        }
        val palindrome = palindromes.maxByOrNull { (_, v) -> v }!!
        return palindrome.key to palindrome.value
    }

    /**
     * Project Euler specific implementation that counts Lychrel numbers < 1e4.
     *
     * Storing visited numbers in a set that is checked before cycling through iterations
     * prevents unnecessary steps. e.g. If 19 converges to a palindrome, so will 91.
     */
    fun countLychrelNumbers(): Int {
        val limit = 10_000
        val limitBI = limit.toBigInteger()
        var count = 0
        val visited = mutableSetOf<Int>()
        nextN@for (n in 1 until limit) {
            if (n in visited) continue
            var num = n.toBigInteger()
            val nums = mutableSetOf<Int>()
            for (i in 0 until 50) {
                if (num <= limitBI) nums.add(num.intValueExact())
                val reverseNum = num.toString().reversed()
                val reverseBI = reverseNum.toBigInteger()
                if (reverseBI <= limitBI && reverseNum.first() > '0') {
                    nums.add(reverseBI.intValueExact())
                }
                num += reverseBI
                if (num.toString().isPalindrome()) {
                    visited += nums
                    continue@nextN
                }
            }
            count++
        }
        return count
    }
}