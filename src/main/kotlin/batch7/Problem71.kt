package batch7

import util.maths.gcd
import util.maths.lcm

/**
 * Problem 71: Ordered Fractions
 *
 * https://projecteuler.net/problem=71
 *
 * Goal: By listing the set of reduced proper fractions for d <= N in ascending order of size,
 * find the numerator and denominator of the fraction immediately to the left of n/d.
 *
 * Constraints: 1 <= n < d <= 1e9, gcd(n, d) == 1, d < N <= 1e15
 *
 * Reduced Proper Fraction: A fraction n/d, where n & d are positive integers, n < d, and
 * gcd(n, d) == 1.
 *
 * Farey Sequence: A sequence of completely reduced fractions, either between 0 and 1, or which
 * when in reduced terms have denominators <= N, arranged in order of increasing size. The
 * sequence optionally begins with 0/1 and ends with 1/1 if restricted. The middle term of a
 * Farey sequence is always 1/2 for N > 1.
 *
 *      e.g. if d < 9, the Farey sequence would be ->
 *          1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4,
 *          4/5, 5/6, 6/7, 7/8
 *
 * e.g.: N = 8, n = 3, d = 7
 *       ans = 2/5
 */

class OrderedFractions {
    /**
     * Solution finds Farey sequence neighbours based on the following:
     *
     * If a/b and n/d are neighbours, with a/b < n/d, then their difference:
     *
     *      n/d - a/b = (nb - ad)/(db)
     *
     *      with nb - ad = 1, it becomes ->
     *
     *      n/d - a/b = 1/(db)
     *
     * A mediant fraction can be found between 2 neighbours using:
     *
     *      p/q = (a + n)/(b + d)
     *
     * This solution could also be implemented similarly using a Stern-Brocot Tree fraction
     * search algorithm that uses binary search to recursively find the target fraction [n]/[d]
     * starting from the left & right ancestors, 0/1 & 1/0. Once found, the last left boundary is
     * used with the target to find all mediants until a new mediant's denominator exceeds [limit].
     *
     * SPEED (WORST) 1.00s for N = 1e7
     * SPEED (Impossible for N > 1e7)
     *
     * @return pair of (numerator, denominator) representing the fraction to the left of [n]/[d].
     */
    fun leftFareyNeighbour(limit: Long, n: Long, d: Long): Pair<Long, Long> {
        val upperBound = n to d
        var lowerBound = if (d != limit) (n to d + 1).reduced() else (n - 1 to d).reduced()
        val half = 1L to 2L
        if (lowerBound < half && half < upperBound) {
            lowerBound = half
        }
        var neighbour: Pair<Long, Long>? = null
        while (true) {
            val delta = upperBound - lowerBound
            val neighbourDelta = 1L to lowerBound.second * d
            if (delta == neighbourDelta) neighbour = lowerBound
            lowerBound = ((lowerBound.first + n) to (lowerBound.second + d)).reduced()
            if (lowerBound.second > limit) {
                if (neighbour != null) break else continue
            }
        }
        return neighbour!!
    }

    private fun Pair<Long, Long>.reduced(): Pair<Long, Long> {
        val divisor = gcd(first, second)
        return first / divisor to second / divisor
    }

    /**
     * Rather than compare Doubles, whole numbers are compared based on the property that:
     *
     *      if a/b < n/d, then ad < bn
     */
    private operator fun Pair<Long, Long>.compareTo(other: Pair<Long, Long>): Int {
        val left = this.first.toBigInteger() * other.second.toBigInteger()
        val right = this.second.toBigInteger() * other.first.toBigInteger()
        return if (left < right) -1 else if (left > right) 1 else 0
    }

    private operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>): Pair<Long, Long> {
        val denominator = lcm(this.second, other.second)
        val firstNum = denominator / this.second * this.first
        val secondNum = denominator / other.second * other.first
        return (firstNum - secondNum) to denominator
    }

    /**
     * Solution improved based on the following:
     *
     * For each denominator b up to N, the only fraction that needs to be considered is the one
     * with the largest numerator a for which a/b < n/d.
     *
     *      a/b < n/d becomes ad < bn, which means ad <= bn - 1
     *
     *      a <= floor((bn - 1)/d)
     *
     *      for b <= N, floor((bn - 1)/d)/b is the largest fraction.
     *
     * Fractions with larger denominators are spaced more closely than those with smaller
     * denominators, so iterating backwards starting at N means the largest neighbour below
     * [n]/[d] will be found sooner. The loop is broken based on the aforementioned property that:
     *
     *      the difference between 2 neighbours is given as 1/(db)
     *
     *      for a new fraction r/s to be closer to n/d than a/b ->
     *
     *      1/(ds) < (nb - da)/(db) -> s > b/(nb - da)
     *
     *      if delta = nb - da = 1, this means s > b, & the loop can be broken as all
     *      denominators between b and N have already been examined.
     *
     * SPEED (BEST) 3.3e4ns for N = 1e7
     * SPEED (BETTER) 29.48s for N = 1e15
     *
     * @return pair of (numerator, denominator) representing the fraction to the left of [n]/[d].
     */
    fun leftFareyNeighbourImproved(limit: Long, n: Long, d: Long): Pair<Long, Long> {
        val nBI = n.toBigInteger()
        val dBI = d.toBigInteger()
        var closestNeighbour = 0L to 1L
        var b = limit.toBigInteger() // current denominator
        var minB = 1.toBigInteger()
        while (b >= minB) {
            val a = (b * nBI - 1.toBigInteger()) / dBI // current numerator
            // if closestA/closestB < currentA/currentB,
            // then closestA * currentB < closestB * currentA
            val currentIsLarger = closestNeighbour.first.toBigInteger() * b <
                    closestNeighbour.second.toBigInteger() * a
            if (currentIsLarger) {
                closestNeighbour = (a.longValueExact() to b.longValueExact()).reduced()
                val delta = nBI * b - dBI * a
                minB = b / delta + 1.toBigInteger()
            }
            b--
        }
        return closestNeighbour
    }

    /**
     * Solution optimised by taking advantage of the Extended Euclidean Algorithm that generates
     * coefficients x and y, in addition to the gcd.
     *
     * When a and b are co-prime, x will be the modular multiplicative inverse of a % b and y will
     * be the modular multiplicative inverse of b % a. Remember that the modular multiplicative
     * inverse of an integer a is an integer x such that the product ax is congruent to 1 with
     * respect to the modulus b.
     *
     * SPEED (BETTER) 1.00ms for N = 1e7
     * SPEED (BEST) 9.7e5ns for N = 1e15
     *
     * @return pair of (numerator, denominator) representing the fraction to the left of [n]/[d].
     */
    fun leftFareyNeighbourOptimised(limit: Long, n: Long, d: Long): Pair<Long, Long> {
        // add modulus to handle cases when x is negative, as Kotlin % would return negative
        val modInverseOfN = (extendedGCD(n, d).second % d + d) % d
        var newD = (limit % d) - modInverseOfN
        if (newD < 0) newD += d
        val neighbourDenom = (limit - newD).toBigInteger()
        val neighbourNum = (neighbourDenom * n.toBigInteger() - 1.toBigInteger()) / d.toBigInteger()
        return neighbourNum.longValueExact() to neighbourDenom.longValueExact()
    }

    /**
     * Implements the Extended Euclidean Algorithm that calculates, in addition to the gcd of [n1]
     * and [n2], the coefficients of Bezout's identity, integers x and y such that:
     *
     *      ax + by = gcd(a, b)
     *
     * @return triple of (gcd, x, y).
     * @throws IllegalArgumentException if either [n1] or [n2] are less than 0.
     */
    private fun extendedGCD(n1: Long, n2: Long): Triple<Long, Long, Long> {
        require(n1 >= 0 && n2 >= 0) { "Inputs should not be negative" }
        if (n1 == 0L) {
            return Triple(n2, 0, 1)
        }
        val (eGCD, x, y) = extendedGCD(n2 % n1, n1)
        return Triple(eGCD, y - n2 / n1 * x, x)
    }
}