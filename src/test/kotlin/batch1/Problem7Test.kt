package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class The10001stPrimeTest {
    val tool = The10001stPrime()

    @Test
    fun testIsPrime_allTrue() {
        val nums = listOf(2, 3, 5, 11, 17)
        for (num in nums) {
            assertTrue(tool.isPrime(num))
        }
    }

    @Test
    fun testIsPrime_allFalse() {
        val nums = listOf(1, 6, 14, 15, 21)
        for (num in nums) {
            assertFalse(tool.isPrime(num))
        }
    }

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

        assertEquals(expected, tool.getNthPrime(n))
    }
}