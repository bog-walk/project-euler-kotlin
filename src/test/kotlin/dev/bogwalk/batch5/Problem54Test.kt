package dev.bogwalk.batch5

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.getTestResource

internal class PokerHandsTest {
    private val tool = PokerHands()

    @Test
    fun `HR problem correct when no tie`() {
        val hands = listOf(
            listOf("5H", "5C", "6S", "7S", "KD") to listOf("2C", "3S", "8S", "8D", "TD"),
            listOf("5D", "8C", "9S", "JS", "AC") to listOf("2C", "5C", "7D", "8S", "QH"),
            listOf("2D", "9C", "AS", "AH", "AC") to listOf("3D", "6D", "7D", "TD", "QD"),
            listOf("QS", "TS", "KS", "JS", "AS") to listOf("TD", "TC", "TS", "AH", "TH")
        )
        val expected = listOf(2, 1, 2, 1)
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.pokerHandWinner(hands[i].first, hands[i].second))
        }
    }

    @Test
    fun `HR problem correct when 1 tie exists`() {
        val hands = listOf(
            listOf("4D", "6S", "9H", "QH", "QC") to listOf("3D", "6D", "7H", "QD", "QS"),
            listOf("2H", "2D", "4C", "4D", "4S") to listOf("3C", "3D", "3S", "9S", "9D"),
        )
        val expected = listOf(1, 1)
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.pokerHandWinner(hands[i].first, hands[i].second))
        }
    }

    @Test
    fun `HR problem correct when multiple ties exist`() {
        val hands = listOf(
            listOf("2H", "2D", "4C", "4D", "4S") to listOf("4C", "4D", "4S", "9S", "9D"),
            listOf("3H", "4D", "5C", "7D", "9S") to listOf("2C", "4C", "5S", "7S", "9D"),
        )
        val expected = listOf(2, 1)
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.pokerHandWinner(hands[i].first, hands[i].second))
        }
    }

    @Test
    fun `HR problem correct when ace low`() {
        val hand = listOf("2H", "3D", "4C", "AD", "5S") to listOf("2H", "2D", "4C", "4D", "8S")
        val expected = 1
        assertEquals(expected, tool.pokerHandWinner(hand.first, hand.second))
    }

    @Test
    fun `PE problem correct`() {
        val expected = 376
        val plays = getTestResource("src/test/resources/PokerHands.txt") { it }
        val player1Wins = plays.count { play ->
            tool.pokerHandWinner(play.take(5), play.takeLast(5)) == 1
        }
        assertEquals(expected, player1Wins)
    }
}