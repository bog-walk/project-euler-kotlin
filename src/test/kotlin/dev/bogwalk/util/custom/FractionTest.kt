package dev.bogwalk.util.custom

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FractionTest {
    private lateinit var sampleFractions: List<Pair<Fraction, Fraction>>

    @BeforeAll
    fun setUp() {
        sampleFractions = listOf(
            Fraction(3, 5) to Fraction(1, 4),
            Fraction(3, 4) to Fraction(1, 4),
            Fraction(-16, 3) to Fraction(1, 4),
            Fraction(11, 13) to Fraction(-7, 8)
        )
    }

    @Test
    fun `Fraction normalises itself on construction`() {
        val inputs = listOf(
            0 to 1, 0 to 2, 1 to 3, 6 to 2, 15 to 25, 16 to -10, -99 to 336, -3 to -5
        )
        val expected = listOf(
            0 to 1, 0 to 1, 1 to 3, 3 to 1, 3 to 5, -8 to 5, -33 to 112, 3 to 5
        )

        for ((input, e) in inputs.zip(expected)) {
            val actual = Fraction(input.first, input.second)
            assertEquals(e.first, actual.numerator)
            assertEquals(e.second, actual.denominator)
        }
    }

    @Test
    fun `Fraction does not allow 0 denominator`() {
        assertThrows<IllegalArgumentException> { Fraction(5, 0) }

        val a = Fraction(1, 4)
        val b = Fraction(0, 2)
        assertThrows<IllegalArgumentException> { a / b }
    }

    @Test
    fun `Fraction equality correct`() {
        val a = Fraction(2, 4)
        val b = Fraction(1, 2)
        val c = Fraction(3, 5)
        val d = c

        assertEquals(a, b)
        assertNotEquals(a, c)
        assertEquals(c, d)
        assertNotEquals(b, d)
    }

    @Test
    fun `addition of Fractions`() {
        val expected = listOf(
            Fraction(17, 20), Fraction(1, 1),
            Fraction(-61, 12), Fraction(-3, 104)
        )

        for ((fractions, e) in sampleFractions.zip(expected)) {
            assertEquals(e, fractions.first + fractions.second)
        }
    }

    @Test
    fun `subtraction of Fractions`() {
        val expected = listOf(
            Fraction(7, 20), Fraction(1, 2),
            Fraction(-67, 12), Fraction(179, 104)
        )

        for ((fractions, e) in sampleFractions.zip(expected)) {
            assertEquals(e, fractions.first - fractions.second)
        }
    }

    @Test
    fun `multiplication of Fractions`() {
        val expected = listOf(
            Fraction(3, 20), Fraction(3, 16),
            Fraction(-4, 3), Fraction(-77, 104)
        )

        for ((fractions, e) in sampleFractions.zip(expected)) {
            assertEquals(e, fractions.first * fractions.second)
        }
    }

    @Test
    fun `division of Fractions`() {
        val expected = listOf(
            Fraction(12, 5), Fraction(3, 1),
            Fraction(-64, 3), Fraction(-88, 91)
        )

        for ((fractions, e) in sampleFractions.zip(expected)) {
            assertEquals(e, fractions.first / fractions.second)
        }
    }
}