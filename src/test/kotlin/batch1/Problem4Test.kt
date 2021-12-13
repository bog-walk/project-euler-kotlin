package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestPalindromeProductTest {
    private val tool = LargestPalindromeProduct()

    @Test
    fun testGetPrevPalindrome() {
        val numbers = listOf(101110, 800000, 200003, 650001, 333333)
        val expected = listOf(101101, 799997, 200002, 649946, 332233)
        numbers.forEachIndexed { index, number ->
            assertEquals(expected[index], tool.getPrevPalindrome(number))
        }
    }

    @Test
    fun testIs3DigProduct_allTrue() {
        val palindromes = listOf(793397, 101101, 649946)
        palindromes.forEach { palindrome ->
            assertTrue(tool.is3DigProduct(palindrome))
        }
    }

    @Test
    fun testIs3DigProduct_allFalse() {
        val palindromes = listOf(200002, 799997)
        palindromes.forEach { palindrome ->
            assertFalse(tool.is3DigProduct(palindrome))
        }
    }

    @ParameterizedTest(name="Less than {0}")
    @CsvSource(
        // lower constraints
        "101102, 101101", "101110, 101101",
        // normal value
        "794000, 793397", "650001, 649946",
        // palindrome value
        "332233, 330033",
        // upper constraints
        "1000000, 906609"
    )
    fun testLargestPalindromeProduct(max: Int, expected: Int) {
        assertEquals(expected, tool.largestPalindromeProduct(max))
        assertEquals(expected, tool.largestPalindromeProductCountingDown(max))
    }
}