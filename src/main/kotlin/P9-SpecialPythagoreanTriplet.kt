import util.gcd
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Problem 9: Special Pythagorean Triplet
 * Goal: Given N, with 1 <= N <= 3000, check if there exists any
 * Pythagorean triplet for which a + b + c = N. Find the max product
 * a*b*c or return -1 if no triplets possible.
 * Note that a Pythagorean triplet is a set of 3 natural numbers
 * such that a < b < c && a^2 + b^2 = c^2.
 * e.g. 3^2 + 4^2 = 5^2.
 * A scaled up version of the example would also produce a triplet:
 * 6^2 + 8^2 = 10^2.
 */

fun Triple<Int, Int, Int>.sum(): Int = first + second + third
fun Triple<Int, Int, Int>.product(): Long = 1L * first * second * third

class SpecialPythagoreanTriplet {
    fun maxTripletProduct(n: Int): Long = findTripletsParametrisation(n)?.product() ?: -1L

    /**
     * Optimised performance solution.
     * A primitive Pythagorean triplet has gcd(a,b,c) = 1, as gdc(a,b) =
     * gcd(b,c) = gcd(c,a) = 1. All triplets can be found from 2 numbers
     * m > n > 0, using the following:
     * a = m^2 - n^2, b = 2 * m * n, c = m^2 + n^2; and
     * these triplets will be primitive if exactly one of m or n is
     * even and gcd(m,n) = 1. All triplets can be reduced to a primitive
     * one by dividing out the gcd(a,b,c) = d, such that:
     * a + b + c = 2 * m * (m + n) * d, with m > n > 0, gcd(m, n) = 1
     * and exactly one of m or n being even.
     */
    fun findTripletsParametrisation(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        var maxProduct = 0L
        val nMax = n / 2
        val mMax = ceil(sqrt(1.0 * nMax)).toInt() - 1
        for (m in 2..mMax) {
            if (nMax % m == 0) { // Find even divisor m (> 1) of n/2
                var kMax = nMax / m
                while (kMax % 2 == 0) { // Find odd divisor k (= m + n) of n/2m
                    kMax /= 2
                }
                var k = if (m % 2 == 1) m + 2 else m + 1
                while (k < 2 * m && k <= kMax) {
                    if (kMax % k == 0 && gcd(k, m) == 1) {
                        val triplet = pythagoreanTriplet(m, k - m, nMax / (k * m))
                        if (triplet.sum() == n) {
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

    private fun pythagoreanTriplet(m: Int, n: Int, d: Int): Triple<Int, Int, Int> {
        val a = (m * m - n * n) * d
        val b = 2 * m * n * d
        val c = (m * m + n * n) * d
        return Triple(minOf(a, b), maxOf(a, b), c)
    }

    /**
     * The set of triples must either be all evens OR
     * 2 odds with 1 even. Therefore, the sum of triples
     * will only ever be even numbers as the sum of evens
     * is an even number and the sum of 2 odds is an even
     * number as well.
     */
    fun findTripletsLoop(n: Int): Triple<Int, Int, Int>? {
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

    private fun isPythagoras(a: Int, b: Int, c: Int): Boolean {
        return sqrt(1.0 * a * a + b * b) == 1.0 * c
    }

    /**
     * Based on c >= 5 and a >= 3, with a <= (N - 3)/3 and
     * b < (N - a)/2, the loop can be slightly adjusted.
     */
    fun findTripletsLoopImproved(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        var maxTriplet: Triple<Int, Int, Int>? = null
        var maxProduct = 0L
        for (c in (n / 2 - 1) downTo 5) {
            val diff = n - c
            inner@for (a in (n - 3) / 3 downTo 3) {
                val b = diff - a
                // Will this condition ever be reached?
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
}