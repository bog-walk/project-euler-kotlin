package dev.bogwalk.batch8

import dev.bogwalk.util.combinatorics.combinations

/**
 * Problem 90: Cube Digit Pairs
 *
 * https://projecteuler.net/problem=90
 *
 * Goal: Count the distinct arrangements of M cubes that allow for all square numbers [1, N^2] to
 * be displayed.
 *
 * Constraints: 1 <= M <= 3, 1 <= N < 10^(M/2)
 *
 * The 6 faces of each cube has a different digit [0, 9]] on it. Placing the cubes side-by-side
 * in any order creates a variety of M-digit square numbers <= N^2. For example, when 2 cubes
 * are used, the arrangement {0, 5, 6, 7, 8, 9} and {1, 2, 3, 4, 6, 7} allows all squares <= 81;
 * Note that "09" is achievable because '6' can be flipped to represent '9'.
 *
 * Arrangements are considered distinct if the combined cubes have different numbers,
 * regardless of differing order. Note that cubes can share the same numbers as long as they
 * still are able to display all expected squares.
 *
 * e.g.: M = 1, N = 3
 *       Cube must have {1, 4, 6} or {1, 4, 9} at minimum
 *       Of all possible digit combinations, 55 match this requirement
 */

class CubeDigitPairs {
    fun countValidCubes(maxSquare: Int, cubes: Int): Int {
        val squares = getSquares(maxSquare, cubes)
        var count = 0

        // '9' is represented by '6' for ease of validity check within block
        val allCubes = combinations("0123456786".toList(), 6).toList()
        // avoid duplicate arrangements by limiting iteration to only larger cubes
        for ((i, cube1) in allCubes.withIndex()) {
            if (cubes > 1) {
                // must include equivalent cubes for cases when N is lower
                for (j in i..allCubes.lastIndex) {
                    val cube2 = allCubes[j]
                    // at least 1 cube must have '0' by now, regardless of N value
                    // any further combos will be larger and not include '0'
                    if (cube1[0] != '0' && cube2[0] != '0') break
                    if (cubes > 2) {
                        for (k in j..allCubes.lastIndex) {
                            val cube3 = allCubes[k]
                            // both smaller cubes must have '0' as cube3 will be larger
                            if ((cube1[0] == '0') xor (cube2[0] == '0')) break
                            if (squares.all {
                                    it[0] in cube1 && it[1] in cube2 && it[2] in cube3 ||
                                            it[0] in cube1 && it[1] in cube3 && it[2] in cube2 ||
                                            it[0] in cube2 && it[1] in cube1 && it[2] in cube3 ||
                                            it[0] in cube2 && it[1] in cube3 && it[2] in cube1 ||
                                            it[0] in cube3 && it[1] in cube2 && it[2] in cube1 ||
                                            it[0] in cube3 && it[1] in cube1 && it[2] in cube2
                                }) {
                                count++
                            }
                        }
                    } else {
                        // 2 cubes cannot be equivalent when expected to show squares >= 36
                        // as amount of necessary digits will exceed 6
                        if (maxSquare > 5 && cube1 == cube2) continue
                        // 2 cubes cannot display > 9^2
                        if (squares.all {
                                it[0] in cube1 && it[1] in cube2 || it[0] in cube2 && it[1] in cube1
                            }) {
                            count++
                        }
                    }
                }
            } else {
                // one cube cannot display > 3^2
                if (squares.all { it[0] in cube1 }) count++
            }
        }

        return count
    }

    /**
     * Generates a list of square numbers with all '9' represented by '6' and appropriately padded
     * with leading zeroes.
     *
     * Note that certain squares have been removed to avoid redundancy:
     *      8^2 = 64 = 46
     *      10^2 = 100 = 001
     *      14^2 = 196 = 31^2 = 961 = 166
     *      20^2 = 400 = 004
     *      21^2 = 441 = 144
     *      23^2 = 529 = 25^2 = 625 = 256
     *      30^2 = 900 = 006
     */
    private fun getSquares(maxN: Int, cubes: Int): List<String> {
        val squares = mutableListOf<String>()

        for (i in 1..maxN) {
            if (i in listOf(8, 10, 14, 20, 21, 23, 25, 30, 31)) continue
            squares.add((i * i).toString()
                .replace('9', '6')
                .padStart(cubes, '0')
            )
        }

        return squares
    }
}