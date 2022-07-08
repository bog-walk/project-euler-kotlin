package dev.bogwalk.util.custom

import kotlin.random.Random

/**
 * A class that simulates the movement of a single player in a standard Monopoly game by storing
 * the amount of times each boardgame square is visited at the end of the dice roll.
 *
 * The community chest and chance card piles are simulated using another custom class,
 * RollingQueue, which allows the card that is removed from the top (head) to be auto-added to the
 * bottom (tail) after processing.
 *
 * N.B. The game board and its square representations (as detailed by their String name or index
 * number) is based on the layout presented in Project Euler Problem 84.
 * @see <a href="https://projecteuler.net/problem=84">Monopoly Board</a>
 */
class MonopolyGame(
    private val diceSides: Int = 6
) {
    private val board = IntArray(40)
    private val communityChest = RollingQueue<Int>(16).apply {
        (1..16).shuffled().forEach { card ->
            when (card) {
                1 -> add(0) // move to GO
                2 -> add(10) // move to JAIL
                else -> add(-1) // no movement
            }
        }
    }
    private val chance = RollingQueue<Int>(16).apply {
        (1..16).shuffled().forEach { card ->
            when (card) {
                1 -> add(0) // move to GO
                2 -> add(10) // move to JAIL
                3 -> add(11) // move to C1
                4 -> add(24) // move to E3
                5 -> add(39) // move to H2
                6 -> add(5) // move to R1
                7, 8 -> add(100) // move to next R
                9 -> add(200) // move to next U
                10 -> add(-3) // move to back 3 squares
                else -> add(-1) // no movement
            }
        }
    }
    private var currentSquare = 0
    private var doubles = 0

    fun play() {
        val roll = rollDices()
        currentSquare = if (doubles == 3) {
            // 3 doubles in a row sends the player to jail
            doubles = 0
            10
        } else {
            var nextSquare = (currentSquare + roll) % 40
            // must handle CH separately as CH3 move back 3 squares would land on CC3
            if (nextSquare in listOf(7, 22, 36)) {
                nextSquare = chance.pickCard(nextSquare)
            }
            when (nextSquare) {
                30 -> 10 // G2J
                2, 17, 33 -> communityChest.pickCard(nextSquare)
                else -> nextSquare
            }
        }
        board[currentSquare]++
    }

    private fun rollDices(): Int {
        val dice1 = Random.nextInt(1, diceSides + 1)
        val dice2 = Random.nextInt(1, diceSides + 1)
        doubles = if (dice1 == dice2) doubles + 1 else 0
        return dice1 + dice2
    }

    private fun RollingQueue<Int>.pickCard(square: Int): Int {
        return when (val card = poll().also { add(it) }) {
            -3 -> square - 3 // only possible with CH, which will not cause negative values
            100 -> if (square == 7) 15 else if (square == 22) 25 else 5
            200 -> if (square == 22) 28 else 12
            -1 -> square
            else -> card
        }
    }

    /**
     * Returns the percentage of times each board square was visited sorted in descending order,
     * with each square represented a 2-digit modal string version of its index number.
     */
    fun getOdds(rounds: Int): List<Pair<String, Double>> {
        return board.mapIndexed { i, visits ->
            val position = i.toString().padStart(2, '0')
            val odds = visits * 100.0 / rounds
            position to odds
        }.sortedByDescending { it.second }
    }
}