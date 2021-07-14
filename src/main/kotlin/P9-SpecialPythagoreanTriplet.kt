import kotlin.math.sqrt

/**
 * Problem 9: Special Pythagorean Triplet
 * Goal: Given N, with 1 <= N <= 3000, check if there exists any
 * Pythagorean triplet for which a + b + c = N. Find the max product
 * a*b*c or return -1 if no triplets possible.
 * Note that a Pythagorean triplet is a set of 3 natural numbers
 * such that a < b < c && a^2 + b^2 = c^2.
 * e.g. 3^2 + 4^2 = 5^2.
 * Test: 12 -> product = 60 (above 3 * 4 * 5). 4 -> product = -1.
 */

fun Triple<Int, Int, Int>.product(): Int = first * second * third

class SpecialPythagoreanTriplet {
    fun maxTripletProduct(n: Int): Int = findTriplets(n)?.product() ?: -1

    fun findTriplets(n: Int): Triple<Int, Int, Int>? {
        outer@for (c in n - 3 downTo 3) {
            val diff = n - c
            for (a in 1 until diff) {
                val b = diff - a
                if (a >= b) continue@outer
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