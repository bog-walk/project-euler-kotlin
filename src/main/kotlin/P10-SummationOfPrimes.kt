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
     * less than a provided integer, best up to 10^5, then consider
     * performing trial division by these primes.
     * This algorithm marks all composites of the divisors as false.
     */
    fun getPrimesUsingSieve(max: Int): List<Int> {
        if (max < 2) return emptyList()
        // immediately set all even numbers except for 2 as false
        val primesBool = BooleanArray(max - 1) { !(it != 0 && it % 2 == 0) }
        // only iterate through odd numbers
        for (p in 3..(sqrt(1.0 * max).toInt()) step 2) {
            if (primesBool[p - 2]) {
                if (p * p > max) break
                // Steps of 2p for odd primes avoids already marked out multiples of 2
                for (m in (p * p)..max step 2 * p) {
                    primesBool[m - 2] = false
                }
            }
        }
        return primesBool.mapIndexed { i, isPrime ->
            if (isPrime) i + 2 else null
        }.filterNotNull()
    }

    /**
     *  This optimisation discriminates against even numbers entirely, thereby
     *  using only half of memory & fewer iterations, for multiple draws.
     *  Returns array of sums of prime numbers less than or equal to index + 2.
     */
    fun sumOfPrimesQuickDraw(): LongArray {
        val max = 1_000_000
        val primesBool = BooleanArray(max) { it >= 2 }
        val sums = LongArray (max) { 0L }
        for (i in 2 until max) {
            if (primesBool[i]) {
                sums[i] = sums[i - 1] + i
                for (j in (i * i) until max step i) {
                    primesBool[j] = false
                }
            } else {
                sums[i] = sums[i - 1]
            }
        }
        return sums
    }
}