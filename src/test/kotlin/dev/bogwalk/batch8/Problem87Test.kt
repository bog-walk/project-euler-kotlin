package dev.bogwalk.batch8

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PrimePowerTriplesTest {
    private val tool = PrimePowerTriples()
    private lateinit var allCounts: List<Int>

    @BeforeAll
    fun setup() {
        val limit = 10_000_000
        allCounts = tool.allPrimePowerTripleCounts(limit)
    }

    @ParameterizedTest(name = "N = {0}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 0", "10, 0", "50, 4", "100, 10", "212, 24", "499, 53",
        // mid constraints
        "1000, 98", "8888, 634", "10_000, 683", "40_000, 2229",
        // upper constraints
        "1_000_000, 33616", "10_000_000, 256_629", "50_000_000, 1_097_343"
    )
    fun `correct for all constraints`(n: Int, expected: Int) {
        assertEquals(expected, allCounts[n])
    }
}