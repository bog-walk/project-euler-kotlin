package batch5

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed

internal class LychrelNumbersTest {
    private val tool = LychrelNumbers()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "100, 121, 16", "130, 121, 18", "500, 121, 18",
        // mid constraints
        "1000, 1111, 25", "5000, 12221, 88",
        // upper constraints
        "10000, 79497, 215", "50000, 79497, 295"
    )
    fun `HR problem correct`(n: Int, first: String, second: Int) {
        val expected = first to second
        assertEquals(expected, tool.maxPalindromeConvergenceCached(n))
        assertEquals(expected, tool.maxPalindromeConvergence(n))
    }

    @Test
    fun `maxPalindromeConvergence speed`() {
        val n = 1e5.toInt()
        val expected = "4964444694" to 583
        val solutions = mapOf(
            "Cache" to tool::maxPalindromeConvergenceCached,
            "No Cache" to tool::maxPalindromeConvergence
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `PE problem correct`() {
        val expected = 249
        assertEquals(expected, tool.countLychrelNumbers())
    }
}