package dev.bogwalk.util.custom

import kotlin.random.Random

/**
 * A class that simulates the movement of a single player in a standard Monopoly game by storing
 * the amount of times each game square is visited at the end of each dice roll.
 *
 * The community chest and chance card piles are stored using another custom class,
 * RollingQueue, which allows the card that is removed from the top (head) to be auto-added to the
 * bottom (tail) after processing.
 *
 * The following standard rules are ignored:
 *  - 'Just Visiting' differs from JAIL as a square.
 *  - Rolling a double gets a player out of JAIL (instead it is assumed the player leaves on
 *  the next turn by paying).
 *
 * N.B. The game board and its square representations (as detailed by their String name or index
 * number) is based on the layout presented in Project Euler Problem 84.
 * @see <a href="https://projecteuler.net/problem=84">Monopoly Board</a>
 *
 * @param [doublesRule] toggles the standard rule that sends a player directly to JAIL if 3
 * consecutive doubles are rolled.
 */
class MonopolyGame(
    private val diceSides: Int = 6,
    private val doublesRule: Boolean = true
) {
    private val finishes = IntArray(40)
    private val board = listOf(
        "GO", "A1", "CC1", "A2", "T1", "R1", "B1", "CH1", "B2", "B3",
        "JAIL", "C1", "U1", "C2", "C3", "R2", "D1", "CC2", "D2", "D3",
        "FP", "E1", "CH2", "E2", "E3", "R3", "F1", "F2", "U2", "F3",
        "G2J", "G1", "G2", "CC3", "G3", "R4", "CH3", "H1", "T2", "H2"
    )
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
                10 -> add(-3) // move backwards 3 squares
                else -> add(-1) // no movement
            }
        }
    }
    private var currentSquare = 0
    private var doubles = 0
    private var rounds = 0

    fun play() {
        val roll = rollDices()
        currentSquare = if (doublesRule && doubles == 3) {
            doubles = 0
            10  // 3 doubles in a row sends the player to jail
        } else {
            var nextSquare = (currentSquare + roll) % 40
            // must handle chance cards separately as CH3 causes a move back 3 squares
            // which would land on CC3 and cause a new card to be picked
            if (nextSquare in listOf(7, 22, 36)) {
                nextSquare = chance.pickCard(nextSquare)
            }
            when (nextSquare) {
                30 -> 10 // G2J
                2, 17, 33 -> communityChest.pickCard(nextSquare)
                else -> nextSquare
            }
        }
        finishes[currentSquare]++
        rounds++
    }

    private fun rollDices(): Int {
        val dice1 = Random.nextInt(1, diceSides + 1)
        val dice2 = Random.nextInt(1, diceSides + 1)
        // only consecutive doubles are counted
        doubles = if (dice1 == dice2) doubles + 1 else 0
        return dice1 + dice2
    }

    private fun RollingQueue<Int>.pickCard(square: Int): Int {
        return when (val card = poll().also { add(it) }) {
            -3 -> square - 3 // only possible with CH, so will not cause negative values
            100 -> if (square == 7) 15 else if (square == 22) 25 else 5
            200 -> if (square == 22) 28 else 12
            -1 -> square
            else -> card
        }
    }

    /**
     * Returns the percentage of times each game square was finished on, sorted in descending order,
     * with each square represented by a 2-digit modal string version of its index number.
     */
    fun getOdds(): List<Pair<String, Double>> {
        return finishes.mapIndexed { i, count ->
            val position = i.toString().padStart(2, '0')
            val odds = count * 100.0 / rounds
            position to odds
        }.sortedByDescending { it.second }
    }

    /**
     * Returns the top [k] most finished on squares both as a concatenated [k * 2]-digit modal
     * string (first component) and a string-name representation (second component).
     */
    fun getTopSquares(k: Int): Pair<String, String> {
        val top = getOdds().take(k).map { it.first }
        return top.joinToString("") to top.joinToString(" ") { board[it.toInt()] }
    }
}