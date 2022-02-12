package batch2

/**
 * Problem 22: Names Scores
 *
 * https://projecteuler.net/problem=22
 *
 * Goal: Given an unsorted list of N names, first sort alphabetically, then, given 1 of the
 * names, multiply the sum of the value of all its characters by its position in the alphabetical
 * list.
 *
 * Constraints: 1 <= N <= 5200, len(NAME) < 12
 *
 * e.g.: input = [ALEX, LUIS, JAMES, BRIAN, PAMELA]
 *       sorted = [ALEX, BRIAN, JAMES, LUIS, PAMELA]
 *       name = PAMELA = 16 + 1 + 13 + 5 + 12 + 1 = 48
 *       position = 5th
 *       result = 5 * 48 = 240
 */

class NamesScores {
    /**
     * Project Euler specific implementation that requires all the name scores of a 5000+ list
     * to be summed.
     */
    fun sumOfNameScores(input: List<String>): Int {
        return input
            .sorted()
            .withIndex()
            .sumOf { (i, name) -> nameScore(i, name) }
    }

    /**
     * Helper function returns a score for a name as detailed in documentation above.
     *
     * @param [index] zero-indexed position found in calling function using
     * sortedList.indexOf("name").
     * @param [name] String assumed to be in ALL_CAPS, but this could be ensured by including
     * name.uppercase() in the solution.
     */
    fun nameScore(index: Int, name: String): Int {
        // unicode decimal value of 'A' is 65, but is normalised to represent value 1
        return (index + 1) * name.sumOf { it.code - 64 }
    }
}