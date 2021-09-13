package batch1

/**
 * Problem 10: Summation of Primes
 *
 * https://projecteuler.net/problem=10
 *
 * Goal: Find the sum of all prime numbers <= N.
 *
 * Constraints: 1 <= N <= 1e6
 *
 * e.g. N = 5
 *      primes = {2, 3, 5}
 *      sum = 10
 */

class SummationOfPrimes {

    fun sumOfPrimes(n: Int, primesFunc: (Int) -> List<Int>): Long {
        return primesFunc(n).sumOf { it.toLong() }
    }

    /**
     * Returns array of sums of prime numbers <= index.
     * This optimisation use the Sieve of Eratosthenes algorithm to discriminate
     * against even numbers entirely, using only half of memory & fewer
     * iterations, for multiple draws.
     */
    fun sumOfPrimesQuickDraw(max: Int): LongArray {
        require(max % 2 == 0) { "Limit must be even otherwise loop check needed" }
        val primesBool = BooleanArray(max + 1) {
            it > 2 && it % 2 != 0 || it == 2
        }
        val sums = LongArray (max + 1) { 0L }.apply { this[2] = 2L }
        for (i in 3..max step 2) {
            if (primesBool[i]) {
                sums[i] = sums[i - 1] + i
                if (1L * i * i < max.toLong()) {
                    for (j in (i * i)..max step 2 * i) {
                        primesBool[j] = false
                    }
                }
            } else {
                sums[i] = sums[i - 1]
            }
            // Even numbers greater than 2 are skipped, receiving previous sum
            sums[i + 1] = sums[i]
        }
        return sums
    }
}