package batch6

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class OddPeriodSquareRootsTest {
    private val tool = OddPeriodSquareRoots()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "10, 3", "13, 4", "20, 5", "30, 7", "45, 9",
        // mid constraints
        "100, 20", "500, 83", "2000, 296",
        // upper constraints
        "10000, 1322", "20000, 2524", "30000, 3687"
    )
    fun `oddSquareRoots correct`(num: Int, expected: Int) {
        assertEquals(expected, tool.oddSquareRoots(num))
    }
}