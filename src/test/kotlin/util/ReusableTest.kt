package util

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReusableTest {
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

    @ParameterizedTest(name="gcd({0}, {1}) is {2}")
    @CsvSource(
        "366, 60, 6", "81, 153, 9",
        "10, 100, 10", "1, 1, 1",
        "456, 32, 8", "-100, 10, 10",
        "-60, -366, 6"
    )
    fun testGCD(x: Int, y: Int, expected: Int) {
        assertEquals(expected, gcd(x, y))
    }
}