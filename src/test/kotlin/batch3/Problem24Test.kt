package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LexicographicPermutationsTest {
    private val tool = LexicographicPermutations()

    @ParameterizedTest(name="{0} p{1} = {2}")
    @CsvSource(
        "'abc', 1, 'abc'", "'abc', 4, 'bca'",
        "'012', 2, '021'", "'012', 6, '210'"
    )
    fun testLexicoPerms(input: String, permutation: Long, expected: String) {
        val permutationIndexed = permutation - 1
        assertEquals(expected, tool.lexicographicPerm(permutationIndexed, input))
    }

    @Test
    fun testLexicoPerms_longInput() {
        val input = "0123456789"
        // 3628800 permutations already 0-indexed
        val permutations = listOf<Long>(0, 362880, 999999, 1088640, 3628799)
        val expected = listOf(
            "0123456789", "1023456789", "2783915460", "3012456789", "9876543210"
        )
        permutations.forEachIndexed { i, p ->
            assertEquals(expected[i], tool.lexicographicPerm(p, input))
        }
    }
}