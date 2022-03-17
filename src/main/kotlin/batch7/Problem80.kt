package batch7

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sqrt
import kotlin.math.truncate

/**
 * Problem 80: Square Root Digital Expansion
 *
 * https://projecteuler.net/problem=80
 *
 * Goal: For the first N natural numbers, find the total of the digit sums of the first P digits
 * for all irrational square roots x, such that x <= N. Note that this includes digits before &
 * after the decimal point.
 *
 * Constraints: 1 <= N <= 1e3 && 1 <= P <= 1e3 OR
 *              1 <= N <= 100 && 1 <= P <= 1e4
 *
 * e.g.: N = 2, P = 20
 *       sqrt(1) = 1 (not irrational)
 *       sqrt(2) = 1.4142135623730950488...
 *       total = 76
 */

class SquareRootDigitalExpansion {
    /**
     * Solution takes advantage of BigDecimal's intrinsic precision capabilities to return a
     * BigDecimal value of sqrt([n]) that has only [p] digits.
     *
     * N.B. This solution uses an in-built function BigDecimal.sqrt() added in Java 9. A manual
     * version of this function was implemented below (using the Babylonian method) to handle
     * HackerRank submissions. This solution has a parameter toggle purely for the purposes of
     * enabling the latter & performing speed tests, for curiosity's sake.
     *
     * SPEED (WORSE for Manual sqrt) 29.50s for N = 100, P = 1e4
     * SPEED (BETTER for Built-in sqrt) 4.98s for N = 100, P = 1e4
     */
    fun irrationalSquareDigitSum(n: Int, p: Int, manualRoot: Boolean = false): Int {
        // set precision (amount of digits before & after decimal point)
        val rules = MathContext(p, RoundingMode.FLOOR)
        var total = 0
        for (num in 2..n) {
            val root = sqrt(1.0 * num)
            if (root - truncate(root) == 0.0) continue // skip perfect squares
            val rootBD = if (manualRoot) {
                num.toBigDecimal().sqrtManual(rules)
            } else {
                num.toBigDecimal().sqrt(rules)
            }
            total += rootBD.toPlainString().sumOf {
                if (it == '.') 0 else it.digitToInt()
            }
        }
        return total
    }

    /**
     * Manual implementation of the Babylonian method that calculates a successive approximation
     * of the square root of a BigDecimal.
     *
     * The Babylonian method oscillates between approximations of the square root until either
     * successive guesses are equal or the iteration has exceeded the requested precision.
     *
     * After the first arbitrary guess G is made, it is assumed that, if G is an overestimate,
     * then [this]/G will be an underestimate, so the average of the 2 estimates is found to get
     * a better approximation, such that:
     *
     *      averageG = (G + [this]/G) / 2
     *
     * @throws ArithmeticException if [this] is < 0.
     */
    fun BigDecimal.sqrtManual(mc: MathContext): BigDecimal {
        if (this < BigDecimal.ZERO) throw ArithmeticException("BigDecimal must be 0 or positive")
        if (this == BigDecimal.ZERO) return BigDecimal.ZERO
        var guess = this.divide(2.toBigDecimal(), mc)
        var i = 0
        while (i < mc.precision + 1) {
            val averageGuess = (guess + this.divide(guess, mc)).divide(2.toBigDecimal(), mc)
            if (guess == averageGuess) break
            guess = averageGuess
            i++
        }
        return guess
    }
}