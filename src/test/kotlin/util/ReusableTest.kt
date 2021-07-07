package util

import org.junit.jupiter.api.Test
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
}