package batch5

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConsecutivePrimeSumTest {
    private val tool = ConsecutivePrimeSum()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 2, 1", "5, 5, 2", "10, 5, 2", "20, 17, 4", "50, 41, 6",
        "100, 41, 6", "150, 127, 9", "200, 197, 12", "300, 281, 14",
        "500, 499, 17", "1000, 953, 21", "2000, 1583, 27",
        "10000, 9521, 65", "22340, 22039, 96",
        // mid constraints (1e5 to 1e9)
        "100000, 92951, 183", "1000000, 997651, 543",
        "10000000, 9951191, 1587", "100000000, 99819619, 4685",
        "1000000000, 999715711, 13935",
    )
    fun testConsecutivePrimeSum(n: Long, expectedPrime: Long, expectedLength: Int) {
        val expected = expectedPrime to expectedLength
        assertEquals(expected, tool.consecutivePrimeSum(n))
        assertEquals(expected, tool.consecutivePrimeSumImproved(n))
    }

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // upper constraints (1e11 to 1e12)
        "100000000000, 99987684473, 125479",
        "1000000000000, 999973156643, 379317"
    )
    fun testConsecutivePrimeSumHigh(n: Long, expectedPrime: Long, expectedLength: Int) {
        val expected = expectedPrime to expectedLength
        assertEquals(expected, tool.consecutivePrimeSumImproved(n))
    }

    @Test
    fun testConsecutivePrimeSumSpeed() {
        val n = 10_000_000_000
        val expected = 9999419621 to 41708
        val solutions = listOf(
            tool::consecutivePrimeSum, tool::consecutivePrimeSumImproved
        )
        val times = LongArray(solutions.size)
        solutions.forEachIndexed { i, solution ->
            times[i] = measureNanoTime {
                assertEquals(expected, solution(n))
            }
        }
        print("Brute solution took: ${1.0 * times[0] / 1_000_000_000}s\n" +
                "Improved solution took: ${1.0 * times[1] / 1_000_000_000}s\n")
    }
}