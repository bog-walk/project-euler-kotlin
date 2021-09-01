package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SmallestMultipleTest {
    private val tool = SmallestMultiple()

    @ParameterizedTest(name="1..{0} = {1}")
    @CsvSource(
       // lower constraints
        "1, 1", "2, 2", "3, 6",
        // normal values
        "6, 60", "10, 2520", "20, 232792560",
        // higher constraints
        "30, 2329089562800", "40, 5342931457063200"
    )
    fun testSmallestMultiple(n: Int, expected: Long) {
        //assertEquals(expected, tool.lcmIterative(n))
        //assertEquals(expected, tool.lcmPrimeFactors(n))
        //assertEquals(expected, tool.lcmUsingGCD(n))
        //assertEquals(expected.toBigInteger(), tool.lcmUsingBigInteger(n))
        assertEquals(expected, tool.lcmUsingPrimes(n))
    }
}