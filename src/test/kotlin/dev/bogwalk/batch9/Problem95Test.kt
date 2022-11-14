package dev.bogwalk.batch9

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class AmicableChainsTest {
    private val tool = AmicableChains()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "6, 6", "10, 6", "30, 6", "100, 6", "300, 220", "1000, 220",
        // mid constraints
        "5000, 220", "10_000, 220", "50_000, 12496", "100_000, 12496",
        // upper constraints
        "500_000, 12496", "800_000, 14316", "1_000_000, 14316"
    )
    fun `longestAmicableChain() correct for all constraints`(n: Int, expected: Int) {
        val chain = tool.longestAmicableChain(n)
        assertEquals(expected, chain.min())
    }
}