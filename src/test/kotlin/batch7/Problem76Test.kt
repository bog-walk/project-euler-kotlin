package batch7

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CountingSummationsTest {
    private val tool = CountingSummations()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "2, 1", "3, 2", "4, 4", "5, 6", "6, 10", "7, 14", "10, 41",
        // mid constraints
        "80, 15796475", "100, 190569291", "350, 528158869",
        //upper constraints
        "880, 200208910", "1000, 709496665"
    )
    fun `countSumCombos correct for all constraints`(n: Int, expected: Int) {
        assertEquals(expected, tool.countSumCombos(n))
    }
}