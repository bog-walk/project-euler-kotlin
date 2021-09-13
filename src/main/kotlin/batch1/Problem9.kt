package batch1

import util.gcd
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Problem 9: Special Pythagorean Triplet
 *
 * https://projecteuler.net/problem=9
 *
 * Goal: If there exists any Pythagorean triplet for which a + b + c = N,
 * find the maximum product among all such triplets, otherwise return -1.
 *
 * Constraints: 1 <= N <= 3000
 *
 * Pythagorean Triplet: a set of 3 natural numbers, such that
 * a < b < c && a^2 + b^2 = c^2.
 *
 * e.g.: N = 12
 *       triplets = {{3,4,5}}; as 3 + 4 + 5 == 12
 *       product = 3*4*5 = 60
 */

fun Triple<Int, Int, Int>.sum(): Int = first + second + third
fun Triple<Int, Int, Int>.product(): Long = 1L * first * second * third

class SpecialPythagoreanTriplet {

    private fun isPythagoras(a: Int, b: Int, c: Int): Boolean {
        return sqrt(1.0 * a * a + b * b) == 1.0 * c
    }

    /**
     * Limits iterations through values of c and b based on:
     * - Set {3,4,5} being the smallest existing triplet, means c >= 5.
     * - b cannot be <= a.
     * - Set of triples must either be all evens OR 2 odds with 1 even.
     * Therefore, the sum of triples will only ever be even numbers as
     * the sum of evens is an even number and the sum of 2 odds is an even
     * number as well.
     */
    fun maxTripletBruteA(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        var maxProduct = 0L
        outer@for (c in (n / 2 - 1) downTo 5) {
            val diff = n - c
            for (b in (c - 1) downTo (diff / 2)) {
                val a = diff - b
                if (b <= a) continue@outer
                if (isPythagoras(a, b, c)) {
                    val triplet = Triple(a, b, c)
                    val product = triplet.product()
                    if (product >= maxProduct) {
                        maxTriplet = triplet
                        maxProduct = product
                    }
                }
            }
        }
        return maxTriplet
    }

    /**
     * Similar iterative approach as above, but loops are adjusted to
     * iterate through values of c and a based on:
     * - Set {3,4,5} being the smallest existing triplet, means c >= 5 and a >= 3.
     * - a must be <= (N - 3)/3 and b cannot be > (N - a)/2.
     * Note that the above iterative option is faster than this
     * alternate option (38ms for N=10000, vs 76ms).
     */
    fun maxTripletBruteB(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        var maxProduct = 0L
        for (c in (n / 2 - 1) downTo 5) {
            val diff = n - c
            inner@for (a in (n - 3) / 3 downTo 3) {
                val b = diff - a
                if (a >= b || b > (n - a) / 2) continue@inner
                if (isPythagoras(a, b, c)) {
                    val triplet = Triple(a, b, c)
                    val product = triplet.product()
                    if (product >= maxProduct) {
                        maxTriplet = triplet
                        maxProduct = product
                    }
                }
            }
        }
        return maxTriplet
    }

    fun maxTripletProduct(n: Int): Long = maxTripletParametrisation(n)?.product() ?: -1L

    /**
     * All Pythagorean triplets can be found from 2 numbers m and n, with m > n > 0.
     * All triplets originate from a primitive one by multiplying them by d = gcd(a,b,c).
     */
    private fun pythagoreanTriplet(m: Int, n: Int, d: Int): Triple<Int, Int, Int> {
        val a = (m * m - n * n) * d
        val b = 2 * m * n * d
        val c = (m * m + n * n) * d
        return Triple(minOf(a, b), maxOf(a, b), c)
    }

    /**
     * Optimised solution (2ms for N=10000) based on:
     * - A primitive Pythagorean triplet having gcd(a,b,c) = 1,
     * as gdc(a,b) = gcd(b,c) = gcd(c,a) = 1.
     * - A triplet being primitive if m XOR n is even and gcd(m,n) = 1.
     * - All triplets can be reduced to a primitive one by dividing out
     * the gcd(a,b,c) = d, such that:
     * a + b + c = 2 * m * (m + n) * d, with n > m > 0.
     */
    fun maxTripletParametrisation(num: Int): Triple<Int, Int, Int>? {
        if (num % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        var maxProduct = 0L
        val limit = num / 2
        val mMax = ceil(sqrt(1.0 * limit)).toInt() - 1
        for (m in 2..mMax) {
            if (limit % m == 0) { // Find even divisor n (> 1) of num/2
                var kMax = limit / m
                while (kMax % 2 == 0) { // Find odd divisor k (= m + n) of num/2m
                    kMax /= 2
                }
                var k = if (m % 2 == 1) m + 2 else m + 1
                while (k < 2 * m && k <= kMax) {
                    if (kMax % k == 0 && gcd(k.toLong(), m.toLong()) == 1L) {
                        val triplet = pythagoreanTriplet(m, k - m, limit / (k * m))
                        if (triplet.sum() == num) {
                            val product = triplet.product()
                            if (product >= maxProduct) {
                                maxProduct = product
                                maxTriplet = triplet
                            }
                        }
                    }
                    k += 2
                }
            }
        }
        return maxTriplet
    }
}