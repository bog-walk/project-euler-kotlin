package dev.bogwalk.batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

internal class AmicableNumbersTest {
    private val tool = AmicableNumbers()

    @ParameterizedTest(name="Sum < {0} = {1}")
    @CsvSource(
        // lower constraints
        "1, 0", "100, 0",
        // first pair (220, 284)
        "300, 504",
        // second pair (1184, 1210)
        "2000, 2898",
        // third pair (2620, 2924)
        "5000, 8442",
        // upper constraints
        "10_000, 31626", "40_000, 115_818", "100_000, 852_810"
    )
    fun `sumAmicablePairs correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.sumAmicablePairs(n))
    }
}