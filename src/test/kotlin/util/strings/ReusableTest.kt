package util.strings

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

internal class ReusableTest {
    @Nested
    @DisplayName("isPalindrome test suite")
    inner class IsPalindrome {
        @Test
        fun `isPalindrome returns true for palindromes`() {
            val nums = listOf("5", "22", "303", "9119")
            nums.forEach { n ->
                assertTrue { n.isPalindromeBuiltIn() }
                assertTrue { n.isPalindromeManual() }
                assertTrue { n.isPalindromeNumber() }
                assertTrue { n.isPalindrome() }
            }
        }

        @Test
        fun `isPalindrome returns false for non-palindromes`() {
            val nums = listOf("10", "523", "8018", "124521")
            nums.forEach { n ->
                assertFalse { n.isPalindromeBuiltIn() }
                assertFalse { n.isPalindromeManual() }
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
}