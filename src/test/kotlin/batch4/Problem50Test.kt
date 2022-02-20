package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConsecutivePrimeSumTest {
    private val tool = ConsecutivePrimeSum()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 2, 1", "5, 5, 2", "10, 5, 2", "20, 17, 4", "50, 41, 6", "100, 41, 6", "150, 127, 9",
        "200, 197, 12", "300, 281, 14", "500, 499, 17", "1000, 953, 21", "2000, 1583, 27",
        "10000, 9521, 65", "22340, 22039, 96",
        // mid constraints (1e5 to 1e9)
        "100000, 92951, 183", "1000000, 997651, 543", "10000000, 9951191, 1587",
        "100000000, 99819619, 4685", "1000000000, 999715711, 13935"
    )
    fun `both correct for all but upper N`(n: Long, expectedPrime: Long, expectedLength: Int) {
        val expected = expectedPrime to expectedLength
        assertEquals(expected, tool.consecutivePrimeSum(n))
        assertEquals(expected, tool.consecutivePrimeSumImproved(n))
    }

    @Test
    fun `improved solution correct for upper N`() {
        val expected = listOf(
            99_987_684_473 to 125_479, 999_973_156_643 to 379_317
        )
        var n = 10_000_000_000
        for (e in 11..12) {
            n *= 10
            assertEquals(expected[e-11], tool.consecutivePrimeSumImproved(n))
        }
    }

    @Test
    fun `consecutivePrimeSum speed`() {
        val n = 10_000_000_000
        val expected = 9_999_419_621 to 41708
        val solutions = mapOf(
            "Brute" to tool::consecutivePrimeSum,
            "Improved" to tool::consecutivePrimeSumImproved
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}