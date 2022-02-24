package batch5

/**
 * Problem 54: Poker Hands
 *
 * https://projecteuler.net/problem=54
 *
 * Goal: Determine the winner when presented with 2 poker hands, based on the rules detailed
 * below and assuming that all cards are valid and a winner is possible.
 *
 * Constraints: None
 *
 * Poker Hand Rankings: From lowest to highest ->
 * [High Cards, One Pair, Two Pairs, Three of a kind, Straight, Flush, Full House, Four of a
 * kind, Straight Flush, Royal Flush]
 *
 * If both hands have the same ranked hand, the rank made up of the highest value wins. If both
 * ranks tie, then the highest value of the next highest rank is assessed until the only rank
 * left to assess is the high card rank.
 *
 * e.g.: hand 1 = "5H 6H 2S KS 3D" -> High card King
 *       hand 2 = "5D JD 7H JS 8H" -> One pair Joker
 *       winner = player 2
 *
 *       hand 1 = "AH AC AS 7S 7C" -> Full house 3 Aces, One pair 7
 *       hand 2 = "AS 3S 3D AD AH" -> Full house 3 Aces, One pair 3
 *       winner = player 1
 */

class PokerHands {
    /**
     * Compares lists representing all possible ranks for both hands, in reverse order, as this
     * allows higher ranking possibilities to be compared first. If a rank has equal values, the
     * next highest rank possibility will be compared. If comparison comes down to individual
     * high cards, they have already been returned in reverse order by the helper function.
     *
     * @return 1 if [hand1] is the winner; otherwise, 2.
     */
    fun pokerHandWinner(hand1: List<String>, hand2: List<String>): Int {
        val hand1Ranks = rankHand(hand1)
        val hand2Ranks = rankHand(hand2)
        for (i in 9 downTo 1) {
            val h1 = hand1Ranks[i]
            val h2 = hand2Ranks[i]
            if (h1.isEmpty() && h2.isEmpty()) continue
            if (h1.isEmpty()) return 2
            if (h2.isEmpty()) return 1
            if (h1.single() == h2.single()) continue
            return if (h1.single() < h2.single()) 2 else 1
        }
        for (j in hand1Ranks[0].indices) {
            if (hand1Ranks[0][j] == hand2Ranks[0][j]) continue
            return if (hand1Ranks[0][j] < hand2Ranks[0][j]) 2 else 1
        }
        return 0 // will not be reached
    }

    /**
     * Rank a five card hand based on the order detailed in the problem description.
     *
     * All existing ranks are presented as their highest relevant card to allow potential ties
     * between hands to be broken without needing to re-rank them.
     *
     * e.g.
     *      ["4C", "4D", "4S", "9S", "9D"] is presented as:
     *
     *      [[], [9], [], [4], [], [], [4], [], [], []], which, when evaluated from RtL means the
     *      hand has Full House with 3 Fours, then 3 Fours, then 2 Nines.
     *
     *      ["3D", "6D", "7H", "QD", "QS"] is presented as:
     *
     *      [[7, 6, 3], [12], [], [], [], [], [], [], [], []], which, when evaluated from
     *      RtL means the hand has 2 Queens, then High card 7, then 6, then 3.
     */
    private fun rankHand(hand: List<String>): List<List<Int>> {
        // nested list to allow multiple high cards
        val ranks = MutableList(10) { emptyList<Int>() }
        val count = normaliseCount(hand.map(String::first))
        val uniqueSuits = hand.map { it[1] }.toSet().size
        var streak = 0
        var pair = 0
        var triple = 0
        // move backwards to sort high cards in reverse
        for (i in 14 downTo 2) {
            when (count[i]) {
                0 -> { // no cards of value i
                    streak = 0
                    continue
                }
                4 -> ranks[7] = listOf(i) // 4 of a kind
                3 -> { // 3 of a kind
                    ranks[3] = listOf(i)
                    triple = i
                }
                2 -> { // 1 pair, at minimum
                    if (pair > 0) { // 2 pair
                        // give the 2 pair ranking the higher value pair
                        ranks[1] = listOf(
                            maxOf(i, ranks[1].single())
                        )
                        ranks[2] = listOf(
                            minOf(i, ranks[1].single())
                        )
                    } else {
                        ranks[1] = listOf(i)
                        pair = i
                    }
                }
                1 -> ranks[0] += listOf(i) // high card
            }
            streak++
            if (streak == 5 || streak == 4 && i == 2 && count[1] > 0) { // straight
                // low Ace straight has high card 5
                ranks[4] = if (streak == 4) {
                    // ensure 14 not at front of high card list since Ace is low in this case
                    ranks[0] = ranks[0].drop(1) + listOf(14)
                    listOf(5)
                } else {
                    listOf(i + 4)
                }
                if (uniqueSuits == 1) { // straight flush
                    ranks[8] = if (streak == 4) {
                        // low Ace straight flush has high card 5
                        listOf(5)
                    } else {
                        listOf(i + 4)
                    }
                    // royal flush
                    if (i == 10) ranks[9] = listOf(14)
                }
                break // no further cards if streak achieved
            }
        }
        // give the full house ranking the higher 3 of a kind value
        if (triple > 0 && pair > 0) ranks[6] = listOf(triple)
        // give flush ranking the highest card value, unless it's a low Ace straight flush
        if (uniqueSuits == 1) {
            ranks[5] = ranks[8].ifEmpty {
                listOf(ranks[0].maxOrNull()!!)
            }
        }
        return ranks
    }

    /**
     * Normalises card values to range from 1 to 14 & counts the amount of each card in the hand.
     * Note that Ace cards are counted as both high & low.
     *
     * @return list of card counts with card value == index. e.g. [0, 0, 0, 0, 0, 2, 1, 1, 0, 0,
     * 0, 0, 0, 1, 0] represents a hand with 2 fives, 1 six, 1 seven, and 1 King.
     */
    private fun normaliseCount(values: List<Char>): List<Int> {
        val nonNums = listOf('T', 'J', 'Q', 'K', 'A')
        val count = MutableList(15) { 0 }
        for (value in values) {
            val num = if (value < ':') value.code - 48 else 10 + nonNums.indexOf(value)
            count[num]++
            if (num == 14) count[1]++ //count Ace as a high or low card
        }
        return count
    }
}