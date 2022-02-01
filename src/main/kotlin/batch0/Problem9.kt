package batch0

import util.maths.gcd
import util.maths.pythagoreanTriplet
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
    private fun Triple<Int, Int, Int>.product(): Int = first * second * third

    fun maxTripletProduct(
        n: Int,
        solution: (Int) -> Triple<Int, Int, Int>?
    ): Int {
        return solution(n)?.product() ?: -1
    }

    /**
     * Solution iterates through values of c and b with some limits:
     *
     * - Set {3,4,5} being the smallest existing triplet, means c >= 5 and can be
     * at most [n]/(2 - 1).
     *
     * - b cannot be <= a.
     *
     * - Triplet elements must either be all evens OR 2 odds with 1 even. Therefore, the sum of a
     * triplet must be even as the sum of evens is an even number and the sum of 2 odds is
     * an even number as well.
     *
     * SPEED (WORST) 4.55ms for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletBruteBC(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        outer@for (c in (n / 2 - 1) downTo 5) {
            val diff = n - c
            for (b in (c - 1) downTo (diff / 2)) {
                val a = diff - b
                if (b <= a) continue@outer
                if (hypot(a.toDouble(), b.toDouble()) == c.toDouble()) {
                    maxTriplet = Triple(a, b, c)
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
     * at most [n]/(3 - 1).
     *
     * - Inserting c = [n] - a - b into the formula a^2 + b^2 = c^2 reduces to:
     *
     *  2ab + 2bn = n^2 - 2an
     *
     *  b = n(n - 2a)/2(n - a)
     *
     * - Exhaustive search shows that the first maximum triplet found will be the only solution,
     * so the loop can be broken early.
     *
     * - Triplet elements must either be all evens OR 2 odds with 1 even. Therefore, the sum of a
     * triplet must be even as the sum of evens is an even number and the sum of 2 odds is
     * an even number as well.
     *
     * SPEED (BETTER) 56600ns for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletBruteA(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        for (a in (n / 3 - 1) downTo 3) {
            val b = n * (n - 2 * a) / (2 * (n - a))
            val c = n - a - b
            if (a < b && hypot(a.toDouble(), b.toDouble()) == c.toDouble()) {
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
     * gcd(a,b,c) = d, such that:
     *
     *  a + b + c = 2m(m + n) * d, with n > m > 0.
     *
     * - A triplet is primitive if m XOR n is even and gcd(m,n) = 1. The latter occurs because
     * gdc(a,b) = gcd(b,c) = gcd(c,a) = 1.
     *
     * - Exhaustive search shows that the first maximum triplet found will be the only solution,
     * so the loop can be broken early.
     *
     * SPEED (BEST): 31500ns for N = 3000
     *
     * @return triple(a, b, c) if one exists, or null.
     */
    fun maxTripletOptimised(num: Int): Triple<Int, Int, Int>? {
        if (num % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        val limit = num / 2
        val mMax = ceil(sqrt(1.0 * limit)).toInt()
        outer@for (m in 2 until mMax) {
            if (limit % m == 0) {
                // find even divisor m (> 1) of num/2
                var kMax = limit / m
                while (kMax % 2 == 0) {
                    // find odd divisor k (= m + n) of num/2m
                    kMax /= 2
                }
                var k = if (m % 2 == 1) m + 2 else m + 1
                while (k < 2 * m && k <= kMax) {
                    if (kMax % k == 0 && gcd(k.toLong(), m.toLong()) == 1L) {
                        maxTriplet = pythagoreanTriplet(m, k - m, limit / (k * m))
                        break@outer
                    }
                    k += 2
                }
            }
        }
        return maxTriplet
    }
}