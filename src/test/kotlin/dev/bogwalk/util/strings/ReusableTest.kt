package dev.bogwalk.util.strings

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReusableTest {
    @Nested
    @DisplayName("isPalindrome test suite")
    inner class IsPalindrome {
        @Test
        fun `isPalindrome returns true for palindromes`() {
            val nums = listOf("0", "5", "22", "303", "9119")
            nums.forEach { n ->
                assertTrue { n.isPalindromeBuiltIn() }
                assertTrue { n.isPalindromeRecursive() }
                assertTrue { n.isPalindromeNumber() }
                assertTrue { n.isPalindrome() }
            }
        }

        @Test
        fun `isPalindrome returns false for non-palindromes`() {
            val nums = listOf("10", "523", "8018", "124521")
            nums.forEach { n ->
                assertFalse { n.isPalindromeBuiltIn() }
                assertFalse { n.isPalindromeRecursive() }
                assertFalse { n.isPalindromeNumber() }
                assertFalse { n.isPalindrome() }
            }
        }
    }

    @Nested
    @DisplayName("isPandigital test suite")
    inner class IsPandigital {
        @Test
        fun `isPandigital returns true for valid strings`() {
            val nums = listOf("1", "231", "54321", "564731982", "1234560789")
            val digits = listOf(1, 3, 5, 9, 10)
            nums.forEachIndexed { i, num ->
                assertTrue { num.isPandigital(digits[i]) }
            }
        }

        @Test
        fun `isPandigital returns false for invalid strings`() {
            val nums = listOf("", "1", "85018", "810")
            val digits = listOf(10, 4, 5, 3)
            nums.forEachIndexed { i, num ->
                assertFalse { num.isPandigital(digits[i]) }
            }
        }
    }

    @ParameterizedTest(name="BI = {0}")
    @CsvSource(
        // small numbers
        "0, 1", "1, 1", "5, 1", "10, 2", "78, 2", "1234, 4", "999999, 6",
        // medium numbers
        "100000000000, 12", "1234567890123456, 16",
        // Int.MAX_VALUE, Long.MAX_VALUE
        "2147483647, 10", "9223372036854775807, 19",
        // large numbers
        "9999999999999999999, 19", "12345678901234567890, 20",
        "999999999999999999999999999999999, 33", "10000000000000000000000000000000000000, 38",
        "16016016098798700000001872635495879999123456737465, 50"
    )
    fun `digitCount correct for BigInteger`(input: String, expected: Int) {
        val num = input.toBigInteger()
        assertEquals(expected, num.digitCount())
    }
}