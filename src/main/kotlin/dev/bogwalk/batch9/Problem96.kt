package dev.bogwalk.batch9

import dev.bogwalk.util.custom.SuDokuGame

/**
 * Problem 96: Su Doku
 *
 * https://projecteuler.net/problem=96
 *
 * Goal: Given an unsolved representation of a Su Doku puzzle that has a unique solution, output
 * the solved puzzle with all empty spaces ('0') replaced with the correct digit.
 *
 * Constraints: Standard Su Doku 9x9 grid, split into 9 3x3 boxes, with each row, column, and 3x3
 * box expected to contain the set of digits [1, 9].
 */

class SuDoku {
    /**
     * Project Euler specific implementation that requires all 50 Su Doku puzzles in a file to be
     * solved, so that the 3-digit number in the top-left corner of every solution can be summed.
     *
     * Puzzles that require 1 guess -> 7, 42, 48, 49, 50.
     */
    fun solveAllSuDoku(puzzles: List<SuDokuGame>): Int {
        var sum = 0
        for ((i, puzzle) in puzzles.withIndex()) {
            if (puzzle.solve()) {
                val solution = puzzle.getGrid()
                if (solution.any { '0' in it }) {
                    println("Puzzle ${i+1} has empty cell in it")
                }
                if (solution.any { it.toSet().size != 9 }) {
                    println("Puzzle ${i+1} has invalid repeats")
                }
                sum += solution.first().take(3).toInt()
            } else {
                println("Puzzle ${i+1} failed")
            }
        }
        return sum
    }
}