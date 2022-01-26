package batch4

import util.getPermutations
import util.isPrime
import util.primeNumbers
import kotlin.math.pow

/**
 * Problem 49: Prime Permutations
 *
 * https://projecteuler.net/problem=49
 *
 * Goal: Return all concatenated integers formed by joining K terms,
 * such that the first term is below N and all K terms are permutations
 * of each other, as well as being prime, as well as being in constant
 * arithmetic progression.
 *
 * Constraints: 2000 <= N <= 1e6, K in {3, 4}
 *
 * e.g.: N = 2000, K = 3
 *       sequence = {1487, 4817, 8147}, with step = 3330
 *       output = 148748178147
 */

class PrimePermutations {
    /**
     * Solution generates all permutations of a prime & filters potential
     * candidates for a sequence. Will be slower due to permutations being
     * (re-)generated unnecessarily.
     *
     * SPEED: 18.7028s for N = 1e6, K = 3
     */
    fun primePermSequence(n: Int, k: Int): List<String> {
        val primes = primeNumbers(n - 1)
        val sequences = mutableListOf<String>()
        for (first in primes) {
            // there are no arithmetic sequences that match these
            // properties with terms having less than 4 digits.
            if (first < 1117) continue
            val firstChars = first.toString().toMutableList()
            val perms = getPermutations(firstChars, firstChars.size)
                .map{ it.toInt() }
                .toSet()
                .filter { it > first && isPrime(it) }
                .sorted()
            for (i in 0 until perms.size - k + 2) {
                val next = perms[i]
                val delta = next - first
                val sequence = List(k) { first + it * delta }
                if (sequence.drop(2).all {
                        it in perms.slice(i+1..perms.lastIndex)
                }) {
                    sequences.add(sequence.joinToString(""))
                }
            }
        }
        return sequences
    }

    /**
     * Generates a hash key for a prime number based on the amount of repeated
     * digits, represented as a numerical version of an indexed RTL array.
     *
     * e.g. 1487 -> 110010010 <- 4817
     *      2214 -> 10210 <- 4212
     */
    private fun permutationID(n: Int): Int {
        var perm = n
        var permID = 0
        while (perm > 0) {
            val digit = perm % 10
            permID += (10.0).pow(digit).toInt()
            perm /= 10
        }
        return permID
    }

    /**
     * Solution optimised by using a helper function that maps all primes
     * with same type and amount of digits to a permutation id. Then every
     * list of primes that share a permutation id and has >= K elements is
     * iterated over to check for an arithmetic progression sequence. Also
     * eliminates need to check for primality by pre- generating all primes
     * with same number of digits.
     *
     * SPEED: 0.7789s for N = 1e6, K = 3
     */
    fun primePermSequenceImproved(n: Int, k: Int): List<String> {
        val limit = (10.0).pow(n.toString().length).toInt() - 1
        val primes = primeNumbers(limit)
        val primePerms = mutableMapOf<Int, IntArray>()
        val sequences = mutableListOf<List<Int>>()
        for (prime in primes) {
            if (prime < 1117) continue
            val permID = permutationID(prime)
            primePerms[permID] = primePerms.getOrDefault(permID, intArrayOf()) + prime
        }
        for (perms in primePerms.values) {
            if (perms.size >= k) {
                for (i in 0 until perms.size - k + 1) {
                    val first = perms[i]
                    if (first >= n) break
                    for (j in i + 1 until perms.size - k + 2) {
                        val delta = perms[j] - first
                        val sequence = List(k) { first + it * delta }
                        if (sequence.drop(2).all {
                                it in perms.slice(j+1..perms.lastIndex)
                            }) {
                            sequences.add(sequence)
                        }
                    }
                }
            }
        }
        return sequences
            .sortedBy { it.first() }
            .map { it.joinToString("") }
    }
}