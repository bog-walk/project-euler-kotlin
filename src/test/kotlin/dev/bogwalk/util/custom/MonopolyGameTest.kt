package dev.bogwalk.util.custom

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MonopolyGameTest {
    private val rounds = 6_000_000
    private lateinit var standardOdds: List<Pair<String, Double>>

    @BeforeAll
    fun setUp() {
        val game = MonopolyGame(diceSides = 6)
        repeat(rounds) {
            game.play()
        }
        standardOdds = game.getOdds()
    }

    @Test
    fun `JAIL is the most finished on square`() {
        val jail = "10"
        val expectedProb = 6.24
        val actual = standardOdds.first()
        assertEquals(jail, actual.first)
        assertEquals(expectedProb, actual.second, absoluteTolerance = 0.05)
    }

    @Test
    fun `dice roll should never finish on G2J`() {
        val g2j = "30"
        val expectedProb = 0.0
        val actual = standardOdds.last()
        assertEquals(g2j, actual.first)
        assertEquals(expectedProb, actual.second, absoluteTolerance = 0.001)
    }

    @Test
    fun `chance squares have lowest probabilities`() {
        val chanceSquares = listOf("07", "22", "36")
        val actual = standardOdds.slice(36..38)
        assertTrue(actual.map { it.first }.all { it in chanceSquares })
    }

    @Test
    fun `JAIL, E3, and GO are top finishers in standard game`() {
        val expectedTop = listOf("10", "24", "00")
        val expectedProbs = listOf(6.24, 3.18, 3.09)
        val actual = standardOdds.slice(0..2)
        assertContentEquals(expectedTop, actual.map { it.first })
        for ((i, prob) in actual.map { it.second }.withIndex()) {
            assertEquals(expectedProbs[i], prob, absoluteTolerance = 0.1)
        }
    }

    @Test
    fun `dice sides should not affect JAIL and G2J being first and last`() {
        val jail = "10"
        val g2j = "30"
        for (n in 4..10) {
            if (n == 6) continue
            val game = MonopolyGame(n)
            repeat(rounds) {
                game.play()
            }
            val odds = game.getOdds()
            assertEquals(jail, odds.first().first)
            assertEquals(g2j, odds.last().first)
        }
    }
}