package dev.bogwalk.batch9

import java.math.BigInteger
import kotlin.math.sqrt

/**
 * Problem 100: Arranged Probability
 *
 * https://projecteuler.net/problem=100
 *
 * Goal: Find the first arrangement (of blue and red discs) to contain > D total discs, where the
 * probability of taking 2 blue discs is exactly P/Q. Output the number of blue discs followed by
 * the total amount of discs.
 *
 * Constraints: 2 <= D <= 1e15, 0 < P < Q <= 1e7
 *
 * e.g. A box has 21 total discs, of which 15 are blue and 6 are red. The probability of taking 2
 * blue discs at random is P(BB) = 15/21 * 14/20 = 1/2. The next arrangement to also have exactly
 * 50% chance involves a box with 120 total discs, of which 85 must be blue.
 *
 * e.g.: D = 2, P/Q = 1/2
 *       output = 3 4
 */

class ArrangedProbability {
    /**
     * Solution based on the equation:
     *
     *      X^2 - X = p * (b^2 - b), where p = probability of picking 2 blue discs
     *
     * If a value of X is assumed (& valid), the value of b is found by solving for the positive
     * integer solution of the quadratic equation:
     *
     *      0 = b^2 - b - ((X^2 - X) / p)
     *
     * A quadratic solution is solved using the formula:
     *
     *      (-b +/- sqrt(b^2 - 4ac)) / 2a, with a = 1, b = -1, and c = -(X^2 - X) / p
     *
     * Since the value of a and b never change & only the positive integer solution is required &
     * c is always negative, the formula becomes:
     *
     *      (1 + sqrt(4c + 1)) / 2
     *
     * This means that 2 requirements are needed for a positive solution to be possible:
     *  - 4c must be a whole positive integer to be a square number when incremented.
     *  - the square root must be odd to be divisible by 2 when incremented.
     *
     * Brute force of the solutions also brought to light that `totalDiscs` switched between even
     * and odd values, with the first arrangement always having an even total, but this is only
     * useful if generating all arrangements. Brute force also shows that some fractions (e.g.
     * 1/2, 3/4, 11/12) can be easily found using the following:
     *
     *      given initial values determined using the equations above:
     *      red_{n+1} = 2 * blue_n + red_n - 1
     *      blue_{n+1} = blue_n + (pNum + pDenom - 1) * red_{n+1}
     *
     * Certain fractions deviate from this norm (e.g. 3/8, 2/5) by toggling between 2 distinct
     * fractions to use in the multiplication, e.g.:
     *
     *      given p = 3/8
     *      red_{n+1} = 2 * blue_n + red_n - 1
     *      blue_{n+1} = blue_n + (6/5 OR ~189/125) * red_{n+1}
     */
    fun getNextArrangement(limit: Long, probNum: Int, probDenom: Int): Pair<String, String>? {
        val (p, q) = probNum.toBigInteger() to probDenom.toBigInteger()
        val (zero, one, two, four) = listOf(BigInteger.ZERO, BigInteger.ONE, BigInteger.TWO, 4.toBigInteger())
        var blueDiscs = zero
        val maxTotal = BigInteger.valueOf(Long.MAX_VALUE)
        var totalDiscs = (limit + 1).toBigInteger()

        while (totalDiscs < maxTotal) {
            // divideAndRemainder() returns quotient and remainder as array
            // is this necessary? BI don't seem to ever leave a remainder?
            val (c, cRem) = (totalDiscs * (totalDiscs - one) * p).divideAndRemainder(q)
            // must have integer value
            if (cRem == zero) {
                // sqrtAndRemainder() returns integer root and remainder as array
                val (root, rRem) = (four * c + one).sqrtAndRemainder()
                // must have integer root, which must be odd
                if (rRem == zero && root.mod(two) == one) {
                    blueDiscs = (root + one) / two
                    break
                }
            }
            totalDiscs++
        }

        return if (blueDiscs == zero) null else {
            blueDiscs.toString() to totalDiscs.toString()
        }
    }

    /**
     * Original PE problem (p = 1/2) results in a known integer sequence of `blueDiscs` based on
     * the solution to X(X-1) = 2b(b-1), such that:
     *
     *      when p = 1/2 -> b(n) = 6b(n-1) - b(n-2) - 2, with b(0) = 1, b(1) = 3
     *
     * Some other fractions can be observed to follow a similar pattern:
     *
     *      when p = 3/4 -> b(n) = 14b(n-1) - b(n-2) - 6
     *      when p = 5/7 -> b(n) = 12b(n-1) - b(n-2) - 5
     *
     * @see <a href="https://oeis.org/A011900">Sequence</a>
     */
    fun getNextHalfArrangement(limit: Long): Pair<String, String> {
        if (limit <= 3L) return "3" to "4"
        var blueNMinus2 = 1L
        var blueNMinus1 = 3L
        var blueDiscs: Long
        var totalDiscs: Long

        do {
            blueDiscs = 6 * blueNMinus1 - blueNMinus2 - 2
            val rhs = 2 * blueDiscs * (blueDiscs - 1)
            val root = sqrt(1.0 + 4 * rhs).toLong()
            totalDiscs = (1 + root) / 2
            blueNMinus2 = blueNMinus1
            blueNMinus1 = blueDiscs
        } while (totalDiscs <= limit)

        return blueDiscs.toString() to totalDiscs.toString()
    }
}