package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LexicographicPermutationsTest {
    private val tool = LexicographicPermutations()

    @ParameterizedTest(name="{0} p{1} = {2}")
    @CsvSource(
        "'abc', 1, 'abc'", "'abc', 4, 'bca'",
        "'012', 2, '021'", "'012', 6, '210'"
    )
    fun testLexicoPerms(input: String, permutation: Int, expected: String) {
        TODO()
    }
}