package batch5

import util.isPrime

/**
 * Problem 41: Pandigital Prime
 *
 * https://projecteuler.net/problem=41
 *
 * Goal: Find the largest pandigital prime <= N or return -1 if none exists.
 *
 * Constraints: 10 <= N < 1e10
 *
 * e.g.: N = 100
 *       return -1, as no 2-digit pandigital primes exist; however,
 *       N = 10_000
 *       return 4231, as the smallest pandigital prime.
 */

class PandigitalPrime {
    /**
     * Project Euler specific implementation that returns the largest
     * n-digit pandigital prime that exists. This solution checks primality of all
     * numbers starting from 9 digits backwards til the first eligible number is found.
     */
    fun largestPandigitalPrimePE(): Int {
        var n = 9
        val digits = ('9' downTo '1').toMutableList()
        var largest = 0
        outer@while (n > 3) {
            if (n == 7) println("Now at 7 digits")
            println("Digits=$digits")
            val perms = getPermutations(digits, digits.size).sortedDescending()
            println("7652413 is a perm ${"7652413" in perms}")
            for (perm in perms) {
                if (perm == "7652413") println("Found result")
                if (isPrime(perm.toInt())) {
                    largest = perm.toInt()
                    break@outer
                }
            }
            digits.remove('0' + n--)
        }
        return largest
    }

    /**
     * Solution optimised based on the following:
     *
     * - The smallest pandigital prime is 4-digits -> 1423.
     *
     * - The largest pandigital prime is 7-digits -> 7652413.
     *
     * - The above 2 bounds (proven with PY permutations & above brute method)
     * confirms that only 4- & 7-digit pandigitals can be prime numbers as all
     * primes greater than 3 are of the form 6*p(+/- 1) & so cannot be multiples of 3.
     * If the sum of a pandigital's digits is a multiple of 3, then that number
     * will be a multiple of 3 & thereby not a prime. Only 4- & 7-digit pandigitals
     * have sums that are not divisible by 3 (10 & 28 respectively).
     *
     * - Prime numbers (> 2) are all odd.
     *
     * @return  List of all pandigital primes in descending order.
     */
    fun allPandigitalPrimes(): List<Int> {
        val pandigitalPrimes = mutableListOf<Int>()
        val digits = ('7' downTo '1').toMutableList()
        repeat(2) {
            for (perm in getPermutations(digits, digits.size)) {
                if (isPrime(perm.toInt())) {
                    pandigitalPrimes.add(perm.toInt())
                }
            }
            digits.removeIf { it > '4' }
        }
        return pandigitalPrimes.sortedDescending()
    }

    fun largestPandigitalPrimeHR(n: Long): Int {
        if (n < 1423L) return -1
        return allPandigitalPrimes()
            .asSequence()
            .filter { it.toLong() <= n }
            .first()
    }

    fun getPermutations(
        chars: MutableList<Char>,
        size: Int,
        perms: MutableList<String> = mutableListOf()
    ): List<String> {
        if (size == 1) {
            perms.add(chars.joinToString(""))
        }
        repeat(size) { i ->
            getPermutations(chars, size - 1, perms)
            if (size % 2 == 1) {
                val swap = chars.first()
                chars[0] = chars[size - 1]
                chars[size - 1] = swap
            } else {
                val swap = chars[i]
                chars[i] = chars[size - 1]
                chars[size - 1] = swap
            }
        }
        return perms
    }
}