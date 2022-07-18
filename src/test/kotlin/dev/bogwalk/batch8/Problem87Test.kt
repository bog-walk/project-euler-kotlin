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
        val limit = 100_000_000
        allCounts = tool.allPrimePowerTripleCounts(limit)
    }

    @ParameterizedTest(name = "N = {0}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 0", "10, 0", "50, 4", "100, 10", "212, 24", "499, 53",
        // mid constraints
        "1000, 98", "8888, 634", "10000, 683", "40000, 2229",
        // upper constraints
        "1000000, 33616", "10000000, 256629", "50000000, 1097343", "100000000, 2037066"
    )
    fun `correct for all constraints`(n: Int, expected: Int) {
        assertEquals(expected, allCounts[n])
    }
}