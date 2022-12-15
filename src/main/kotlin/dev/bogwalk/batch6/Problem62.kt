package dev.bogwalk.batch6

import dev.bogwalk.util.combinatorics.permutationID

/**
 * Problem 62: Cubic Permutations
 *
 * https://projecteuler.net/problem=62
 *
 * Goal: Given N, find the smallest cubes for which exactly K permutations of its digits are the
 * cube of some number < N.
 *
 * Constraints: 1e3 <= N <= 1e6, 3 <= K <= 49
 *
 * e.g.: N = 1000, K = 3
 *       smallest cube = 41_063_625 (345^3)
 *       permutations -> 56_623_104 (384^3), 66_430_125 (405^3)
 */

class CubicPermutations {
    /**
     * Solution stores all cubes in a dictionary with their permutation id as the key, thereby
     * creating value lists of cubic permutations. The dictionary is then filtered for lists of K
     * size.
     *
     * @return list of all K-sized lists of permutations that are cubes. These will already be
     * sorted by the first (smallest) element of each list.
     */
    fun cubicPermutations(n: Int, k: Int): List<List<Long>> {
        val cubePerms = mutableMapOf<String, List<Long>>()
        for (num in 345 until n) {
            val cube = 1L * num * num * num
            val cubeID = permutationID(cube)
            cubePerms[cubeID] = cubePerms.getOrDefault(cubeID, emptyList()) + cube
        }
        return cubePerms.values.filter { perms -> perms.size == k }
    }

    /**
     * Project Euler specific implementation that requests the smallest cube for which exactly 5
     * permutations of its digits are cube.
     *
     * Since exactly 5 permutations are required, without setting a limit, once the first
     * permutations are found, the loop has to continue until the generated cubes no longer have
     * the same amount of digits as the smallest cube in the permutation list. This ensures the
     * list is not caught early.
     *
     * N.B. The permutations occur between 5027^3 and 8384^3.
     */
    fun smallest5CubePerm(): List<Long> {
        val cubePerms = mutableMapOf<String, List<Long>>()
        var longestID = "0"
        var num = 345L
        var maxDigits = 100
        var currentDigits = 0
        while (currentDigits <= maxDigits) {
            val cube = num * num * num
            val cubeID = permutationID(cube)
            cubePerms[cubeID] = cubePerms.getOrDefault(cubeID, emptyList()) + cube
            if (longestID == "0" && cubePerms[cubeID]?.size == 5) {
                longestID = cubeID
                maxDigits = cubeID.length
            }
            num++
            currentDigits = cubeID.length
        }
        return cubePerms[longestID]!!
    }
}