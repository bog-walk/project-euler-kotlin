package dev.bogwalk.util.custom

import dev.bogwalk.util.maths.gcd
import dev.bogwalk.util.maths.lcm

/**
 * Provides support for basic rational number arithmetic.
 *
 * @property [numerator] numerator of fraction in its lowest term.
 * @property [denominator] denominator of fraction in its lowest term.
 * @throws IllegalArgumentException if attempting to create a Fraction with a denominator
 * equivalent to 0.
 */
class Fraction(numerator: Int = 0, denominator: Int = 1) {
    var numerator: Int = numerator
        private set
    var denominator: Int = denominator
        private set

    init {
        require(denominator != 0) { "Denominator cannot be 0" }
        var divisor = gcd(1L * this.numerator, 1L * this.denominator).toInt()
        if (this.denominator < 0) divisor = -divisor
        this.numerator /= divisor
        this.denominator /= divisor
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Fraction &&
                this.numerator == other.numerator && this.denominator == other.denominator
    }

    override fun hashCode(): Int {
        var result = numerator
        result = 31 * result + denominator
        return result
    }

    override fun toString(): String = "Fraction($numerator/$denominator)"

    operator fun plus(other: Fraction): Fraction {
        val denominator = lcm(denominator.toLong(), other.denominator.toLong()).toInt()
        val first = denominator / this.denominator * numerator
        val second = denominator / other.denominator * other.numerator
        return Fraction((first + second), denominator)
    }

    operator fun minus(other: Fraction): Fraction {
        val denominator = lcm(denominator.toLong(), other.denominator.toLong()).toInt()
        val first = denominator / this.denominator * numerator
        val second = denominator / other.denominator * other.numerator
        return Fraction((first - second), denominator)
    }

    operator fun times(other: Fraction): Fraction {
        val numerators = numerator * other.numerator
        val denominators = denominator * other.denominator
        return Fraction(numerators, denominators)
    }

    operator fun div(other: Fraction): Fraction {
        val first = numerator * other.denominator
        val second = denominator * other.numerator
        return Fraction(first, second)
    }
}