package dev.bogwalk.batch5

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import dev.bogwalk.util.maths.isPrimeMRBI
import dev.bogwalk.util.maths.primeNumbers
import kotlin.concurrent.thread

/**
 * Problem 60: Prime Pair Sets
 *
 * https://projecteuler.net/problem=60
 *
 * Goal: Find the sums of every set of K-primes (where every prime < N) for which any of the 2
 * primes, when concatenated in any order, result in another prime.
 *
 * Constraints: 100 <= N <= 2e4, 3 <= K <= 5
 *
 * Lowest sum for a set of 4 primes = 792 -> {3, 7, 109, 673}
 * e.g. 7109 and 1097 are primes, as are 37 and 73.
 *
 * e.g.: N = 100, K = 3
 *       sums = [107, 123]
 *       107 -> {3, 37, 67} & 123 -> {7, 19, 97}
 */

class PrimePairSets {
    private val limit = 20_000
    // all non-single digit primes end in {1, 3, 7, 9} so 2 and 5 are ruled out
    // 3 is also removes so that it can be kept out of the list filter in the helper function
    private val allPrimes = primeNumbers(limit - 1).drop(3)

    /**
     * Kotlin coroutine library processes 2 prime lists in parallel.
     *
     * SPEED (BEST - for concurrent processing) 6.53s for N = 2e4, K = 5
     *
     * @return sorted list (ascending order) of the totals of all k-prime sets whose members are
     * eligible, as detailed above.
     */
    fun concurrentPrimePairSetSum(n: Int, k: Int): List<Int> {
        var totals: List<Int>
        runBlocking {
            val job1 = async { primePairSetSums(n, k, 1) }
            val job2 = async { primePairSetSums(n, k, 2) }
            totals = job1.await() + job2.await()
        }
        return totals.sorted()
    }

    /**
     * Kotlin standard library processes 2 prime lists in parallel using 2 threads. This method
     * does not require a dependency implementation in the build file.
     *
     * SPEED (BETTER - for concurrent processing) 8.54s for N = 2e4, K = 5
     *
     * @return sorted list (ascending order) of the totals of all k-prime sets whose members are
     * eligible, as detailed above.
     */
    fun threadPrimePairSetSum(n: Int, k: Int): List<Int> {
        var total1 = emptyList<Int>()
        var total2 = emptyList<Int>()
        thread(start = true) {
            total1 = primePairSetSums(n, k, 1)
        }.join()
        thread(start = true) {
            total2 = primePairSetSums(n, k, 2)
        }.join()
        return (total1 + total2).sorted()
    }

    /**
     * Solution optimised by the following:
     *
     *  -   Caching the results of concatenation & primality checks to avoid doing so for every
     *  nested iteration. The cache has every visited prime as a key and, as its value, a set of
     *  all greater primes with which it produces a prime after concatenation.
     *
     *  -   Set intersection is used in lieu of checking for each nested prime in all preceding
     *  prime pair sets. An empty set after intersection means that the latest prime checked is
     *  not eligible and can be skipped, with the set intersection being reverted to its previous
     *  value.
     *
     *  -   Refactored to be used in asynchronously in a coroutine scope by reducing list of primes
     *  to either include those that are congruent to 1 mod 3 or 2 mod 3. These lists can be
     *  processed separately as p_1, where p_1 % 3 == 1, and p_2, where p_2 % 3 == 2, will always
     *  concatenate to a number that is evenly divisible by 3, and thereby not a prime. This is
     *  based on the concatenation being congruent to the sum of p_1 and p_2 mod 3.
     *
     * SPEED (WORST - for processing all primes < n) 11.37s for N = 2e4, K = 5
     *
     * @param [m] modulo used to split all primes < n, to avoid unnecessary
     * isConcatenablePrime() checks.
     * @return unsorted list of the totals of all k-prime sets whose members are
     * eligible, as detailed above.
     */
    private fun primePairSetSums(n: Int, k: Int, m: Int): List<Int> {
        val concatenatedPairs = mutableMapOf<Int, Set<Int>>()
        // prime 3 has to be added otherwise it will not be included in the modulus filter
        val primes = listOf(3) + allPrimes.filter { p -> p < n && p % 3 == m }
        val numOfPrimes = primes.size

        fun getPairs(primeIndex: Int) {
            val prime = primes[primeIndex]
            val pairs = mutableSetOf<Int>()
            for (p in primeIndex + 1 until numOfPrimes) {
                val other = primes[p]
                if (isConcatenablePrime(prime, other)) pairs.add(other)
            }
            concatenatedPairs[prime] = pairs
        }

        val totals = mutableListOf<Int>()
        for (a in 0 until numOfPrimes - k + 1) {
            val kA = primes[a]
            if (kA !in concatenatedPairs.keys) getPairs(a)
            var common = concatenatedPairs[kA]!!
            for (b in a + 1 until numOfPrimes - k + 2) {
                val kB = primes[b]
                if (kB !in common) continue
                if (kB !in concatenatedPairs.keys) getPairs(b)
                val commonA = common
                common = common.intersect(concatenatedPairs[kB]!!)
                if (common.isEmpty()) {
                    common = commonA
                    continue
                }
                if (k - 2 == 1) {
                    for (kC in common) {
                        totals.add(kA + kB + kC)
                    }
                } else {
                    for (c in b + 1 until numOfPrimes - k + 3) {
                        val kC = primes[c]
                        if (kC !in common) continue
                        if (kC !in concatenatedPairs.keys) getPairs(c)
                        val commonB = common
                        common = common.intersect(concatenatedPairs[kC]!!)
                        if (common.isEmpty()) {
                            common = commonB
                            continue
                        }
                        if (k - 3 == 1) {
                            for (kD in common) {
                                totals.add(kA + kB + kC + kD)
                            }
                        } else {
                            for (d in c + 1 until numOfPrimes - k + 4) {
                                val kD = primes[d]
                                if (kD !in common) continue
                                if (kD !in concatenatedPairs.keys) getPairs(d)
                                val commonC = common
                                common = common.intersect(concatenatedPairs[kD]!!)
                                if (common.isEmpty()) {
                                    common = commonC
                                    continue
                                }
                                for (kE in common) {
                                    totals.add(kA + kB + kC + kD + kE)
                                }
                                common = commonC
                            }
                        }
                        common = commonB
                    }
                }
                common = commonA
            }
        }
        return totals
    }

    /**
     * Converting to and back from strings for concatenation can become slow. Instead, the first
     * prime is multiplied to an appropriate power based on the amount of digits in the second
     * prime, then they are added.
     */
    private fun isConcatenablePrime(prime1: Int, prime2: Int): Boolean {
        var power1 = 10L
        var power2 = 10L
        while (power1 <= prime2) {
            power1 *= 10
        }
        val concat1 = prime1 * power1 + prime2
        val isConcat1Prime = if (concat1 < limit) {
            concat1.toInt() in allPrimes
        } else {
            concat1.isPrimeMRBI()
        }
        if (!isConcat1Prime) return false
        while (power2 <= prime1) {
            power2 *= 10
        }
        val concat2 = prime2 * power2 + prime1
        return if (concat2 < limit) {
            concat2.toInt() in allPrimes
        } else {
            concat2.isPrimeMRBI()
        }
    }
}