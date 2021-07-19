import util.getPrimeNumbers
import kotlin.math.sqrt

/**
 * Problem 10: Summation of Primes
 * Goal: Find the sum of all the primes not greater than N, with 1 <= N <= 10^6.
 * e.g. N = 10 -> {2, 3, 5, 7}.sum() = 17.
 */

class SummationOfPrimes {
    // Takes either brute iterative function (top-level) or efficient sieve function below
    fun sumOfPrimes(n: Int, primesFunc: (Int) -> List<Int>): Long {
        return primesFunc(n).sumOf { it.toLong() }
    }

    /**
     * Sieve of Eratosthenes algorithm: an efficient way to find all primes
     * less than a provided integer.
     */
    fun getPrimesUsingSieve(max: Int): List<Int> {
        if (max < 2) return emptyList()
        val primesBool = BooleanArray(max - 1) { true }
        for (p in 2..(sqrt(1.0 * max).toInt())) {
            if (p * p > max) break
            if (primesBool[p - 2]) {
                for (m in (p * p)..max step p) {
                    primesBool[m - 2] = false
                }
            }
        }
        return primesBool.mapIndexed { i, isPrime ->
            if (isPrime) i + 2 else null
        }.filterNotNull()
    }
}