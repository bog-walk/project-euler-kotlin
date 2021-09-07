package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class The10001stPrimeTest {
    @ParameterizedTest(name="{0}th prime is {1}")
    @CsvSource(
        // lower constraint N
        "1, 2", "2, 3", "3, 5",
        // normal N
        "6, 13", "20, 71", "62, 293",
        // large N
        "289, 1879", "919, 7193", "1000, 7919",
        // higher constraint N
        "5000, 48611", "10000, 104729", "10001, 104743"
    )
    fun testGetNthPrime(n: Int, expected: Int) {
        val tool = The10001stPrime()
        assertEquals(expected, tool.getNthPrime(n))
    }
}