package util.combinatorics

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ReusableTest {
    @Nested
    @DisplayName("combinations test suite")
    inner class Combinations {
        @Test
        fun `combinations returns empty list if r greater than n or r equal 0`() {
            val input = "ABCD".toList()
            var r = 5
            assertTrue { combinations(input, r).toList().isEmpty() }
            r = 0
            assertTrue { combinations(input, r).toList().isEmpty() }
        }

        @Test
        fun `combinations returns 1 combo if n equal 1`() {
            val input = "A".toList()
            val r = 1
            val expected = listOf("A")
            assertContentEquals(
                expected, combinations(input, r).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `combinations correct for small-sized string`() {
            val input = "ABCD".toList()
            val r = 2
            val expected = listOf("AB", "AC", "AD", "BC", "BD", "CD")
            assertContentEquals(
                expected, combinations(input, r).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `combinations correct for small-sized range`() {
            val input = 0..3
            val r = 3
            val expected = listOf(12, 13, 23, 123)
            assertContentEquals(
                expected,
                combinations(input, r).map { it.joinToString("").toInt() }.toList()
            )
        }

        @Test
        fun `combinations correct for large-sized range`() {
            val input = 1..9
            val r = 6
            val expectedSize = 84
            assertEquals(expectedSize, combinations(input, r).toList().size)
        }
    }

    @Nested
    @DisplayName("combinations with replacement test suite")
    inner class CombinationsWithReplacement {
        @Test
        fun `combinations returns empty list if either n or r equal 0`() {
            var input = "ABCD".toList()
            var r = 0
            assertTrue { combinationsWithReplacement(input, r).toList().isEmpty() }
            input = emptyList()
            r = 1
            assertTrue { combinationsWithReplacement(input, r).toList().isEmpty() }
        }

        @Test
        fun `combinationsWR correct when r greater than n`() {
            val input = "AB".toList()
            val r = 4
            val expected = listOf("AAAA", "AAAB", "AABB", "ABBB", "BBBB")
            assertContentEquals(
                expected,
                combinationsWithReplacement(input, r).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `combinations returns 1 combo if n equal 1`() {
            val input = "A".toList()
            val r = 1
            val expected = listOf("A")
            assertContentEquals(
                expected,
                combinationsWithReplacement(input, r).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `combinations correct for small-sized string`() {
            val input = "ABCD".toList()
            val r = 2
            val expected = listOf("AA", "AB", "AC", "AD", "BB", "BC", "BD", "CC", "CD", "DD")
            assertContentEquals(
                expected,
                combinationsWithReplacement(input, r).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `combinations correct for small-sized range`() {
            val input = 0..3
            val r = 3
            val expectedSize = 20
            assertEquals(expectedSize, combinationsWithReplacement(input, r).toList().size)
        }

        @Test
        fun `combinations correct for large-sized range`() {
            val input = 1..9
            val r = 6
            val expectedSize = 3003
            assertEquals(expectedSize, combinationsWithReplacement(input, r).toList().size)
        }
    }

    @Nested
    @DisplayName("permutations test suite")
    inner class Permutations {
        @Test
        fun `permutations returns empty list if r greater than n or r equal 0`() {
            val input = "ABC".toList()
            var r = 5
            assertTrue { permutations(input.toMutableList(), r).toList().isEmpty() }
            r = 0
            assertTrue { permutations(input, r).toList().isEmpty() }
        }

        @Test
        fun `both permutations correct with small-sized list & n equal r`() {
            val chars = listOf(listOf('0'), listOf('0', '1'), listOf('0', '1', '2'))
            val expected = listOf(
                listOf("0"), listOf("01", "10"), listOf("012", "021", "102", "120", "201", "210")
            )
            for ((i, input) in chars.withIndex()) {
                assertContentEquals(
                    expected[i],
                    getPermutations(input.toMutableList(), i + 1).sorted()
                )
                assertContentEquals(
                    expected[i],
                    permutations(input).map { it.joinToString("") }.toList()
                )
            }
        }

        @Test
        fun `both permutations correct with large-sized list & n equal r`() {
            val chars = ('0'..'9').toMutableList()
            val factorials = listOf(24, 120, 720, 5040, 40320, 362_880)
            for (n in 4..9) {
                val perms = getPermutations(chars.subList(0, n), n)
                assertEquals(factorials[n - 4], perms.size)
                assertEquals(factorials[n - 4], permutations(chars.subList(0, n)).toList().size)
            }
        }

        @Test
        fun `permutations correct & already sorted for large-sized range`() {
            val input = 1..9
            val r = 6
            val expectedSize = 60480
            val expectedTail = listOf(
                987_634, 987_635, 987_641, 987_642, 987_643, 987_645, 987_651, 987_652,
                987_653, 987_654
            )
            val perms = permutations(input, r).toList()
            assertEquals(expectedSize, perms.size)
            assertContentEquals(
                expectedTail,
                perms.takeLast(10).map { it.joinToString("").toInt() }
            )
        }

        @Test
        fun `permutations correct for input with greater than 10 elements`() {
            val input = 'A'..'P'
            val r = 5
            val expectedSize = 524_160
            val expectedTail = listOf("PONMH", "PONMI", "PONMJ", "PONMK", "PONML")
            val perms = permutations(input, r).toList()
            assertEquals(expectedSize, perms.size)
            assertContentEquals(
                expectedTail,
                perms.takeLast(5).map { it.joinToString("") }
            )
        }

        @Test
        fun `speed comparison when r equal n`() {
            val input = ('0'..'9').toMutableList()
            val r = input.size
            val expectedSize = 3_628_800
            val speeds = mutableListOf<Pair<String, Long>>()
            getSpeed(::getPermutations, input, r).run {
                speeds.add("Heap algorithm" to second)
                assertEquals(expectedSize, first.size)
            }
            getSpeed(::permutations, input, r).run {
                speeds.add("Python algorithm" to second)
                assertEquals(expectedSize, first.toList().size)
            }
            compareSpeed(speeds)
        }
    }
}