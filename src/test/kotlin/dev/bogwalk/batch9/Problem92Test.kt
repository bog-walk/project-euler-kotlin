package dev.bogwalk.batch9

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.getSpeed
import dev.bogwalk.util.tests.compareSpeed

internal class SquareDigitChainsTest {
    private val tool = SquareDigitChains()

    @ParameterizedTest(name="10^{0}")
    @CsvSource(
        // lower constraints
        "1, 7", "2, 80", "3, 857", "4, 8558", "5, 85623", "6, 856_929"
    )
    fun `correct for very low constraints`(k: Int, expected: Int) {
        assertEquals(expected, tool.countSDChainsBrute(k))
        assertEquals(expected, tool.countSDChainsPerm(k))
        assertEquals(expected, tool.countSDChainsImproved(k))
    }

    @Test
    fun `speed for low constraints`() {
        val k = 7
        val expected = 8_581_146
        val solutions = mapOf(
            "Original" to tool::countSDChainsBrute,
            "Permutations" to tool::countSDChainsPerm,
            "Improved" to tool::countSDChainsImproved
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, k).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @ParameterizedTest(name="10^{0}")
    @CsvSource(
        // lower constraints
        "8, 85_744_333", "9, 854_325_192", "10, 507_390_796",
        // mid constraints
        "20, 742_248_133", "50, 428_414_150", "73, 319_026_233",
        // upper constraints
        "100, 999_578_314", "200, 237_156_061"
    )
    fun `correct for all constraints`(k: Int, expected: Int) {
        assertEquals(expected, tool.countSDChainsImproved(k))
    }
}