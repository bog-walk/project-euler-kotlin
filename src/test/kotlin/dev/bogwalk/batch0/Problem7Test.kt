package dev.bogwalk.batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class The10001stPrimeTest {
    val tool = The10001stPrime()

    @ParameterizedTest(name="{0}th prime = {1}")
    @CsvSource(
        // lower constraints
        "1, 2", "2, 3", "3, 5",
        // normal values
        "6, 13", "20, 71", "62, 293",
        // large values
        "289, 1879", "919, 7193", "1000, 7919",
        // higher constraints
        "5000, 48611", "10_000, 104_729", "10_001, 104_743"
    )
    fun `getNthPrime correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.getNthPrime(n))
    }
}