package batch7

import util.maths.primeNumbers

/**
 * Problem 73: Counting Fractions In A Range
 *
 * https://projecteuler.net/problem=73
 *
 * Goal: Count the elements of a sorted set of reduced proper fractions of order D (their
 * denominator <= D) that lie between 1/A+1 and 1/A (both exclusive).
 *
 * Constraints: 1 < D < 2e6, 1 < A <= 100
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
 * e.g.: D = 8, A = 2
 *       count between 1/3 and 1/2 = 3
 */

class CountingFractionsInARange {
    /**
     * Solution uses recursion to count mediants between neighbours based on:
     *
     *      p/q = (a + n)/(b + d)
     *
     * This is a recursive implementation of the Stern-Brocot tree binary search algorithm.
     *
     * There is no need to reference the numerators of these bounding fractions, so each fraction
     * is represented as its Integer denominator instead of a Pair as in previous problem sets.
     *
     * SPEED (WORSE) 96.63ms for D = 12000, A = 2
     * SPEED (BETTER) 2.22ms for D = 1e5, A = 100
     * Risk of StackOverflow Error for D > 1e5.
     */
    fun fareyRangeCountRecursive(d: Int, a: Int): Long = countMediants(d, a + 1, a)

    /**
     * Recursively count all mediants between a lower and upper bound until the calculated
     * denominator exceeds [d]. Each new mediant will replace both the lower and upper bounds in
     * different recursive calls.
     */
    private fun countMediants(d: Int, left: Int, right: Int): Long {
        val mediantDenom = left + right
        return if (mediantDenom > d) {
            0L
        } else {
            1L + countMediants(d, left, mediantDenom) + countMediants(d, mediantDenom, right)
        }
    }

    /**
     * This an iterative implementation of the Stern-Brocot tree binary search algorithm above.
     *
     * SPEED (WORST) 213.63ms for D = 12000, A = 2
     * SPEED (WORSE) 16.69ms for D = 1e5, A = 100
     * Quadratic complexity O(N^2) makes it difficult to scale for D > 1e5.
     */
    fun fareyRangeCountIterative(d: Int, a: Int): Long {
        var count = 0L
        var left = a + 1
        var right = a
        val stack = mutableListOf<Int>()
        while (true) {
            val mediantDenom = left + right
            if (mediantDenom > d) {
                if (stack.isEmpty()) break
                left = right
                right = stack.removeLast()
            } else {
                count++
                stack.add(right)
                right = mediantDenom
            }
        }
        return count
    }

    /**
     * Solution finds the number of reduced fractions less than the greater fraction 1/[a], then
     * subtracts the number of reduced fractions less than 1/([a]+1) from the former. The result is
     * further decremented to remove the count for the upper bound fraction itself.
     *
     * SPEED (BETTER) 8.97ms for D = 12000, A = 2
     * SPEED (BETTER) 719.88ms for D = 2e6, A = 2
     */
    fun fareyRangeCountSieve(d: Int, a: Int): Long {
        return fareySieve(d, a) - fareySieve(d, a + 1) - 1
    }

    /**
     * Finds number of reduced fractions p/q <= 1/[a] with q <= [d].
     */
    private fun fareySieve(d: Int, a: Int): Long {
        // every denominator q is initialised to floor(1/a * q) == floor(q/a)
        val cache = LongArray(d + 1) { q -> 1L * q / a }
        for (q in 1..d) {
            // subtract each q's rank from all its multiples
            for (m in 2 * q..d step q) {
                cache[m] -= cache[q]
            }
        }
        return cache.sum()
    }

    /**
     * Solution based on the Inclusion-Exclusion principle.
     *
     * If F(N) is a Farey sequence and R(N) is a Farey sequence of reduced proper fractions, i.e.
     * gcd(n, d) = 1), then the starting identity is:
     *
     *      F(N) = {N}Sigma{m=1}(R(floor(N/m)))
     *
     * Using the Mobius inversion formula, this becomes:
     *
     *      R(N) = {N}Sigma{m=1}(mobius(m) * F(floor(N/m)))
     *
     * If all Mobius function values were pre-generated, this would allow a O(N) loop. Instead,
     * R(N) is calculated using primes and the inclusion-exclusion principle.
     *
     * N.B. This solution does not translate well if A != 2.
     *
     * SPEED (BEST) 5.43ms for D = 12000, A = 2
     * SPEED (BEST) 167.49ms for D = 2e6, A = 2
     */
    fun fareyRangeCountIE(d: Int, a: Int): Long {
        require(a == 2) { "Solution does not work if A != 2" }
        val primes = primeNumbers(d)
        val numOfPrimes = primes.size

        fun inclusionExclusion(limit: Int, index: Int): Long {
            var count = numOfFractionsInRange(limit)
            var i = index
            while (i < numOfPrimes && 5 * primes[i] <= limit) {
                val newLimit = limit / primes[i]
                count -= inclusionExclusion(newLimit, ++i)
            }
            return count
        }

        return inclusionExclusion(d, 0)
    }

    /**
     * Counts the number of fractions between 1/3 and 1/2 based on the formula:
     *
     *      F(N) = {N}Sigma{m=1}(R(floor(N/m)))
     *
     * This can be rewritten to assist finding R(N) in the calling function:
     *
     *      F(m) = {m}Sigma{n=1}(floor((n-1)/2) - floor(n/3))
     *
     *      F(m) = q(3q - 2 + r) + { if (r == 5) 1 else 0 }
     *
     *      where m = 6q + r and r in [0, 6).
     *
     * This helper function has not been adapted to allow different values of A. Once the lcm of
     * A and A+1 is found, the above F(m) equation needs to be solved for q and r to determine
     * the coefficients for the final function. How to do so programmatically for any input A has
     * not yet been achieved.
     */
    private fun numOfFractionsInRange(limit: Int): Long {
        val q = limit / 6
        val r = limit % 6
        var f = 1L * q * (3 * q - 2 + r)
        if (r == 5) f++
        return f
    }
}