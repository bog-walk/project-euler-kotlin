package util.strings

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ReusableTest {
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

    @Test
    fun `getPermutations correct with small-sized list`() {
        val chars = mutableListOf<Char>()
        val expected = listOf(
            listOf("0"), listOf("01", "10"), listOf("012", "021", "102", "120", "201", "210")
        )
        for ((i, ch) in ('0'..'2').withIndex()) {
            chars.add(ch)
            assertEquals(expected[i], getPermutations(chars, i + 1).sorted())
        }
    }

    @Test
    fun `getPermutations correct with large-sized list`() {
        val chars = ('0'..'9').toMutableList()
        val factorials = listOf(24, 120, 720, 5040, 40320, 362_880, 3_628_800)
        for (n in 4..10) {
            val perms = getPermutations(chars.subList(0, n), n)
            assertEquals(factorials[n - 4], perms.size)
        }
    }

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