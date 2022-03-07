package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class MultiplesOf3Or5Test {
    private val tool = MultiplesOf3Or5()

    @ParameterizedTest(name="N={0}, K1={1}, K2={2}, Sum={3}")
    @CsvSource(
            // lower constraints for N
            "1, 1, 1, 0", "2, 1, 2, 1", "2, 1, 1, 1", "3, 1, 2, 3", "4, 2, 3, 5",
            // lower constraints for K
            "20, 1, 2, 190",
            // K1 == K2
            "10, 3, 3, 18",
            // mid constraints
            "10, 3, 5, 23", "100, 3, 5, 2318", "1000, 3, 5, 233_168",
            // upper constraints
            "23000, 8, 21, 44_087_172", "10_000_000, 20, 32, 3_749_995_000_000"
    )
    fun `sumOfMultiples correct`(n: Int, factor1: Int, factor2: Int, expected: Long) {
        assertEquals(expected, tool.sumOfMultiplesBrute(n, factor1, factor2))
        assertEquals(expected, tool.sumOfMultiples(n, factor1, factor2))
    }

    @Test
    fun `sumOfMultiples correct at upper constraint of N`() {
        val n = 1e9.toInt()
        val expected = 233_333_333_166_666_668
        assertEquals(expected, tool.sumOfMultiples(n, 3, 5))
    }
}