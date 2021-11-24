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
    fun testPrimeNumbers_small() {
        val max = 30
        val expected = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
        assertEquals(expected, primeNumbers(max))
    }

    @Test
    fun testPrimeNumbers_large() {
        val max = 10000
        val actual = primeNumbers(max)
        val expectedSize = 1229
        val expectedTail = listOf(
            9887, 9901, 9907, 9923, 9929, 9931, 9941, 9949, 9967, 9973
        )
        assertEquals(expectedSize, actual.size)
        assertEquals(expectedTail, actual.takeLast(10))
    }

    @ParameterizedTest(name="{0} = {1}")
    @CsvSource(
        "1, 1", "2, 3", "3, 6", "50, 1275",
        "100, 5050", "2234, 2496495"
    )
    fun testGaussianSum(n: Int, expected: Long) {
        assertEquals(expected, n.gaussianSum())
    }

    @ParameterizedTest(name="d({0}) = {1}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 1", "3, 1", "12, 16", "20, 22",
        // perfect squares
        "36, 55", "49, 8",
        // normal values
        "220, 284", "284, 220", "999, 521",
        // upper constraints
        "5500, 7604", "100000, 146078"
    )
    fun testSumProperDivisors(n: Int, expected: Int) {
        assertEquals(expected, sumProperDivisorsOG(n))
        assertEquals(expected, sumProperDivisorsPF(n))
    }

    @Test
    fun testIsPrime_allTrue() {
        val nums = listOf(2, 5, 11, 17, 29)
        for (num in nums) {
            assertTrue(isPrime(num))
        }
    }

    @Test
    fun testIsPrime_allFalse() {
        val nums = listOf(1, 4, 9, 14)
        for (num in nums) {
            assertFalse(isPrime(num))
        }
    }
}