import util.getPrimeNumbers

/**
 * Problem 10: Summation of Primes
 * Goal: Find the sum of all the primes not greater than N, with 1 <= N <= 10^6.
 * e.g. N = 10 -> {2, 3, 5, 7}.sum() = 17.
 */

class SummationOfPrimes {
    fun sumOfPrimesBrute(n: Int): Long {
        return getPrimeNumbers(n).sumOf { it.toLong() }
    }
}