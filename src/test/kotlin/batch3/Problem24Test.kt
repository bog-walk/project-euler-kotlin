package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
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
        assertEquals(expected, tool.lexicographicPermImproved(permutationIndexed, input))
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
            assertEquals(expected[i], tool.lexicographicPermImproved(p, input))
        }
    }

    @Test
    fun testLexicoPerms_speed() {
        val input = "0123456789"
        val permutation = 3628799L
        var ogAns = ""
        var altAns = ""
        val ogTime = measureNanoTime {
            repeat(100) {
                ogAns = tool.lexicographicPerm(permutation, input)
            }
        }
        val altTime = measureNanoTime {
            repeat(100) {
                altAns = tool.lexicographicPermImproved(permutation, input)
            }
        }
        println("Improved solution took: ${1.0 * ogTime / 1_000_000}ms\n" +
                "Alt solution took: ${1.0 * altTime / 1_000_000}ms")
        assertEquals(ogAns, altAns)
    }
}