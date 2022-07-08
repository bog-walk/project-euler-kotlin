package dev.bogwalk.batch8

import dev.bogwalk.util.custom.MonopolyGame

/**
 * Problem 84: Monopoly Odds
 *
 * https://projecteuler.net/problem=84
 *
 * Goal: Given a standard Monopoly board & the rules detailed below, find the K squares with the
 * highest probability of being landed on when using a N-sided dice. The squares should be
 * represented as a (K * 2)-digit modal string in descending order of popularity.
 *
 * Constraints: 4 <= N < 40, 3 <= K <= 40
 *
 * A Monopoly board can be represented as:
 *
 *      [GO, A1, CC1, A2, T1, R1, B1, CH1, B2, B3]
 *      [JAIL, C1, U1, C2, C3, R2, D1, CC2, D2, D3]
 *      [FP, E1, CH2, E2, E3, R3, F1, F2, U2, F3]
 *      [G2J, G1, G2, CC3, G3, R4, CH3, H1, T2, H2]
 *
 * If a player starts on GO & adds the scores of 2 6-sided dice to advance clockwise, without any
 * further rules, they would expect to visit each square with equal probability of 2.5%. However,
 * landing on G2J sends the player to JAIL, as would picking the 1 jail card in the CC and CH
 * piles. A player is also sent to jail if they roll 3 doubles in a row. The only CC cards that
 * involve movement (2/16) either end at GO or JAIL. The 10/16 CH cards cause the following
 * movements: GO, JAIL, C1, E3, H2, R1, next R, next R, next U, back 3 squares.
 *
 * Since this problem only cares about the probability of finishing on a square after a roll, G2J
 * will be the only square that has a probability of 0. The CH squares will have the lowest
 * probabilities as 10/16 = 5/8 cause movement to another square.
 *
 * N.B. There should be no distinction between "Just Visiting" and being sent to JAIL and, unlike
 * the normal rules, a player cannot leave jail by rolling a double (they are assumed to pay on
 * their next turn).
 *
 * e.g.: N = 6, K = 3
 *       JAIL (6.24%) at square 10
 *       E3 (3.18%) at square 24
 *       GO (3.09%) at square 00
 *       result = "102400"
 */

class MonopolyOdds {
    /**
     * Solution implements a Monte Carlo simulation that runs repeated random samples using
     * Monopoly logic detailed in the documentation above (contained in a custom MonopolyGame
     * class). The higher the amount of sample rounds, the closer towards a deterministic result
     * the solution will get.
     *
     * @return indices of the most popular squares in descending order, represented as a string.
     */
    fun monteCarloOdds(n: Int, k: Int, rounds: Int = 4_000_000): String {
        val game = MonopolyGame(n)
        repeat(rounds) {
            game.play()
        }
        val odds = game.getOdds(rounds).take(k)
        return odds.joinToString("") { it.first }
    }

    /**
     * A Markov Chain is a memory-less stochastic process in that the probability of
     * transitioning to any particular state is dependent solely on the current state & time
     * elapsed (the latter is unimportant if the chain is also time-homogenous). Given this
     * Markov property, this solution must ignore the Monopoly rule concerning 3 doubles in a row
     * sending a player to jail.
     *
     * For any positive integer n and possible states i_0, i_1, ..., i_n, a Markov Chain is a
     * sequence that satisfies the rule of conditional independence, such that:
     *
     *      P(X_n = i_n | X_n-1 = i_n-1) = P(X_n = i_n | X_0 = i_0, X_1 = i_1, ..., X_n-1 = i_n-1)
     *
     * This allows for non-stationary transition probabilities & time-inhomogenous chains (i.e.
     * as the number of steps increase, the probability of moving to another state may change).
     *
     * A transition matrix P_t for Markov Chain {X} at time t is built using:
     *
     *      (P_t)_i,j = P(X_t+1 = j | X_t = i)
     *
     *      with each matrix row being a probability vector with a row sum of 1.
     *
     * The product of subsequent matrices describes the transition along time intervals, such that:
     *
     *      the (i, j)th position of P_t * P_t+1 * ... * P_t+k ->
     *
     *      P(X_t+k+1 = j | X_t = i)
     *
     *      e.g. P_0 * P_1 has in its (i, j)th position the probability that X_2 = j given that
     *      X_0 = i.
     */
    fun markovChainOdds(n: Int, k: Int): String {
        val matrix = Array(40) { DoubleArray(40) }
        val rollStats = getDiceProbability(n)
        for (square in 0 until 40) {
            when (square) {
                30 -> matrix[30][10] = 1.0 // G2J
                7, 22, 36 -> matrix[square].adjustForChance(square)
                2, 17, 33 -> matrix[square].adjustForCommunityChest(square)
                else -> {
                    for ((i, prob) in rollStats.withIndex()) {
                        if (i < 2) continue
                        when (val nextSquare = (square + i) % 40) {
                            30 -> {
                                matrix[square][10] += prob
                            }
                            7, 22, 36 -> matrix[square].adjustForChance(nextSquare, prob)
                            2, 17, 33 -> matrix[square].adjustForCommunityChest(nextSquare, prob)
                            else -> {
                                matrix[square][nextSquare] += prob
                            }
                        }
                    }
                }
            }
            if (square < 2) {
                println(matrix[square].contentToString())
            }
        }
        // flip columns to rows for easier access
        val transposed = matrix.transpose()
        // initialise with the probability of landing on square 0 (GO) after 0 rolls being 1.0
        var odds = DoubleArray(40).apply { this[0] = 1.0 }
        repeat(40) { // 3 dice rolls
            val current = DoubleArray(40) { odds.product(transposed[it]) }
            odds = current.clone()
        }
        val popularSquares = odds.mapIndexed { square, prob ->
            square.toString().padStart(2, '0') to prob
        }.sortedByDescending { it.second }.take(k)
        return popularSquares.joinToString("") { it.first }
    }

    private fun DoubleArray.adjustForChance(square: Int, rollProb: Double = 1.0) {
        this[square] += rollProb * 3 / 8 // stay on CH
        // move to (GO, R1, JAIL, C1, E3, H2, back 3)
        for (i in listOf(0, 5, 10, 11, 24, 39, square - 3)) {
            this[i] += (rollProb * 1 / 16)
        }
        when (square) {
            7 -> {
                this[15] += rollProb * 1 / 8 // next R = R2
                this[12] += rollProb * 1 / 16 // next U = U1
            }
            22 -> {
                this[25] += rollProb * 1 / 8 // next R = R3
                this[28] += rollProb * 1 / 16 // next U = U2
            }
            36 -> {
                this[5] += rollProb * 1 / 8 // next R = R1
                this[12] += rollProb * 1 / 16 // next U = U1
            }
        }
    }

    private fun DoubleArray.adjustForCommunityChest(square: Int, rollProb: Double = 1.0) {
        this[square] += rollProb * 7 / 8 // stay on CC
        for (i in listOf(0, 10)) { // move to (GO, JAIL)
            this[i] += (rollProb * 1 / 16)
        }
    }

    private fun getDiceProbability(sides: Int): List<Double> {
        val outcomes = sides * sides
        // outcomes of 0 or 1 are not possible but remain for easier indexing
        val probabilities = DoubleArray(2 * sides + 1)
        for (i in 1..sides) {
            for (j in 1..sides) {
                probabilities[i + j]++
            }
        }
        return probabilities.map { it / outcomes }
    }

    fun Array<DoubleArray>.multiply(other: Array<DoubleArray>): Array<DoubleArray> {
        val result = Array(size) { DoubleArray(size) }
        val cols = transpose()
        for (row in indices) {
            for (col in indices) {
                result[row][col] = this[row].product(cols[col])
            }
        }
        return result
    }

    private fun Array<DoubleArray>.transpose(): Array<DoubleArray> {
        val transposed = Array(size) { DoubleArray(size) }
        for (i in indices) {
            for (j in indices) {
                transposed[j][i] = this[i][j]
            }
        }
        return transposed
    }

    private fun DoubleArray.product(other: DoubleArray): Double {
        return this.zip(other) { x, y -> x * y }.sum()
    }
}

fun main() {
    val tool = MonopolyOdds()
    println(tool.markovChainOdds(6, 3))
}