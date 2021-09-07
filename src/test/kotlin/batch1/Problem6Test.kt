package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SumSquareDifferenceTest {
    private val tool = SumSquareDifference()

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 4", "3, 22",
        // normal values
        "10, 2640", "51, 1712750", "100, 25164150",
        // large values
        "2256, 6477756566600", "7000, 600307154415500",
        // upper constraint
        "10000, 2500166641665000"
    )
    fun testSumSquareDiff(max: Int, expected: Long) {
        val solutions = listOf(
            tool::sumSquareDiffBrute, tool::sumSquareDiffImproved
        )
        for (solution in solutions) {
            assertEquals(expected, solution(max))
        }
    }
}