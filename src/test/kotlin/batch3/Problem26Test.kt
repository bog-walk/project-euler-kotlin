package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class ReciprocalCyclesTest {
    private val tool = ReciprocalCycles()

    @ParameterizedTest(name="< 1/{0} = 1/{1}")
    @CsvSource(
        // lower constraints
        "5, 3", "10, 7", "14, 7",
        // normal values
        "25, 23", "46, 29",
        // upper constraints
        "1000, ?"
    )
    fun testLongestReciprocalCycle(n: Int, expected: Int) {
        assertEquals(expected, tool.longestRecurringDecimal(n))
    }
}