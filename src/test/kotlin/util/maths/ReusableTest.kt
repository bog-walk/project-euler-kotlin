package util.maths

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.*

internal class ReusableTest {
    @ParameterizedTest(name="{0} = {1}")
    @CsvSource(
        // lower constraints
        "1, 1", "2, 3", "3, 6", "50, 1275",
        // normal values
        "100, 5050", "2234, 2_496_495"
    )
    fun `gaussian sum correct`(n: Int, expected: Long) {
        assertEquals(expected, n.gaussianSum())
    }

    @ParameterizedTest(name="gcd({0}, {1}) = {2}")
    @CsvSource(
        // same values
        "1, 1, 1",
        // obvious values
        "366, 60, 6", "10, 100, 10",
        // normal values
        "456, 32, 8", "81, 153, 9",
        // negative values
        "-100, 10, 10", "-60, -366, 6"
    )
    fun `greatest common divisor correct`(x: Long, y: Long, expected: Long) {
        assertEquals(expected, gcd(x, y))
    }

    @Nested
    @DisplayName("factorial test suite")
    inner class Factorial {
        @ParameterizedTest(name="{0}! = {1}")
        @CsvSource(
            // lower constraints
            "0, 1", "1, 1", "2, 2", "3, 6", "4, 24", "5, 120",
            // normal values
            "10, 3_628_800"
        )
        fun `factorial correct with valid input`(n: Int, expected: Long) {
            assertEquals(expected.toBigInteger(), n.factorial())
        }

        @Test
        fun `factorial throws exception with invalid input`() {
            assertThrows<IllegalArgumentException> { (-5).factorial() }
        }
    }

    @Nested
    @DisplayName("isPentagonal test suite")
    inner class IsPentagonal {
        @Test
        fun `isPentagonal correct with pentagonal numbers`() {
            val nums = listOf<Long>(1, 5, 12, 22, 35, 247, 532, 1001)
            val expected = listOf(1, 2, 3, 4, 5, 13, 19, 26)
            nums.forEachIndexed { i, n ->
                assertEquals(expected[i], n.isPentagonalNumber())
            }
        }

        @Test
        fun `isPentagonal returns null with non-pentagonal numbers`() {
            val nums = listOf<Long>(2, 23, 100, 313, 691, 1111)
            nums.forEach { n ->
                assertNull(n.isPentagonalNumber())
            }
        }
    }

    @Nested
    @DisplayName("isPrime test suite")
    inner class IsPrime {
        @Test
        fun `isPrime both return true with small primes`() {
            val nums = listOf(2, 5, 11, 17, 29, 7919, 514_229, 9_369_319)
            nums.forEach { n ->
                assertTrue(n.isPrime())
                assertTrue(n.toLong().isPrimeMRBI())
                assertTrue(n.toLong().isPrimeMR())
            }
        }

        @Test
        fun `isPrime both return false with small composites`() {
            val nums = listOf(1, 4, 9, 14, 221, 9523, 22041, 997_653, 999_715_709)
            nums.forEach { n ->
                assertFalse(n.isPrime())
                assertFalse(n.toLong().isPrimeMRBI())
                assertFalse(n.toLong().isPrimeMR())
            }
        }

        @Test
        fun `isPrime both return true with big primes`() {
            val nums = listOf(2_147_483_647, 99_987_684_473, 999_973_156_643)
            nums.forEach { n ->
                assertTrue(n.isPrimeMRBI())
                assertTrue(n.isPrimeMR())
            }
        }

        @Test
        fun `isPrime both return false with big composites`() {
            val nums = listOf(99_987_684_471)
            nums.forEach { n ->
                assertFalse(n.isPrimeMRBI())
                assertFalse(n.isPrimeMR())
            }
        }

        @Test
        fun `isPrimeMR returns true with huge primes`() {
            val nums = listOf(888_888_877_777_777, 999_998_727_899_999)
            nums.forEach { n ->
                assertTrue(n.isPrimeMRBI())
                assertTrue(n.isPrimeMR())
            }
        }

        @Test
        fun `isPrimeMR returns false with huge composites`() {
            val nums = listOf(3_889_108_085_625, 809_709_509_409_105)
            nums.forEach { n ->
                assertFalse(n.isPrimeMRBI())
                assertFalse(n.isPrimeMR())
            }
        }
    }

    @Nested
    @DisplayName("istriangular test suite")
    inner class IsTriangular {
        @Test
        fun `isTriangular correct with triangular numbers`() {
            val nums = listOf<Long>(1, 3, 6, 10, 190, 325, 496, 595)
            val expected = listOf(1, 2, 3, 4, 19, 25, 31, 34)
            nums.forEachIndexed { i, n ->
                assertEquals(expected[i], n.isTriangularNumber())
            }
        }

        @Test
        fun `isTriangular returns null with non-triangular numbers`() {
            val nums = listOf<Long>(2, 8, 46, 121, 173, 299, 403)
            nums.forEach { n ->
                assertNull(n.isTriangularNumber())
            }
        }
    }

    @Nested
    @DisplayName("lcm test suite")
    inner class LCM {
        @ParameterizedTest(name="lcm({0}, {1}) = {2}")
        @CsvSource(
            // lower constraints
            "1, 2, 2", "12, 24, 24", "3, 64, 192",
            // negative values
            "-4, 41, 164", "-6, -27, 54",
            // upper constraints
            "101, 63, 6363"
        )
        fun `least common multiple correct with 2 arguments`(x: Long, y: Long, expected: Long) {
            assertEquals(expected, lcm(x, y))
        }

        @Test
        fun `least common multiple correct with multiple arguments`() {
            val arguments = listOf(
                longArrayOf(2, 6, 12), longArrayOf(31, 5, 7, 14), longArrayOf(8, 11, 3, 5, 2, 7, 6)
            )
            val expected = listOf<Long>(12, 2170, 9240)
            for ((i, args) in arguments.withIndex()) {
                assertEquals(expected[i], lcm(*args))
            }
        }

        @Test
        fun `least common multiple throws exception with invalid input`() {
            assertThrows<IllegalArgumentException> { lcm(0, 2) }
            assertThrows<IllegalArgumentException> { lcm(2, 0) }
            assertThrows<IllegalArgumentException> { lcm(0, 0) }
        }
    }

    @ParameterizedTest(name="sum({1}^{2}) = {0}")
    @CsvSource(
        // lower constraints
        "1, 0, 0", "0, 0, 1", "1, 1, 0", "1, 1, 10", "1, 2, 0", "4, 2, 2", "8, 2, 9",
        "47, 2, 31", "7, 4, 5", "43, 7, 10", "1, 10, 100", "7, 20, 10"
    )
    fun `powerDigitSum correct`(expected: Int, base: Int, exp: Int) {
        assertEquals(expected, powerDigitSum(base, exp))
    }

    @Nested
    @DisplayName("primeFactors test suite")
    inner class Primefactors {
        @Test
        fun `primeFactors correct`() {
            val nums = listOf<Long>(2, 12, 100, 999)
            val expected = listOf<List<Long>>(
                listOf(2), listOf(2, 2, 3), listOf(2, 2, 5, 5), listOf(3, 3, 3, 37)
            )
            nums.forEachIndexed { i, n ->
                val primeFactors = primeFactors(n).flatMap { (prime, exp) ->
                    List(exp) { prime }
                }
                assertEquals(expected[i], primeFactors)
            }
        }

        @Test
        fun `primeFactors throws exception with invalid input`() {
            assertThrows<IllegalArgumentException> { primeFactors(1L) }
            assertThrows<IllegalArgumentException> { primeFactors(-2L) }
        }
    }

    @Nested
    @DisplayName("primeNumbers test suite")
    inner class PrimeNumbers {
        @Test
        fun `primeNumbers correct with small N`() {
            val n = 30
            val expected = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
            assertEquals(expected, primeNumbersOG(n))
            assertEquals(expected, primeNumbers(n))
        }

        @Test
        fun `primeNumbers correct with large N`() {
            val n = 10_000
            val expectedSize = 1229
            val expectedTail = listOf(9887, 9901, 9907, 9923, 9929, 9931, 9941, 9949, 9967, 9973)
            val actualOG = primeNumbersOG(n)
            val actualNew = primeNumbers(n)
            assertEquals(expectedSize, actualOG.size)
            assertEquals(expectedSize, actualNew.size)
            assertEquals(expectedTail, actualOG.takeLast(10))
            assertEquals(expectedTail, actualNew.takeLast(10))
        }
    }

    @Nested
    @DisplayName("pythagoreanTriplet test suite")
    inner class PythagoreanTriplet {
        @Test
        fun `pythagoreanTriplet correct with valid input`() {
            val d = 1
            val arguments = listOf(2 to 1, 3 to 2, 4 to 1, 4 to 3)
            val expected = listOf(
                Triple(3, 4, 5), Triple(5, 12, 13),
                Triple(8, 15, 17), Triple(7, 24, 25)
            )
            arguments.forEachIndexed { i, (m, n) ->
                assertEquals(expected[i], pythagoreanTriplet(m, n, d))
            }
        }

        @Test
        fun `pythagoreanTriplet throws exception with invalid input`() {
            val d = 1
            val arguments = listOf(1 to 0, 1 to 10, 5 to 3, 9 to 3)
            arguments.forEach { (m, n) ->
                assertThrows<IllegalArgumentException> { pythagoreanTriplet(m, n, d) }
            }
        }
    }

    @ParameterizedTest(name="sum(d({0})) = {1}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 1", "3, 1", "12, 16", "20, 22",
        // perfect squares
        "36, 55", "49, 8",
        // normal values
        "220, 284", "284, 220", "999, 521",
        // upper constraints
        "5500, 7604", "100_000, 146_078"
    )
    fun `sumProperDivisors correct`(n: Int, expected: Int) {
        assertEquals(expected, sumProperDivisorsOG(n))
        assertEquals(expected, sumProperDivisors(n))
    }
}