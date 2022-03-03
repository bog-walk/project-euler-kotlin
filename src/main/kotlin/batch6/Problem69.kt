package batch6

import util.maths.primeFactors
import util.maths.primeNumbers
import kotlin.math.pow

/**
 * Problem 69: Totient Maximum
 *
 * https://projecteuler.net/problem=69
 *
 * Goal: Find the smallest value of n < N for which n / Phi(n) is maximum.
 *
 * Constraints: 3 <= N <= 1e18
 *
 * Euler's Totient Function: Phi(n) is used to determine the count of positive integers < n that are
 * relatively prime to n. An integer k is relatively prime to n (aka co-prime or its totative) if
 * gcd(n, k) = 1, as the only positive integer that is a divisor of both of them is 1.
 * n = 2 -> {1}                -> Phi(n) = 1; n/Phi(n) = 2
 * n = 3 -> {1, 2}             -> Phi(n) = 2; n/Phi(n) = 1.5
 * n = 4 -> {1, 3}             -> Phi(n) = 2; n/Phi(n) = 2
 * n = 5 -> {1, 2, 3, 4}       -> Phi(n) = 4; n/Phi(n) = 1.25
 * n = 6 -> {1, 5}             -> Phi(n) = 2; n/Phi(n) = 3
 * n = 7 -> {1, 2, 3, 4, 5, 6} -> Phi(n) = 6; n/Phi(n) = 1.1666...
 * n = 8 -> {1, 3, 5, 7}       -> Phi(n) = 4; n/Phi(n) = 2
 * n = 9 -> {1, 2, 4, 5, 7, 8} -> Phi(n) = 6; n/Phi(n) = 1.5
 * n = 10 -> {1, 3, 7, 9}      -> Phi(n) = 4; n/Phi(n) = 2.5
 *
 * e.g.: N = 3
 *       n = 2
 *       N = 10
 *       n = 6
 */

class TotientMaximum {
    /**
     * Solution calculates the totient of each n under [limit] & returns the first n that
     * achieves the maximum ratio.
     *
     * SPEED (WORSE) 10.22s for N = 1e6
     */
    fun maxTotientRatio(limit: Long): Long {
        var maxValues = 1L to 1.0
        for (n in 2..limit) {
            val currentTotient = totient(n)
            val currentRatio = 1.0 * n / currentTotient
            if (currentRatio > maxValues.second) {
                maxValues = n to currentRatio
            }
        }
        return maxValues.first
    }

    /**
     * Based on Euler's product formula:
     *
     *      Phi(n) = n * Pi(1 - (1/p)), with p being distinct prime factors of n.
     *
     * This is equivalent to the formula that doesn't use fractions:
     *
     *      Phi(n) = p_1^(e_1 - 1)(p_1 - 1) * p_2^(e_2 - 1)(p_2 - 1)... p_r(e_r - 1)(p_r - 1)
     *
     *      e.g. Phi(20) = Phi({2^2, 5^1}) = 2^1 * 1 * 5^0 * 4 = 8
     */
    private fun totient(n: Long): Long {
        val primeFactors = primeFactors(n)
        var count = 1L
        for ((p, e) in primeFactors.entries) {
            count *= (1.0 * p).pow(e - 1).toLong() * (p - 1)
        }
        return count
    }

    /**
     * Solution optimised by taking Euler's product formula further:
     *
     *      Phi(n) = n * Pi(1 - (1/p)), with p being distinct prime factors of n.
     *
     * If the ratio n / Phi(n) is the required result, this becomes:
     *
     *      n/Phi(n) = n / (n * Pi(1 - (1/p)))
     *      n/Phi(n) = Pi(p / (p - 1))
     *
     * Among all numbers having exactly k-distinct prime factors, the quotient is maximised for
     * those numbers divisible by the k-smallest primes. So if n_k is the product of the
     * k-smallest primes, n_k/Phi(n_k) is maximised over all n/Phi(n) that occur for n < n_{k+1}.
     *
     * N.B. Upper constraints 1e18 will be reached by prime number 47.
     *
     * SPEED (BETTER) 5.0e4ns for N = 1e6
     */
    fun maxTotientRatioPrimorial(n: Long): Long {
        val primes = primeNumbers(50)
        var maxN  = 1L
        for (p in primes) {
            // maxN < 1e18 occurs for prime = 43; if this is multiplied by 47, it will overflow
            // Long's 64 bits & not return the correct answer for N = 1e18, so, unlike with Python,
            // the loop break has to be determined before the limit is exceeded.
            if (maxN >= (n + p - 1) / p) break
            maxN *= p
        }
        return maxN
    }
}