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

fun Triple<Int, Int, Int>.product(): Long = 1L * first * second * third

class SpecialPythagoreanTriplet {
    fun maxTripletProduct(n: Int): Long = findTriplets(n)?.product() ?: -1L

    /**
     * The set of triples must either be all evens OR
     * 2 odds with 1 even. Therefore, the sum of triples
     * will only ever be even numbers as the sum of evens
     * is an even number and the sum of 2 odds is an even
     * number as well.
     */
    fun findTriplets(n: Int): Triple<Int, Int, Int>? {
        if (n % 2 != 0) return null
        outer@for (c in (n / 2 - 1) downTo 5) {
            val diff = n - c
            for (b in (c - 1) downTo (diff / 2)) {
                val a = diff - b
                if (b <= a) continue@outer
                if (isPythagoras(a, b, c)) {
                    return Triple(a, b, c)
                }
            }
        }
        return null
    }

    private fun isPythagoras(a: Int, b: Int, c: Int): Boolean {
        return sqrt(1.0 * a * a + b * b) == 1.0 * c
    }
}