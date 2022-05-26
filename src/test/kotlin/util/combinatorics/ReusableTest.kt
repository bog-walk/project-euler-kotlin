package util.combinatorics

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import java.math.BigInteger
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ReusableTest {
    @Nested
    @DisplayName("binomial coefficient test suite")
    inner class BinomialCoefficient {
        @ParameterizedTest(name="{0}_C_{1} = {2}")
        @CsvSource(
            // k > n
            "0, 1, 0", "1, 2, 0",
            // lower constraints
            "0, 0, 1", "1, 0, 1", "1, 1, 1",
            // normal values
            "3, 1, 3", "4, 2, 6", "12, 3, 220", "20, 7, 77520",
            // larger values
            "30, 18, 86493225", "100, 40, 13746234145802811501267369720"
        )
        fun `binomial coefficient correct with valid input`(n: Int, k: Int, expected: String) {
            assertEquals(BigInteger(expected), binomialCoefficient(n, k))
        }

        @Test
        fun `binomial coefficient throws exception with invalid input`() {
            assertThrows<IllegalArgumentException> { binomialCoefficient(5, -1) }
            assertThrows<IllegalArgumentException> { binomialCoefficient(-10, 2) }
        }
    }

    @Nested
    @DisplayName("combinations test suite")
    inner class Combinations {
        @Test
        fun `combinations returns empty list if r greater than n or r equal 0`() {
            val input = "ABCD".toList()
            var r = 5
            assertTrue { combinations(input, r).toList().isEmpty() }
            assertTrue { getCombinations(input, r).toList().isEmpty() }
            r = 0
            assertTrue { combinations(input, r).toList().isEmpty() }
            assertTrue { getCombinations(input, r).toList().isEmpty() }
        }

        @Test
        fun `combinations returns 1 combo if n equal 1`() {
            val input = "A".toList()
            val r = 1
            val expected = listOf("A")
            assertContentEquals(
                expected, combinations(input, r).map { it.joinToString("") }.toList()
            )
            assertContentEquals(
                expected,
                getCombinations(input, r).map { it.joinToString("") }.toList()
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
            assertContentEquals(
                expected,
                getCombinations(input, r).map { it.joinToString("") }.toList()
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
            assertContentEquals(
                expected,
                getCombinations(input, r).map { it.joinToString("").toInt() }.toList()
            )
        }

        @Test
        fun `speed comparison for large-sized range`() {
            val input = 1..19
            val r = 15
            val expectedSize = 3876
            val speeds = mutableListOf<Pair<String, Benchmark>>()
            getSpeed(::getCombinations, input, r).run {
                speeds.add("Old" to second)
                assertEquals(expectedSize, first.size)
            }
            val newActual: List<List<Int>>
            val newTime = measureNanoTime {
                newActual = combinations(input, r).toList()
            }
            compareSpeed("New" to newTime)
            assertEquals(expectedSize, newActual.size)
            compareSpeed(speeds)
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
            val speeds = mutableListOf<Pair<String, Benchmark>>()
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

    @Test
    fun `permutationID correct when less than 9 duplicates of a digit exist`() {
        val nums = listOf<Long>(
            1487, 2214, 999, 15, 148_748_178_147, 1_000_000_000
        )
        val expected = listOf(
            intArrayOf(0, 1, 0, 0, 1, 0, 0, 1, 1), intArrayOf(0, 1, 2, 0, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 3), intArrayOf(0, 1, 0, 0, 0, 1),
            intArrayOf(0, 3, 0, 0, 3, 0, 0, 3, 3), intArrayOf(9, 1)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], permutationID(n))
        }
    }

    @Test
    fun `permutationID correct when more than 9 duplicates of a digit exist`() {
        val nums = listOf(
            1_000_000_000_000, 31_111_111_111, 999_999_999_999
        )
        val expected = listOf(
            intArrayOf(12, 1), intArrayOf(0, 10, 0, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 12)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], permutationID(n))
        }
    }

    @Test
    fun `permutationID static for permutations of the same number`() {
        val nums = listOf<Long>(1487, 4871, 8714, 7814, 1748)
        val expected = intArrayOf(0, 1, 0, 0, 1, 0, 0, 1, 1)
        for (n in nums) {
            assertContentEquals(expected, permutationID(n))
        }
    }

    @Nested
    @DisplayName("product test suite")
    inner class Product {
        @Test
        fun `product returns empty list if empty iterables provided`() {
            assertTrue { product(emptyList(), emptyList()).toList().isEmpty() }
        }

        @Test
        fun `product returns 1 product if single iterable with single element provided`() {
            val input = "A".toList()
            val expected = listOf("A")
            assertContentEquals(
                expected, product(input).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `product correct for small-sized single iterable`() {
            val input = "ABC".toList()
            val expected = listOf("A", "B","C")
            assertContentEquals(
                expected, product(input).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `product correct for 2 small-sized iterables of different lengths`() {
            val input1 = "ABCD".toList()
            val input2 = "xy".toList()
            val expected = listOf("Ax", "Ay", "Bx", "By", "Cx", "Cy", "Dx", "Dy")
            assertContentEquals(
                expected,
                product(input1, input2).map { it.joinToString("") }.toList()
            )
        }

        @Test
        fun `product correct for multiple small-sized iterables`() {
            val inputs = arrayOf(0..2, 1..3, 5..8)
            val expectedSize = 36
            val expectedHead = listOf(15, 16, 17,  18, 25, 26)
            val expectedTail = listOf(227, 228, 235, 236, 237, 238)
            val actual = product(*inputs).map { it.joinToString("").toInt() }.toList()
            assertEquals(expectedSize, actual.size)
            assertContentEquals(expectedHead, actual.take(6))
            assertContentEquals(expectedTail, actual.takeLast(6))
        }

        @Test
        fun `product correct for 2 large-sized iterables`() {
            val inputs = arrayOf('A'..'P', '1'..'9')
            val expectedSize = 144
            val expectedHead = listOf(
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "B1"
            )
            val expectedTail = listOf(
                "O9", "P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9"
            )
            val actual = product(*inputs).map { it.joinToString("") }.toList()
            assertEquals(expectedSize, actual.size)
            assertContentEquals(expectedHead, actual.take(10))
            assertContentEquals(expectedTail, actual.takeLast(10))
        }

        @Test
        fun `product correct for 1 iterable when repeat greater than 1`() {
            val input = 0..2
            val repeat = 2
            val expected = listOf(
                listOf(0, 0), listOf(0, 1), listOf(0, 2), listOf(1, 0), listOf(1, 1), listOf(1, 2),
                listOf(2, 0), listOf(2, 1), listOf(2, 2)
            )
            assertContentEquals(expected, product(input, repeat=repeat).toList())
        }

        @Test
        fun `product correct for 2 iterables when repeat greater than 1`() {
            val input1 = "ab".toList()
            val input2 = "cd".toList()
            val repeat = 3
            val expectedSize = 64
            val expectedHead = listOf(
                "acacac", "acacad", "acacbc", "acacbd", "acadac"
            )
            val expectedTail = listOf(
                "bdbcbd", "bdbdac", "bdbdad", "bdbdbc", "bdbdbd"
            )
            val actual = product(input1, input2, repeat=repeat)
                .map { it.joinToString("") }
                .toList()
            assertEquals(expectedSize, actual.size)
            assertContentEquals(expectedHead, actual.take(5))
            assertContentEquals(expectedTail, actual.takeLast(5))
        }
    }
}