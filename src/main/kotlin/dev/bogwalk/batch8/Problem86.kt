package dev.bogwalk.batch8

import dev.bogwalk.util.maths.pythagoreanTriplet

/**
 * Problem 86: Cuboid Route
 *
 * https://projecteuler.net/problem=86
 *
 * Goal: Find the number of distinct cuboids (ignoring rotations) that can be formed (with integer
 * dimensions and shortest route with integer length) up to a maximum size of MxMxM.
 *
 * Constraints: A <= M && B <= M && C <= M
 *              1 <= M <= 4e5
 *
 * This is based on the premise that a spider can sit on the bottom corner of a cuboid room of
 * size 6x5x3, with a fly perched on the opposite ceiling corner. By travelling in the shortest
 * straight line, the spider can reach the fly on a path of distance 10.
 *
 * Considering all cuboid rooms that do not exceed M = 100, the amount that have a shortest path
 * with integer dimensions is 2060.
 *
 * e.g.: M = 99
 *       count = 1975
 */

class CuboidRoute {
    /**
     * The cuboid room can be visualised by flattening it to 2D, showing that the 3 shortest path
     * candidates are formed by the hypotenuses of either a 6x8 right-angled triangle or a 9x5
     * right-angled triangle. Only the 6x8 triangle has an integer hypotenuse (10), which is a
     * Pythagorean triplet.
     *
     * Solution uses a similar approach to the previously determined solutions for finding
     * perimeters of right-angled triangles (Batch 3 - Problem 39, Batch 7 - Problem 75).
     *
     * The amount of distinct cuboids is checked and counted twice to account for different
     * combinations, e.g. Pythagorean triplet (6, 8, 10) produces different sets of cuboids
     * depending on whether the longest side is 6 or 8.
     *
     * Given that the triplet's a (= m^2 - n^2) must be <= [max], setting the outer loop limit to
     * reflect the lowest value of a (so as not to miss any combinations) works fine but produces
     * very slow speeds as M increases. When M = 1e4, it took 46.91s to iterate through the 3 loops,
     * whereas it took the quick draw solution 4.81s to generate all counts accurately up to 4e5.
     */
    fun countDistinctCuboids(max: Int): Long {
        var cuboids = 0L
        var m = 2
        while (m <= (max + 1) / 2) {
            nextN@for (n in 1 until m) {
                var d = 0
                try {
                    while (true) {
                        val triplet = pythagoreanTriplet(m, n, ++d)
                        if (triplet.first > max && triplet.second > max) break
                        if (triplet.first <= max && triplet.first >= triplet.second / 2) {
                            cuboids += countCuboids(triplet.first, triplet.second)
                        }
                        if (triplet.second <= max && triplet.second >= triplet.first / 2) {
                            cuboids += countCuboids(triplet.second, triplet.first)
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    // primitive triplet must have m XOR n as even & m and n co-prime (gcd == 1)
                    continue@nextN
                }
            }
            m++
        }
        return cuboids
    }

    /**
     * Returns a count of distinct cuboid combinations based on the assumption that [a] is the
     * longest side and [b] is a summation of the 2 other sides.
     *
     *      e.g. Pythagorean triplet (6, 8, 10) produces 2 sets of combinations:
     *           {(6, 2, 6), (6, 3, 5), (6, 4, 4)} when a=6 and b=8 &
     *           {(8, 1, 5), (8, 2, 4), (8, 3, 3)} when a=8 and b=6
     */
    private fun countCuboids(a: Int, b: Int): Long {
        return if (a >= b) b / 2L else a - (b - 1) / 2L
    }

    /**
     * Project Euler specific implementation that requires the least value of M such that the
     * count of distinct cuboid rooms first exceeds [count].
     */
    fun getLeastM(count: Int): Int {
        val cumulativeCount = cuboidCountsQuickDraw()
        return cumulativeCount.indexOfFirst { it > count }
    }

    /**
     * Stores the cumulative sum of all distinct cuboid counts for quick access.
     *
     * Note that brute force shows highest m = 1414 for M = 4e5, so max of 1e6 chosen to ensure
     * no combinations are missed.
     *
     * Note that the list of single counts is identical to sequence A143714.
     * @see <a href=">https://oeis.org/A143714">Sequence A143714</a>
     *
     * @return array of possible cuboid combinations for every index = M.
     */
    fun cuboidCountsQuickDraw(): LongArray {
        val maxM = 1_000_000
        val singleCounts = LongArray(maxM + 1)
        var m = 2
        while (m * m / 2 <= maxM) {
            nextN@for (n in 1 until m) {
                var d = 0
                try {
                    while (true) {
                        val triplet = pythagoreanTriplet(m, n, ++d)
                        if (triplet.first > maxM && triplet.second > maxM) break
                        if (triplet.first <= maxM && triplet.first >= triplet.second / 2) {
                            singleCounts[triplet.first] += countCuboids(triplet.first,
                                triplet.second)
                        }
                        if (triplet.second <= maxM && triplet.second >= triplet.first / 2) {
                            singleCounts[triplet.second] += countCuboids(triplet.second,
                                triplet.first)
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    // primitive triplet must have m XOR n as even & m and n co-prime (gcd == 1)
                    continue@nextN
                }
            }
            m++
        }
        // lowest possible M = 3
        var cumulative = 0L
        val allCounts = LongArray(maxM + 1) { i ->
            if (i < 3) 0L else {
                cumulative += singleCounts[i]
                cumulative
            }
        }
        return allCounts
    }
}