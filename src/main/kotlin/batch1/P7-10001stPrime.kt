import util.isPrime

/**
 * Problem 7: 10001st Prime
 * Goal: Find the Nth prime number, where 1 <= N <= 10000
 * e.g. The 6th prime is 13.
 */

class The10001stPrime {
    fun getNthPrime(n: Int): Int {
        if (n == 1) return 2
        var count = n - 1
        var number = 1
        while (count > 0) {
            number += 2
            if (number.isPrime()) count--
        }
        return number
    }
}