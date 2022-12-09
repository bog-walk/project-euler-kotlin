package dev.bogwalk.batch0

import dev.bogwalk.util.maths.isCoPrime
import dev.bogwalk.util.maths.pythagoreanTriplet
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.math.hypot

/**
 * Problem 9: Special Pythagorean Triplet
 *
 * https://projecteuler.net/problem=9
 *
 * Goal: If there exists any Pythagorean triplet for which a + b + c = N, find the maximum
 * product among all such triplets, otherwise return -1.
 *
 * Constraints: 1 <= N <= 3000
 *
 * Pythagorean Triplet: A set of 3 natural numbers, such that a < b < c && a^2 + b^2 = c^2.
 *
 * e.g.: N = 12
 *       triplets = {{3,4,5}}; as 3 + 4 + 5 == 12
 *       product = 3*4*5 = 60
 */

class SpecialPythagoreanTriplet {
    fun maxTripletProduct(
        n: Int,
        solution: (Int) -> Triple<Int, Int, Int>?
    ): Int {
        return solution(n)?.product() ?: -1
    }

    private fun Triple<Int, Int, Int>.product(): Int = first * second * third

    /**
     * Solution iterates through values of c and b with some limits:
     *
     * - c > [n]/3  and can be at most ([n]/2) - 1 (latter proved if c is replaced by its
     * Pythagorean equation -> sqrt(a^2 + b^2) < a + b).
     *
     * - b cannot be <= a.
     *
     * - Triplet elements must either be all evens OR 2 odds with 1 even. Therefore, the sum of a
     * triplet must be even as the sum of evens is an even number and the sum of 2 odds is
     * an even number as well.
     *
     * - The product of the 2 non-hypotenuse sides (a, b) must be divisible by 12.
     *
     * SPEED (WORST) 2.03ms for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletBruteBC(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        nextC@for (c in (n / 2 - 1) downTo (n / 3 + 1)) {
            val diff = n - c
            nextB@for (b in (c - 1) downTo (diff / 2)) {
                val a = diff - b
                if (b <= a) continue@nextC
                if (a * b % 12 != 0) continue@nextB
                if (hypot(a.toDouble(), b.toDouble()) == c.toDouble()) {
                    val current = Triple(a, b, c)
                    if (maxTriplet == null || current.product() > maxTriplet.product()) {
                        maxTriplet = current
                    }
                    break
                }
            }
        }
        return maxTriplet
    }
    /**
     * Solution iterates through values of a only based on:
     *
     * - Set {3,4,5} being the smallest existing triplet, so a must be >= 3 and can be
     * at most ([n]/3) - 1. Based on a < b and a < c, so 2a < b + c -> 3a < a + b + c.
     *
     * - Inserting c = [n] - a - b into the formula a^2 + b^2 = c^2 reduces to:
     *
     *          2ab + 2bn = n^2 - 2an
     *
     *          b = n(n - 2a)/2(n - a)
     *
     * - Exhaustive search shows that the first maximum triplet found will be the only solution,
     * so the loop can be broken early.
     *
     * - Triplet elements must either be all evens OR 2 odds with 1 even. Therefore, the sum of a
     * triplet must be even as the sum of evens is an even number and the sum of 2 odds is
     * an even number as well.
     *
     * - The product of the 2 non-hypotenuse sides (a, b) must be divisible by 12.
     *
     * SPEED (BETTER) 4.8e4ns for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletBruteA(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        for (a in (n / 3 - 1) downTo 3) {
            val b = n * (n - 2 * a) / (2 * (n - a))
            if (a >= b || a * b % 12 != 0) continue
            val c = n - a - b
            if (hypot(a.toDouble(), b.toDouble()) == c.toDouble()) {
                maxTriplet = Triple(a, b, c)
                break
            }
        }
        return maxTriplet
    }

    /**
     * Solution optimised based on:
     *
     * - All Pythagorean triplets can be reduced to a primitive one by dividing out the
     * gcd(a,b,c) = d, and inserting Euclid's formula equations for each side, such that:
     *
     *          a + b + c = 2m(m + n) * d, with m > n > 0.
     *
     * - A triplet is primitive if only 1 of m or n is even and gcd(m,n) = 1. The latter
     * occurs because gdc(a,b) = gcd(b,c) = gcd(c,a) = 1.
     *
     * SPEED (BEST): 3.5e4ns for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletOptimised(num: Int): Triple<Int, Int, Int>? {
        if (num % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        val limit = num / 2
        val mMax = ceil(sqrt(1.0 * limit)).toInt()
        for (m in 2 until mMax) {
            if (limit % m == 0) { // found even divisor m (> 1) of num/2
                // find opposite parity divisor k (= m + n) of num/2m
                var k = if (m % 2 == 1) m + 2 else m + 1
                var kMax = limit / m
                while (kMax % 2 == 0) {
                    kMax /= 2
                }
                while (k < 2 * m && k <= kMax) {
                    if (kMax % k == 0 && isCoPrime(k, m)) {
                        val current = pythagoreanTriplet(m, k - m, limit / (k * m))
                        if (maxTriplet == null || current.product() > maxTriplet.product()) {
                            maxTriplet = current
                        }
                    }
                    k += 2
                }
            }
        }
        return maxTriplet
    }
}