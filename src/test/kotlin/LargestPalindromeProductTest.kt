import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestPalindromeProductTest {
    @ParameterizedTest(name="Less than {0}")
    @CsvSource(
        // lower constraint
        "101102, 101101", "101110, 101101",
        // normal value
        "794000, 793397", "650001, 649946",
        // palindrome value
        "332233, 330033",
        // higher constraint
        "1000000, 906609"
    )
    fun testLargestPalindromeProduct(max: Int, expected: Int) {
        val tool = LargestPalindromeProduct()
        assertEquals(expected, tool.largestPalindromeProduct(max))
    }

    @Test
    fun testIsPalindrome() {
        assertTrue(5.isPalindrome())
        assertTrue(22.isPalindrome())
        assertTrue(303.isPalindrome())
        assertTrue(9119.isPalindrome())
        assertFalse(10.isPalindrome())
        assertFalse(523.isPalindrome())
        assertFalse(8018.isPalindrome())
        assertFalse(124521.isPalindrome())
    }

    @Test
    fun testGetPrevPalindrome() {
        val tool = LargestPalindromeProduct()
        assertEquals(101101, tool.getPrevPalindrome(101110))
        assertEquals(799997, tool.getPrevPalindrome(800000))
        assertEquals(200002, tool.getPrevPalindrome(200003))
        assertEquals(649946, tool.getPrevPalindrome(650001))
        assertEquals(332233, tool.getPrevPalindrome(333333))
    }

    @Test
    fun testIs3DigProduct() {
        val tool = LargestPalindromeProduct()
        assertTrue(tool.is3DigProduct(793397))
        assertTrue(tool.is3DigProduct(101101))
        assertTrue(tool.is3DigProduct(649946))
        assertFalse(tool.is3DigProduct(200002))
        assertFalse(tool.is3DigProduct(799997))
    }
}