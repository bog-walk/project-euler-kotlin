package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PandigitalPrimeTest {
    private val tool = PandigitalPrime()

    @Test
        fun `PE problem correct`() {
        val expected = 7_652_413
        assertEquals(expected, tool.largestPandigitalPrimePE())
    }

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "10, -1", "100, -1", "2140, 1423",
        // normal values
        "10000, 4231", "100000, 4231", "1000000, 4231",
        // upper constraints
        "7652412, 7642513", "10000000, 7652413"
    )
    fun `HR problem correct`(n: Long, expected: Int) {
        assertEquals(expected, tool.largestPandigitalPrimeHR(n))
    }
}