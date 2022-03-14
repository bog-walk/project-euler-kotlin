package batch7

import util.maths.pythagoreanTriplet
import util.maths.sum

/**
 * Problem 75: Singular Integer Right Triangles
 *
 * https://projecteuler.net/problem=75
 *
 * Goal: Given a length L, find how many values of L <= N can form exactly 1 integer-sided
 * right-angle triangle.
 *
 * Constraints: 12 <= N <= 5e6
 *
 * 12 is the smallest length (of wire e.g. if attempting to bend it into a triangle) that can
 * form an integer-sided right-angle triangle in exactly 1 way. In contrast, some lengths, like
 * 20, cannot form such a triangle, and other lengths, like 120, allow the formation of such a
 * triangle in multiple ways -> {120: (30,40,50), (20,48,52), (24,45,51)}.
 *
 * e.g.: N = 50
 *       count = 6
 *       {12: (3,4,5), 24: (6,8,10), 30: (5,12,13), 36: (9,12,15), 40: (8, 15,17), 48: (12,16,20)}
 */

class SingularIntegerRightTriangles {
    /**
     * Solution is identical to the previously determined solution for finding the smallest
     * perimeter of integer right-angle triangles (Batch 3 - Problem 39).
     *
     * Euclid's formula generates all primitive Pythagorean triplets & stores the count of
     * triplets for every length. This cache is then converted to another that accumulates the
     * count of singular triplet lengths below the given [limit].
     */
    fun singularTriplets(limit: Int): Int {
        val pSols = IntArray(limit + 1)
        var m = 2
        // upper bound represents p = 2dm(m + n); i.e. Euclid's formula inserted into perimeter()
        while (2 * m * m < limit) {
            nextN@for (n in 1 until m) {
                var d = 1
                try {
                    while (true) {
                        val p = pythagoreanTriplet(m, n, d).sum()
                        if (p > limit) break
                        pSols[p] += 1
                        d++
                    }
                } catch (e: IllegalArgumentException) {
                    // primitive triplet must have m XOR n as even & m and n co-prime (gcd == 1)
                    continue@nextN
                }
            }
            m++
        }
        var cumulative = 0
        val singularCount = IntArray(limit + 1) { i ->
            if (i < 12) 0 else {
                if (pSols[i] == 1) cumulative++
                cumulative
            }
        }
        return singularCount[limit]
    }
}