package dev.bogwalk.batch8

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MonopolyOddsTest {
    private val tool = MonopolyOdds()
    private val expected = listOf(
        // lower constraints
        Triple(4, 3, "101524"), Triple(6, 3, "102400"),
        Triple(5, 4, "10242515"),
        // mid constraints
        Triple(6, 5, "1024001925"), Triple(7, 6, "102400051925"),
        // upper constraints
        Triple(10, 5, "1000240525"),
        Triple(16, 8, "1000052425391128"),
        Triple(24, 9, "100005392411251215"),
        Triple(39, 3, "100005")
    )

    @Test
    fun `monteCarloOdds correct`() {
        for ((n, k, ans) in expected.slice(0..1)) {
            assertEquals(ans, tool.monteCarloOdds(n, k), "Error with $n $k")
        }
    }

    @Test
    fun `markovChainOdds correct`() {
        for ((n, k, ans) in expected) {
            assertEquals(ans, tool.markovChainOdds(n, k), "Error with $n $k")
        }
    }
}