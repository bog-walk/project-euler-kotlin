package batch1

import SumSquareDifference
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SumSquareDifferenceTest {
    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        // lower constraint values
        "1, 0", "2, 4", "3, 22",
        // normal values
        "10, 2640", "51, 1712750", "100, 25164150",
        // higher constraint values
        "10000, 2500166641665000"
    )
    fun testSumSquareDiff(max: Int, expected: Long) {
        val tool = SumSquareDifference()
        //assertEquals(expected, tool.sumSquareDiffBrute(max))
        assertEquals(expected, tool.sumSquareDiffImproved(max))
    }
}