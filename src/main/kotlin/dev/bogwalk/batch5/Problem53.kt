package dev.bogwalk.batch5

import dev.bogwalk.util.combinatorics.binomialCoefficient
import java.math.BigInteger

/**
 * Problem 53: Combinatoric Selections
 *
 * https://projecteuler.net/problem=53
 *
 * Goal: Count the values of C(n, r), for 1 <= n <= N, that are greater than K. Values do not
 * have to be distinct.
 *
 * Constraints: 2 <= N <= 1000, 1 <= K <= 1e18
 *
 * Binomial Coefficient:
 *
 *      C(n, r) = n! / r!(n - r)!, where r <= n.
 *
 * There are 10 combinations when 3 digits are chosen from 5 digits, with no repetition & order
 * not mattering: C(5, 3) = 10. It is not until n = 23 that the amount of combinations first
 * exceeds 1e6, namely C(23, 10) = 1_144_066.
 *
 * e.g.: N = 23, K = 1e6
 *       answer = 4
 */

class CombinatoricSelections {
    /**
     * Solution optimised based on the symmetry of Pascal's Triangle:
     *
     *  -   C(n, 0) = 1, C(n, 1) = n. k could be less than n, so must start r-loop at 1.
     *
     *  -   C(n, r) = C(n, n-r) & peaks for each row at the mid-point.
     *
     *  -   So if C(n, r) > k, then all C(n, x) for x in [r+1, n-r] will also be > k. Based on
     *  the incrementing row count (n + 1), this can be calculated as n - 2r + 1, so the rest of
     *  the row values do not need to be also calculated.
     *
     *  -   Starting from the bottom of the triangle & moving up, if no value in a row is greater
     *  than k, then no row (of lesser n) will have valid values & the outer loop can be broken.
     *
     * SPEED (WORSE) 476.40ms for N = 1e3, K = 1e3
     */
    fun countLargeCombinatorics(num: Int, k: Long): Int {
        val kBI = BigInteger.valueOf(k)
        var n = num
        var count = 0
        nextN@while (n > 0) {
            for (r in 1..n / 2) {
                if (binomialCoefficient(n, r) > kBI) {
                    count += n - 2 * r + 1
                    n--
                    continue@nextN
                }
            }
            break
        }
        return count
    }

    /**
     * Solution improved by not depending on factorials to pre-compute the binomial coefficient;
     * however, BigInteger is still necessary to assess the branch where the next nCr is
     * potentially greater than [k], the latter of which can be as high as 1e18.
     *
     * Solution is still based on the symmetry of Pascal's Triangle & its rules as detailed in
     * the solution above, with some additions:
     *
     *      C(n, r-1) = C(n, r) * (n-r) / (r+1) and
     *
     *      C(n-1, r) = C(n, r) * (n-r) / n
     *
     *  -   Movement through the triangle (bottom-up & only checking border values) mimics that
     *  in the above function, but C(n, r) values when moving right in a row or up a row are
     *  determined with these formulae, instead of factorials.
     *
     *  -   Starting from the bottom of the triangle & moving up, if the value of r is allowed to
     *  exceed its midline value, then it means no value > k was found and the outer loop can be
     *  broken.
     *
     * SPEED (BETTER) 3.42ms for N = 1e3, K = 1e3
     */
    fun countLargeCombinatoricsImproved(num: Int, k: Long): Int {
        val kBI = BigInteger.valueOf(k)
        var count = BigInteger.ZERO
        var n = num.toBigInteger()
        // start at left-most border
        var r = BigInteger.ZERO
        var nCr = BigInteger.ONE
        while (r <= n / BigInteger.TWO) {
            val nextInRow = nCr * (n - r) / (r + BigInteger.ONE)
            if (nextInRow > kBI) {
                // count formula differs from previous solution above
                // because r is 1 less than r in nextInRow
                count += n - BigInteger.TWO * r - BigInteger.ONE
                nCr = nCr * (n - r) / n
                n--
            } else {
                r++
                nCr = nextInRow
            }
        }
        return count.intValueExact()
    }
}