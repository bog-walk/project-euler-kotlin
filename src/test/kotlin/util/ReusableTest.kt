package util

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReusableTest {
    @ParameterizedTest(name="gcd({0}, {1}) is {2}")
    @CsvSource(
        "366, 60, 6", "81, 153, 9",
        "10, 100, 10", "1, 1, 1",
        "456, 32, 8", "-100, 10, 10",
        "-60, -366, 6"
    )
    fun testGCD(x: Long, y: Long, expected: Long) {
        assertEquals(expected, gcd(x, y))
    }

    @ParameterizedTest(name="{0}! = {1}")
    @CsvSource(
        "0, 1", "1, 1", "2, 2", "3, 6", "4, 24",
        "5, 120", "10, 3628800"
    )
    fun testFactorial(n: Int, expected: Long) {
        assertEquals(expected.toBigInteger(), n.factorial())
    }

    @Test
    fun testFactorial_invalid() {
        assertThrows<IllegalArgumentException> { (-5).factorial() }
    }

    @Test
    fun testPrimeFactors() {
        val numbers = listOf<Long>(2, 12, 100, 999)
        val expected = listOf<List<Long>>(
            listOf(2), listOf(2, 2, 3), listOf(2, 2, 5, 5), listOf(3, 3, 3, 37))
        numbers.forEachIndexed { index, number ->
            val primeFactors = primeFactors(number).flatMap {
                    (prime, exp) -> List(exp) { prime }
            }
            assertEquals(expected[index], primeFactors)
        }
    }

    @ParameterizedTest(name="lcm({0}, {1}) is {2}")
    @CsvSource(
        "1, 2, 2", "12, 24, 24", "3, 64, 192",
        "-4, 41, 164", "-6, -27, 54",
        "101, 63, 6363"
    )
    fun testLCM(x: Long, y: Long, expected: Long) {
        assertEquals(expected, lcm(x, y))
    }

    @Test
    fun testLCM_invalid() {
        assertThrows<IllegalArgumentException> { lcm(0, 2) }
        assertThrows<IllegalArgumentException> { lcm(2, 0) }
        assertThrows<IllegalArgumentException> { lcm(0, 0) }
    }

    @Test
    fun testIsPrime() {
        assertFalse(1.isPrime())
        assertTrue(2.isPrime())
        assertFalse(6.isPrime())
        assertTrue(5.isPrime())
        assertTrue(11.isPrime())
        assertFalse(14.isPrime())
        assertFalse(15.isPrime())
        assertFalse(21.isPrime())
        assertTrue(17.isPrime())
    }

    // Basic: 24ms; Sieve: 3ms
    @Test
    fun testGetPrimes() {
        val max = 10000
        val basicBefore = System.currentTimeMillis()
        val basic = getPrimeNumbers(max)
        val basicAfter = System.currentTimeMillis()
        val sieveBefore = System.currentTimeMillis()
        val sieve = getPrimesUsingSieve(max)
        val sieveAfter = System.currentTimeMillis()
        println("Basic algorithm took ${basicAfter - basicBefore}ms\n" +
                "Sieve algorithm tool ${sieveAfter - sieveBefore}ms")
        assertEquals(basic, sieve)
    }
}