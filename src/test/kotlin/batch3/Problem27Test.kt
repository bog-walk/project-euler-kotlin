package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

internal class QuadraticPrimesTest {
    private val tool = QuadraticPrimes()

    @ParameterizedTest(name="N={0}: a={1} b={2}")
    @CsvSource(
        "50, -5, 47, 44", "42, -1, 41, 42", "1000, -61, 971, 72"
    )
    fun testQuadPrimeCoeff(n: Int, a: Int, b: Int, primes: Int) {
        assertEquals(Triple(a, b, primes), tool.quadPrimeCoeff(n))
    }
}