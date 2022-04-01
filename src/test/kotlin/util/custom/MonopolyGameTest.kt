package util.custom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MonopolyGameTest {
    @Test
    fun `MonopolyGame produces consistent edge case odds`() {
        val rounds = 1_000_000
        val game = MonopolyGame(diceSides = 6)
        repeat(rounds) {
            game.play()
        }
        val odds = game.getOdds(rounds)
        val expectedFirst = "10" // JAIL
        val expectedLast = "30" // G2J
        // chance cards send player to other squares 5/8 times
        val expectedSecondLast = listOf("07", "22", "36")
        assertEquals(expectedFirst, odds.first().first)
        assertEquals(expectedLast, odds.last().first)
        assertTrue(odds.slice(36..38).map { it.first }.all { it in expectedSecondLast })
    }
}