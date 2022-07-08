package dev.bogwalk.batch7

import kotlin.math.sqrt

/**
 * Problem 72: Counting Fractions
 *
 * https://projecteuler.net/problem=72
 *
 * Goal: Count the elements in a set of reduced proper fractions for d <= N, i.e. order N.
 *
 * Constraints: 2 <= N <= 1e6
 *
 * Reduced Proper Fraction: A fraction n/d, where n & d are positive integers, n < d, and
 * gcd(n, d) == 1.
 *
 * Farey Sequence: A sequence of completely reduced fractions, either between 0 and 1, or which
 * when in reduced terms have denominators <= N, arranged in order of increasing size.
 *
 *      e.g. if d <= 8, the Farey sequence would be ->
 *          1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4,
 *          4/5, 5/6, 6/7, 7/8
 *
 * e.g.: N = 8
 *       size = 21
 */

class CountingFractions {
    /**
     * Solution based on the following Farey sequence properties:
     *
     *  - The middle term of a sequence is always 1/2 for n > 1.
     *
     *  - A sequence F(n) contains all elements of the previous sequence F(n-1) as well as an
     *  additional fraction for each number < n & co-prime to n. e.g. F(6) consists of F(5) as
     *  well as 1/6 & 5/6.
     *
     * Using Euler's totient (phi) function, the relationship between lengths becomes:
     *
     *      F(n).size = F(n-1).size + phi(n)
     *
     *      F(n).size = 0.5 * (3 + ({n}Sigma{d=1} mobius() * floor(n/d)^2))
     *
     *      F(n).size = 0.5n(n + 3) - ({n}Sigma{d=2} F(n/d).size)
     *
     * N.B. This solution does not count left & right ancestors of F(1), namely {0/1, 1/0}, so
     * these should be removed from the resulting length.
     *
     * SPEED (WORST) 74.07ms for N = 1e6
     */
    fun fareySequenceLengthFormula(order: Int): Long {
        val fCache = LongArray(order + 1).apply {
            this[1] = 2
        }

        fun getLength(n: Int): Long {
            if (fCache[n] != 0L) return fCache[n]
            var length = (0.5 * n * (n + 3)).toLong()
            for (d in 2..n) {
                val next = n / d
                length -= if (fCache[next] != 0L) {
                    fCache[next]
                } else {
                    val newLength = getLength(next)
                    fCache[next] = newLength
                    newLength
                }
            }
            fCache[n] = length
            return length
        }

        return getLength(order) - 2
    }

    /**
     * Solution uses Euler's product formula:
     *
     *      Phi(n) = n * Pi(1 - (1/p)), with p being distinct prime factors of n.
     *
     *      if prime factorisation means n = Pi(p_i^a_i), the above formula becomes:
     *
     *      Phi(n) = Pi((p_i - 1) * p_i^(a_i - 1))
     *
     *      if m = n/p, then Phi(n) = Phi(m)p, if p divides m, else, Phi(m)(p - 1)
     *
     * This quickly calculates totients by first sieving the smallest prime factors & checking if
     * they are a multiple factor of n.
     *
     * Only odd numbers are included in the sieve, as all even numbers are handled together based
     * on the following where k > 0 and n is odd:
     *
     *      {m}Pi{k=0}(Phi(n2^k)) = (1 + {m}Pi{k=1}(2^(k-1)) * Phi(n) = 2^m * Phi(n)
     *
     * SPEED (BEST) 19.10ms for N = 1e6
     */
    fun fareySequenceLengthSieve(limit: Int): Long {
        val sieveLimit = (sqrt(1.0 * limit).toInt() - 1) / 2
        val maxI = (limit - 1) / 2
        val cache = IntArray(maxI + 1)
        for (n in 1..sieveLimit) { // sieve the smallest prime factors
            if (cache[n] == 0) { // 2n + 1 is prime
                val p = 2 * n + 1
                for (k in (2 * n * (n + 1)..maxI) step p) {
                    if (cache[k] == 0) {
                        cache[k] = p
                    }
                }
            }
        }
        // find largest multiplier (m) where 2^m * n <= N, i.e. the largest power of 2
        var multiplier = 1
        while (multiplier <= limit) {
            multiplier *= 2
        }
        multiplier /= 2 // num of reduced proper fractions whose denominator is power of 2
        var count = 1L * multiplier - 1 // decrement to exclude fraction 1/1
        multiplier /= 2 // set to 2^(m-1)
        // the smallest index such that (2n + 1) * 2^(m-1) > N
        var stepI = (limit / multiplier + 1) / 2
        for (n in 1..maxI) {
            if (n == stepI) {
                multiplier /= 2 // set to next smallest power of 2
                stepI = (limit / multiplier + 1) / 2
                // this maintains the invariant: (2n + 1)m <= N < (2n + 1)2m
            }
            if (cache[n] == 0) {
                cache[n] = 2 * n
            } else {
                val p = cache[n]
                val cofactor = (2 * n + 1) / p // calculate Phi(2n + 1)
                val factor = if (cofactor % p == 0) p else p - 1
                cache[n] = factor * cache[cofactor/2]
            }
            // add sum of totients of all 2^k * (2n + 1) < N
            count += multiplier * cache[n]
        }
        return count
    }

    /**
     * Solution still uses a sieve, as in the above solution, but the sieve calculates the
     * totient of all k multiples of primes <= [limit] based on the following:
     *
     *      current Phi(p_k) = previous Phi(p_k) * (1 - (1/p))
     *
     *      current Phi(p_k) = previous Phi(p_k) - previous Phi(p_k)/p
     *
     * Then the size of each sequence order is cached based on:
     *
     *      F(n).size = F(n-1).size + phi(n)
     *
     * SPEED (BETTER) 64.55ms for N = 1e6
     *
     * @return array of Farey sequence lengths for every index = order N.
     */
    fun generateAllFareyLengths(limit: Int): LongArray {
        val phiCache = IntArray(limit + 1) { it }
        for (n in 2..limit) {
            if (phiCache[n] == n) { // n is prime
                // calculate Phi of all multiples of n
                for (k in n..limit step n) {
                    phiCache[k] -= phiCache[k] / n
                }
            }
        }
        val counts = LongArray(limit + 1)
        for (n in 2..limit) {
            counts[n] = counts[n-1] + phiCache[n]
        }
        return counts
    }
}