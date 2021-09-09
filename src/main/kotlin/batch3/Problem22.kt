package batch3

/**
 * Problem 22: Names Scores
 *
 * https://projecteuler.net/problem=22
 *
 * Goal: Given an unsorted list of N names, first sort alphabetically,
 * then, given 1 of the names, multiply the sum of the value of all its
 * characters by its position in the alphabetical list.
 *
 * Constraints: 1 <= N <= 5200
 *
 * e.g.: Input = [ALEX, LUIS, JAMES, BRIAN, PAMELA]
 *       Sorted = [ALEX, BRIAN, JAMES, LUIS, PAMELA]
 *       Name = PAMELA = 16 + 1 + 13 + 5 + 12 + 1 = 48
 *       Position = 1st
 *       Result = 5 * 48 = 240
 */

class NamesScores {
    fun nameScore(index: Int, name: String): Int {
       // Unicode decimal value of 'A' is 65
        return (index + 1) * name.sumOf { it.code - 64 }
    }

    /**
     * Method specific to Project Euler implementation that requires
     * all the name scores for a 5000+ list to be summed.
     */
    fun sumOfNameScores(input: List<String>): Int {
        val names = input.sorted()
        var sum = 0
        names.forEachIndexed { index, name ->
            sum += nameScore(index, name)
        }
        return sum
    }
}