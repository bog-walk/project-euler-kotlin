package dev.bogwalk.batch6

import dev.bogwalk.util.combinatorics.permutations

/**
 * Problem 68: Magic 5-Gon Ring
 *
 * https://projecteuler.net/problem=68
 *
 * Goal: Given N, representing a magic N-gon ring, and S, representing the total to which each
 * ring's line sums, return all concatenated strings (in lexicographic order) representing the N
 * sets of solutions.
 *
 * Constraints: 3 <= N <= 10
 *
 * Magic 3-Gon Ring (as seen in above link): The ring has 3 external nodes, each ending a line of
 * 3 nodes (6 nodes total representing digits 1 to 6). The ring can be completed so the lines
 * reach 4 different totals, [9, 12], so there are 8 solutions in total:
 *      9 -> [{4,2,3}, {5,3,1}, {6,1,2}], [{4,3,2}, {6,2,1}, {5,1,3}]
 *      10 -> [{2,3,5}, {4,5,1}, {6,1,3}], [{2,5,3}, {6,3,1}, {4,1,5}]
 *      11 -> [{1,4,6}, {3,6,2}, {5,2,4}], [{1,6,4}, {5,4,2}, {3,2,6}]
 *      12 -> [{1,5,6}, {2,6,4}, {3,4,5}], [{1,6,5}, {3,5,4}, {2,4,6}]
 *      So the maximum concatenated string for a magic 3-gon is "432621513".
 *
 * e.g.: N = 3, S = 9
 *       solutions = ["423531612", "432621513"]
 */

class Magic5GonRing {
    /**
     * Project Euler specific implementation that requests the maximum 16-digit string solution
     * from all solutions for a magic 5-gon ring that uses numbers 1 to 10 in its nodes.
     *
     * For the solution to have 16 digits, the number 10 has to be an external node, as, if it
     * were placed on the internal ring, it would be used by 2 unique lines & would create a
     * 17-digit concatenation. So, at minimum, the line with 10 as an external node will sum to 13.
     *
     * To create the largest lexicographic concatenation, larger numbers ideally should be placed
     * first. Based on the magic 3-gon ring example detailed in the problem description, if the
     * largest numbers are external nodes and the smallest numbers are ring nodes, the maximum
     * solution occurs at the lowest total achieved by the following:
     *
     *      (externalNodes.sum() + 2 * internalNodes.sum()) / N
     *
     *      e.g. ([4, 5, 6].sum() + 2 * [1, 2, 3].sum()) / 3 = 9
     *
     * The maximum solution indeed occurs when S = 14 with the largest numbers on the external
     * nodes, but all totals up to 27 were explored given the speed of the recursive solution below.
     *
     * N.B. Solutions exist for the following totals: [14, 16, 17, 19].
     */
    fun maxMagic5GonSolution(): String {
        val solutions = mutableListOf<List<String>>()
        for (s in 13..27) {
            val solution = magicRingSolutionsOptimised(5, s)
            if (solution.isNotEmpty()) {
                solutions.add(solution)
            }
        }
        return solutions.flatten().filter { it.length == 16 }.sortedDescending()[0]
    }

    /**
     * Solution uses recursion to search through all permutations ([n] * 2 digits choose 3) from an
     * increasingly smaller set of remaining digits. Permutations are checked to see if they sum
     * to [s] & if their middle digit matches the last digit of the permutation found previously.
     *
     * A stack of the remaining digits to use is cached so the search can continue if a solution
     * that cannot close the ring is reached. This is done by adding the elements of the last
     * permutation back to the stack if the solution becomes invalid.
     *
     * Rather than searching for a final permutation that completes the ring, the expected list is
     * generated based on the remaining digits possible & checked to see if it complements the
     * starter list & adds up to [s].
     *
     * SPEED (WORST) 1.57s for N = 7, S = 23
     */
    fun magicRingSolutions(n: Int, s: Int): List<String> {
        val solutions = mutableListOf<String>()
        val allDigits = (1..n * 2).toMutableSet()

        fun nextRingLine(solution: MutableList<List<Int>>) {
            if (allDigits.size == 2) {
                val expected = listOf(
                    (allDigits - setOf(solution.last()[2])).first(),
                    solution.last()[2],
                    solution[0][1]
                )
                if (expected[0] > solution[0][0] && expected.sum() == s) {
                    solutions.add(
                        solution.flatten().joinToString("") +
                                expected.joinToString("")
                    )
                }
            } else {
                for (perm in permutations(allDigits, 3)) {
                    if (
                        perm[1] != solution.last()[2] ||
                        perm[0] < solution[0][0] ||
                        perm.sum() != s
                    ) continue
                    solution.add(perm)
                    allDigits.removeAll(perm.toSet() - setOf(perm[2]))
                    nextRingLine(solution)
                }
            }
            allDigits.addAll(solution.removeLast())
        }

        // starter must have the lowest external node, which limits the digits it can have
        val starterMax = n * 2 - n + 1
        for (starter in permutations(allDigits, 3)) {
            if (starter[0] > starterMax || starter.sum() != s) continue
            allDigits.removeAll(starter.toSet() - setOf(starter[2]))
            nextRingLine(mutableListOf(starter))
            allDigits.clear()
            allDigits.addAll((1..n * 2).toMutableSet())
        }
        return solutions.sorted()
    }

    /**
     * While still using recursion, the solution is optimised by not generating all permutations
     * of 3-digit lines. Instead, nodes on the internal ring alone are recursively generated from
     * an increasingly smaller set of available digits and the corresponding external node for
     * every pair of ring nodes is calculated.
     *
     * SPEED (BETTER) 66.16ms for N = 7, S = 23
     */
    fun magicRingSolutionsImproved(n: Int, s: Int): List<String> {
        val solutions = mutableListOf<String>()
        val allDigits = (1..n * 2)
        val remainingDigits = allDigits.toMutableSet()
        val ringNodes = IntArray(n)
        val externalNodes = IntArray(n)

        fun nextRingNode(i: Int) {
            if (i == n - 1) {
                externalNodes[i] = s - ringNodes[i] - ringNodes[0]
                if (
                    externalNodes[i] in remainingDigits &&
                    // first external node must be smallest of all external nodes
                    externalNodes[0] == externalNodes.minOrNull()
                ) {
                    var solution = ""
                    for (j in 0 until n) {
                        solution += "${externalNodes[j]}${ringNodes[j]}${ringNodes[(j+1)%n]}"
                    }
                    solutions.add(solution)
                }
            } else {
                for (digit in allDigits) {
                    if (digit !in remainingDigits) continue
                    val nextExternal = s - ringNodes[i] - digit
                    if (nextExternal == digit || nextExternal !in remainingDigits) continue
                    ringNodes[i+1] = digit
                    externalNodes[i] = nextExternal
                    val justUsed = setOf(digit, nextExternal)
                    remainingDigits.removeAll(justUsed)
                    nextRingNode(i + 1)
                    // solution found or not; either way backtrack to try a different ring node
                    remainingDigits.addAll(justUsed)
                }
            }
        }

        for (digit in allDigits) {
            ringNodes[0] = digit
            remainingDigits.remove(digit)
            nextRingNode(0)
            // solutions found or not; either way backtrack to start ring with a novel node
            remainingDigits.clear()
            remainingDigits.addAll(allDigits.toMutableSet())
        }
        return solutions.sorted()
    }

    /**
     * This solution is identical to the above improved solution but cuts speed in half by taking
     * advantage of the pattern that all solutions present in pairs.
     *
     * e.g. when N = 4, S = 12, the 1st solution will be found when the starter digit = 2, with
     *
     *      ringNodes = [2, 6, 1 ,3] and externalNodes = [4, 5, 8, 7] -> "426561813732"
     *
     * If the 1st 2 elements of ringNodes are swapped & the rest reversed, and all elements of
     * externalNodes, except the static lowest external, are also reversed, the arrays become:
     *
     *      ringNodes = [6, 2, 3, 1] and externalNodes = [4, 7, 8, 5] -> "462723831516"
     *
     * This latter solution would have been eventually found when the starter digit = 6.
     *
     * Instead, any duplicate searches are eliminated by reversing all solutions when found & not
     * allowing starter digits to explore adjacent ring digits that are smaller (as these would
     * already have been found by previous iterations). So the starter digit 6 would only end up
     * searching through ringNodes = [6, [7, 12], X, X]
     *
     * Lastly, neither digit 1 nor digit n need to be assessed as they will be found in later or
     * earlier iterations.
     *
     * SPEED (BEST) 36.84ms for N = 7, S = 23
     */
    fun magicRingSolutionsOptimised(n: Int, s: Int): List<String> {
        val solutions = mutableListOf<String>()
        val allDigits = (1..n * 2)
        val remainingDigits = allDigits.toMutableSet()
        val ringNodes = IntArray(n)
        val externalNodes = IntArray(n)

        fun nextRingNode(i: Int) {
            if (i == n - 1) {
                externalNodes[i] = s - ringNodes[i] - ringNodes[0]
                if (
                    externalNodes[i] in remainingDigits &&
                    externalNodes[0] == externalNodes.minOrNull()
                ) {
                    var solution1 = ""
                    var solution2 = ""
                    for (j in 0 until n) {
                        solution1 += "${externalNodes[j]}${ringNodes[j]}${ringNodes[(j+1)%n]}"
                        solution2 += "${externalNodes[(n-j)%n]}${ringNodes[(n-j+1)%n]}${ringNodes[(n-j)%n]}"
                    }
                    solutions.add(solution1)
                    solutions.add(solution2)
                }
            } else {
                val searchRange = if (i == 0) (ringNodes[0] + 1)..(n * 2) else allDigits
                for (digit in searchRange) {
                    if (digit !in remainingDigits) continue
                    val nextExternal = s - ringNodes[i] - digit
                    if (nextExternal == digit || nextExternal !in remainingDigits) continue
                    ringNodes[i+1] = digit
                    externalNodes[i] = nextExternal
                    val justUsed = setOf(digit, nextExternal)
                    remainingDigits.removeAll(justUsed)
                    nextRingNode(i + 1)
                    remainingDigits.addAll(justUsed)
                }
            }
        }

        for (digit in 2 until n * 2) {
            ringNodes[0] = digit
            remainingDigits.remove(digit)
            nextRingNode(0)
            remainingDigits.clear()
            remainingDigits.addAll(allDigits.toMutableSet())
        }
        return solutions.sorted()
    }
}