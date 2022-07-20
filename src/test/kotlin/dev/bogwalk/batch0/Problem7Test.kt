package dev.bogwalk.batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class The10001stPrimeTest {
    private val tool = The10001stPrime()
    private lateinit var allPrimes: List<Int>

    @BeforeAll
    fun setup() {
        allPrimes = tool.getAllPrimes(10_001)
    }

    @ParameterizedTest(name="{0}th prime = {1}")
    @CsvSource(
        // lower constraints
        "1, 2", "2, 3", "3, 5", "4, 7", "5, 11", "6, 13",
        // normal values
        "20, 71", "62, 293", "99, 523", "101, 547", "173, 1031",
        // large values
        "250, 1583", "289, 1879", "919, 7193", "1000, 7919", "1284, 10499",
        // higher constraints
        "5000, 48611", "7777, 79357", "10_000, 104_729", "10_001, 104_743"
    )
    fun `getNthPrime correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.getNthPrime(n))
        assertEquals(expected, allPrimes[n-1])
    }
}