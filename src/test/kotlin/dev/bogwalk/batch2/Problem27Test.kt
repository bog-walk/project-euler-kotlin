package dev.bogwalk.batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

internal class QuadraticPrimesTest {
    private val tool = QuadraticPrimes()

    @ParameterizedTest(name="N={0}: a={1} b={2}")
    @CsvSource(
        // lower constraints
        "42, -1, 41, 42", "50, -5, 47, 44",
        // upper constraints
        "1000, -61, 971, 72", "2000, -79, 1601, 81"
    )
    fun `quadPrimeCoeff correct`(n: Int, a: Int, b: Int, primes: Int) {
        assertEquals(Triple(a, b, primes), tool.quadPrimeCoeff(n))
    }
}