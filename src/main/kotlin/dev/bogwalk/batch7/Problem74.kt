package dev.bogwalk.batch7

import dev.bogwalk.util.combinatorics.permutationID
import dev.bogwalk.util.maths.factorial

/**
 * Problem 74: Digit Factorial Chains
 *
 * https://projecteuler.net/problem=74
 *
 * Goal: For a given length L and limit N, return all integers <= N that start a digit factorial
 * non-repeating chain of length L.
 *
 * Constraints: 10 <= N <= 1e6, 1 <= L <= 60
 *
 * Other than the factorions 145 & 40585 that loop with themselves (1! + 4! + 5! = 145), only 3
 * loops exist:
 *
 *      169 -> 363601 -> 1454 -> 169
 *      871 -> 45361 -> 871
 *      872 -> 45362 -> 872
 *
 * Every starting number eventually becomes stuck in a loop, e.g:
 *
 *      69 -> 363600 -> 1454 -> 169 -> 363601 (-> 1454)
 *
 * The above chain is of length 5 and the longest non-repeating chain with a starting number <
 * 1e6 has 60 terms.
 *
 * e.g.: N = 221, L = 7
 *       output = {24, 42, 104, 114, 140, 141}
 */

class DigitFactorialChains {
    // pre-calculation of all digit factorials to increase performance
    private val factorials = List(10) { it.factorial().intValueExact() }
    // store all special case numbers that either loop to themselves (factorions) or to others
    private val loopNums = listOf(145, 169, 871, 872, 1454, 40_585, 45_361, 45_362, 363_601)
    // pre-generate the id of these special case numbers
    private val loopPerms = loopNums.map { permutationID(it.toLong()).joinToString("") }

    /**
     * Solution based on the following:
     *
     *  - 1! & 2! will cause an infinite loop with themselves. The only non-single digit factorions
     *  are: 145 and 40_585.
     *
     *  - If a chain encounters an element in the 3 aforementioned loops or a factorion, it will
     *  become stuck & the search can be broken early without waiting for a repeated element to
     *  be found.
     *
     *  - A starter will also be discarded early if it has not encountered a loop but has
     *  exceeded the requested [length].
     *
     *  - A cache of permutationID(n) to chain length is created to reduce digit factorial sum
     *  calculations of every starter <= [limit].
     *
     *  - Getting the permutation of a number & checking it against the class variables of
     *  special case numbers prevents wrong results caused by permutations that don't actually
     *  start loops. e.g. 27 -> 5042 -> 147 -> 5065 -> 961 -> {363_601 -> loop}. 961 has the same
     *  id as 169 but technically does not start the loop, so the count must be incremented.
     *
     *  - All new chain elements (less than [limit] & up until a loop element is encountered) are
     *  stored so that they can be cached with a count using backtracking. e.g. 69 -> 363_600 ->
     *  loop element = count 5; so cache[id(363_600)] = 4 & cache[id(69)] = 5.
     *
     *  SPEED (WORSE) 1.41s for N = 1e6, L = 10
     */
    fun digitFactorialChainStarters(limit: Int, length: Int): List<Int> {
        val starters = mutableListOf<Int>()
        val cache = mutableMapOf<String, Int>().apply {
            this[permutationID(1L).joinToString("")] = 1
            this[permutationID(2L).joinToString("")] = 1
            this[loopPerms[0]] = 1
            this[loopPerms[1]] = 3
            this[loopPerms[2]] = 2
            this[loopPerms[3]] = 2
            this[loopPerms[4]] = 3
            this[loopPerms[5]] = 1
            this[loopPerms[6]] = 2
            this[loopPerms[7]] = 2
            this[loopPerms[8]] = 3
        }
        nextStarter@for (n in 0..limit) {
            var permID = permutationID(n.toLong()).joinToString("")
            if (permID in cache.keys) {
                var cachedLength = cache.getOrDefault(permID, 0)
                if (permID in loopPerms && n !in loopNums) cachedLength++
                if (cachedLength == length) starters.add(n)
                continue@nextStarter
            }
            val chain = mutableListOf<String?>(permID)
            var count = 1
            var prev = n
            while (true) {
                prev = prev.toString().sumOf { ch -> factorials[ch.digitToInt()] }
                permID = permutationID(prev.toLong()).joinToString("")
                if (permID in cache.keys) {
                    count += cache.getOrDefault(permID, 0)
                    if (permID in loopPerms && prev !in loopNums) count++
                    break
                } else {
                    if (prev <= limit) chain.add(permID) else chain.add(null)
                    count++
                    if (count > length) continue@nextStarter
                }
            }
            for ((i, element) in chain.withIndex()) {
                element?.let {
                    cache[element] = count - i
                }
            }
            if (count == length) starters.add(n)
        }
        return starters
    }

    /**
     * Solution improved by not checking every chain element for a match with a special case loop
     * starter. The search is instead stopped early if the element cannot be added to the chain
     * set because it already exists.
     *
     * This forces the cache to have to store the special case number count values already falsely
     * incremented & to catch these special cases again before checking final count length.
     *
     * SPEED (BETTER) 855.24ms for N = 1e6, L = 10
     */
    fun digitFactorialChainStartersImproved(limit: Int, length: Int): List<Int> {
        val starters = mutableListOf<Int>()
        val cache = mutableMapOf<String, Int>().apply {
            this[loopPerms[0]] = 2 // 145
            this[loopPerms[1]] = 4 // 169
            this[loopPerms[5]] = 2 // 40585
        }
        for (n in 0..limit) {
            val permId = permutationID(n.toLong()).joinToString("")
            if (permId !in cache.keys) {
                val chain = mutableSetOf<Int>()
                var prev = n
                while (chain.add(prev) && chain.size <= length) {
                    prev = prev.toString().sumOf { ch -> factorials[ch.digitToInt()] }
                }
                cache[permId] = chain.size
            }
            val valid = when (n) {
                145, 40_585 -> length == 1
                169, 1454, 363_601 -> length == 3
                871, 872, 45_361, 45_362 -> length == 2
                else -> cache[permId] == length
            }
            if (valid) starters.add(n)
        }
        return starters
    }

    /**
     * Solution identical to the original except that it does not attempt to add all chain
     * elements to the cache, only the starter element.
     *
     * Optimise this solution even further by creating an array of size 60 with each index
     * representing [length] - 1 & storing a list of starters that produce this chain length. The
     * array would be propagated in the main loop by not breaking the loop for a set length &
     * adding each n to this array instead of the starters list. That way, the array can be made
     * for all 1e6 starters & multiple test cases can be picked by simply filtering the list
     * stored at index [length] - 1.
     *
     * SPEED (BETTER) 529.66ms for N = 1e6, L = 10
     */
    fun digitFactorialChainStartersOptimised(limit: Int, length: Int): List<Int> {
        val starters = mutableListOf<Int>()
        val cache = mutableMapOf<String, Int>().apply {
            this[permutationID(1L).joinToString("")] = 1
            this[permutationID(2L).joinToString("")] = 1
            this[loopPerms[0]] = 1
            this[loopPerms[1]] = 3
            this[loopPerms[2]] = 2
            this[loopPerms[3]] = 2
            this[loopPerms[4]] = 3
            this[loopPerms[5]] = 1
            this[loopPerms[6]] = 2
            this[loopPerms[7]] = 2
            this[loopPerms[8]] = 3
        }
        nextStarter@for (n in 0..limit) {
            val nPermID = permutationID(n.toLong()).joinToString("")
            if (nPermID !in cache.keys) {
                var count = 1
                var prev = n
                while (true) {
                    prev = prev.toString().sumOf { ch -> factorials[ch.digitToInt()] }
                    val permID = permutationID(prev.toLong()).joinToString("")
                    if (permID in cache.keys) {
                        count += cache.getOrDefault(permID, 0)
                        if (permID in loopPerms && prev !in loopNums) count++
                        break
                    } else {
                        count++
                        if (count > length) continue@nextStarter
                    }
                }
                cache[nPermID] = count
            }
            var cachedLength = cache.getOrDefault(nPermID, 0)
            if (nPermID in loopPerms && n !in loopNums) cachedLength++
            if (cachedLength == length) starters.add(n)
        }
        return starters
    }
}