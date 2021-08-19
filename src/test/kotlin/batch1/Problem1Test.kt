package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class MultiplesOf3Or5Test {
    @ParameterizedTest(name="N={0}, K1={1}, K2={2}, Sum={3}")
    @CsvSource(
            // lower limits for N
            "2, 1, 1, 1", "3, 1, 2, 3", "4, 2, 3, 5",
            // lower limits for factors
            "20, 1, 2, 190",
            // equivalent factors
            "10, 3, 3, 18",
            // normal limits
            "10, 3, 5, 23", "100, 3, 5, 2318", "1000, 3, 5, 233168",
            // large limits
            "23000, 8, 21, 44087172", "10000000, 20, 32, 3749995000000"
    )
    fun testAllVersions(number: Int, factor1: Int, factor2: Int, expected: Long) {
        val tool = MultiplesOf3Or5()
        assertEquals(expected, tool.sumOfMultiplesBruteA(number, factor1, factor2))
        assertEquals(expected, tool.sumOfMultiplesBruteB(number, factor1, factor2))
        assertEquals(expected.toBigInteger(), tool.sumOfMultiples(number, factor1, factor2))
    }

    @Test
    fun testUpperLimits() {
        val tool = MultiplesOf3Or5()
        val number = 1_000_000_000
        val expected = 233333333166666668.toBigInteger()
        assertEquals(expected, tool.sumOfMultiples(number, 3, 5))
    }
}