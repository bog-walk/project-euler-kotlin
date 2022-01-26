package batch3

import batch0.sum
import util.gcd
import util.pythagoreanTriplet
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Problem 39: Integer Right Triangles
 *
 * https://projecteuler.net/problem=39
 *
 * Goal: If P is the perimeter of a right-angle triangle, find the
 * smallest value of P <= N that has the maximum number of (a, b, c) solutions.
 *
 * Constraints: 12 <= N <= 5e6
 *
 * e.g. P = 120 has 3 solutions: (20, 48, 52), (24, 45, 51) & (30, 40, 50).
 *
 * e.g.: N = 12
 *       P = 12 as 12 is the only sum of a Pythagorean triplet (3, 4, 5)
 */

class IntegerRightTriangles {
    /**
     * Brute solution based on the following:
     * - Pythagorean Triplets must either be all evens OR 2 odds with 1 even.
     * So, the sum of triplets will only ever be an even number as the sum of evens
     * is an even number, as is the sum of 2 odds.
     *
     * - Since a < b < c and a + b + c = P, a will not be higher than P / 3.
     *
     * - If c = P - a - b is inserted into the equation a^2 + b^2 = c^2, then:
     * a^2 + b^2 = P^2 - 2aP - 2bP + 2ab + a^2 + b^2, which yields ->
     * b = (P * (P - 2 * a)) / (2 * (P - a)), which means ->
     * values of P and a that result in an integer value b represent a valid Triplet.
     *
     * SPEED: 10.4579s for N = 1e5
     */
    fun mostTripletSolutionsBrute(n: Int): Int {
        var bestP = 12
        var mostSols = 1
        for (p in 14..n step 2) {
            var pSols = 0
            for (a in 4 until p / 3) {
                val b: Long = (1L * p * (1L * p - 2 * a)) % (1L * 2 * (p - a))
                if (b == 0L) pSols++
            }
            if (pSols > mostSols) {
                bestP = p
                mostSols = pSols
            }
        }
        return bestP
    }

    /**
     * Optimised solution based on the previously determined solution for
     * finding primitive Pythagorean Triplets (Problem 9).
     *
     * SPEED: 0.0702s for N = 1e5
     */
    fun mostTripletSolutions(n: Int): Int {
        var bestP = 12
        var mostSols = 1
        for (p in 14..n step 2) {
            var pSols = 0
            val limit = p / 2
            val mMax = ceil(sqrt(1.0 * limit)).toInt()
            for (m in 2..mMax) {
                if (limit % m == 0) {
                    val kMax = p / (2 * m)
                    var k = if (m % 2 == 1) m + 2 else m + 1
                    while (k < 2 * m && k <= kMax) {
                        if (kMax % k == 0 && gcd(k.toLong(), m.toLong()) == 1L) {
                            pSols++
                        }
                        k += 2
                    }
                }
            }
            if (pSols > mostSols) {
                bestP = p
                mostSols = pSols
            }
        }
        return bestP
    }

    /**
     * Solution above is improved by relying solely on Euclid's formula to
     * generate all primitive Pythagorean triplets.
     * Note that the upper bound for m is found by substituting Euclid's
     * formulae into the perimeter formula, & reducing to:
     * p = 2 * d * m * (m + n), which means ->
     * when d = 1 & n = 1, at most 2 * m * m must be below the given limit.
     *
     * SPEED (BEST): 0.0208s for N = 1e5
     */
    fun mostTripletSolutionsImproved(limit: Int): Int {
        // Every perimeter that has a triple will be represented as
        // a count at p_sols[perimeter]
        val pSols = IntArray(limit + 1)
        var m = 2
        while (2 * m * m < limit) {
            for (n in 1 until m) {
                // ensure that both m and n are not odd
                // and that m and n are co-prime (gcd == 1)
                if (m % 2 == 1 && n % 2 == 1 || gcd(m.toLong(), n.toLong()) > 1L) {
                    continue
                }
                var d = 1
                while (true) {
                    val p = pythagoreanTriplet(m, n, d).sum()
                    if (p > limit) break
                    pSols[p] += 1
                    d++
                }
            }
            m++
        }
        // Convert pSols to an array that accumulates the perimeter, below the
        // given limit, with the most counts, best[limit].
        val best = IntArray(limit + 1)
        var bestP = 12
        var mostSols = 1
        for (i in 12..limit) {
            val count = pSols[i]
            if (count > mostSols) {
                bestP = i
                mostSols = count
            }
            best[i] = bestP
        }
        return best[limit]
    }
}