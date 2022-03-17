package batch7

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sqrt

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
     * N.B. This solution uses a manual square root implementation (using the Babylonian method),
     * instead of the in-built function BigDecimal.sqrt() added in Java 9, to handle HackerRank
     * submissions. This solution has a parameter toggle purely for the purposes of enabling the
     * latter & performing speed tests, for curiosity's sake.
     *
     * SPEED (BETTER for Manual sqrt) 1.94s for N = 100, P = 1e4
     * SPEED (WORSE for Built-in sqrt) 3.98s for N = 100, P = 1e4
     */
    fun irrationalSquareDigitSum(n: Int, p: Int, manualRoot: Boolean = false): Int {
        // set precision (amount of digits before & after decimal point)
        val rules = MathContext(p, RoundingMode.FLOOR)
        var total = 0
        for (num in 2..n) {
            if (sqrt(1.0 * num) % 1 == 0.0) continue // skip perfect squares
            val rootBD = if (manualRoot) {
                num.toBigDecimal().sqrtManual(rules)
            } else {
                num.toBigDecimal().sqrt(rules)
            }
            total += rootBD.toPlainString().sumOf {
                // equivalent to (it - '0').coerceAtLeast(0)
                if (it == '.') 0 else it.digitToInt()
            }
        }
        return total
    }

    /**
     * Solution follows the same idea as the original above, except for the following:
     *
     *  - All square roots are cached in an array to allow quick access for future calculations.
     *
     *  - The square root of a number is only calculated using the manual Babylonian method if
     *  the number is prime. Otherwise, it is calculated using the product of the cached square
     *  roots of its prime factors.
     *
     *  - To allow potential rounding errors from this multiplication, the precision of each
     *  cached BigDecimal root is set to exceed [p]. This means the String version of the
     *  calculated root has to be truncated to the requested size before summing the digits.
     *
     * SPEED (BEST for Manual sqrt) 735.63ms for N = 100, P = 1e4
     */
    fun irrationalSquareDigitSumImproved(n: Int, p: Int): Int {
        val allRoots = Array<BigDecimal>(n + 1) { BigDecimal.ZERO }
        // set precision in excess to account for potential future multiplication rounding errors
        val rules = MathContext(p + 10, RoundingMode.FLOOR)
        var total = 0
        for (num in 2..n) {
            var approx = 1
            while (approx * approx < num) {
                approx++
            }
            if (approx * approx == num) { // store then skip perfect squares
                allRoots[num] = approx.toBigDecimal()
                continue
            }
            var factor = approx - 1
            while (num % factor != 0) {
                factor--
            }
            val rootBD = if (factor > 1) {
                // if num is composite, multiply the first factors found
                allRoots[factor].multiply(allRoots[(num/factor)], rules)
            } else {
                // if num is prime, get the root manually
                num.toBigDecimal().sqrtManual(rules)
            }
            allRoots[num] = rootBD
            // truncate string to requested size plus 1 for the point character
            total += rootBD.toPlainString().take(p+1).sumOf {
                // equivalent to (it - '0').coerceAtLeast(0)
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
     * N.B. If the BigDecimal can fit into a Long/Double value, the 1st guess is brought as close
     * as possible to the true sqrt by casting & getting the sqrt of the latter, as it will
     * require fewer iterations due to simply being of lower precision.
     *
     * @throws ArithmeticException if [this] is < 0.
     */
    private fun BigDecimal.sqrtManual(mc: MathContext): BigDecimal {
        if (this < BigDecimal.ZERO) throw ArithmeticException("BigDecimal must be 0 or positive")
        if (this == BigDecimal.ZERO) return BigDecimal.ZERO
        var guess = if (this.precision() > 18) {
            this.divide(2.toBigDecimal(), mc)
        } else {
            sqrt(this.toDouble()).toBigDecimal(mc)
        }
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