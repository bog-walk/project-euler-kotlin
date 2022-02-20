package batch4

import util.combinatorics.permutationID
import util.combinatorics.permutations
import util.maths.isPrime
import util.maths.primeNumbers
import kotlin.math.pow

/**
 * Problem 49: Prime Permutations
 *
 * https://projecteuler.net/problem=49
 *
 * Goal: Return all concatenated numbers formed by joining K terms, such that the first term is
 * below N and all K terms are permutations of each other, as well as being prime and in constant
 * arithmetic progression.
 *
 * Constraints: 2000 <= N <= 1e6, K in {3, 4}
 *
 * e.g.: N = 2000, K = 3
 *       sequence = {1487, 4817, 8147}, with step = 3330
 *       output = "148748178147"
 */

class PrimePermutations {
    /**
     * Solution uses permutations() helper algorithm to find all permutations of a prime & filter
     * potential candidates for a sequence.
     *
     * SPEED (WORSE) 64.91s for N = 1e6, K = 3
     * Slower due to permutations being (re-)generated unnecessarily.
     */
    fun primePermSequence(n: Int, k: Int): List<String> {
        val primes = primeNumbers(n - 1)
        val sequences = mutableListOf<String>()
        for (first in primes) {
            // there are no arithmetic sequences that match these properties with terms having
            // less than 4 digits.
            if (first < 1117) continue
            val firstChars = first.toString().toList()
            val perms = permutations(firstChars, firstChars.size)
                .map{ it.joinToString("").toInt() }
                .toSet()
                .filter { perm ->
                    perm > first && perm.isPrime()
                }
                .sorted()
            next@for (i in 0 until perms.size - k + 2) {
                val second = perms[i]
                val delta = second - first
                for (x in 1..k-2) {
                    val next = second + x * delta
                    if (next !in perms.slice(i+1..perms.lastIndex)) {
                        continue@next
                    }
                }
                val sequence = List(k) { first + it * delta }
                sequences.add(sequence.joinToString(""))
            }
        }
        return sequences
    }

    /**
     * Solution optimised by using permutationID() helper that maps all primes with same type and
     * amount of digits to a permutation id. Then every list of primes that share a permutation
     * id and has >= K elements is iterated over to check for an arithmetic progression sequence.
     *
     * Pre-generating all primes with same number of digits also eliminates the need to check for
     * primality.
     *
     * SPEED (BETTER) 1.47s for N = 1e6, K = 3
     */
    fun primePermSequenceImproved(n: Int, k: Int): List<String> {
        val limit = (10.0).pow(n.toString().length).toInt() - 1
        val primes = primeNumbers(limit)
        val primePerms = mutableMapOf<String, List<Int>>()
        val sequences = mutableListOf<List<Int>>()
        for (prime in primes) {
            if (prime < 1117) continue
            val permID = permutationID(prime.toLong()).joinToString("")
            primePerms[permID] = primePerms.getOrDefault(permID, listOf()) + prime
        }
        for (perms in primePerms.values) {
            if (perms.size >= k) {
                for (i in 0 until perms.size - k + 1) {
                    val first = perms[i]
                    if (first >= n) break
                    next@for (j in i + 1 until perms.size - k + 2) {
                        val delta = perms[j] - first
                        for (x in 2 until k) {
                            val next = first + x * delta
                            if (next !in perms.slice(j+1..perms.lastIndex)) {
                                continue@next
                            }
                        }
                        sequences.add(List(k) { first + it * delta })
                    }
                }
            }
        }
        return sequences
            .sortedBy { it.first() }
            .map { it.joinToString("") }
    }
}