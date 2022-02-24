package batch5

import java.math.BigInteger

/**
 * Problem 57: Square Root Convergents
 *
 * https://projecteuler.net/problem=57
 *
 * Goal: Given N, in the 1st N expansions of the square root of 2's infinite continued fraction,
 * find the iteration numbers where the numerator has more digits than the denominator.
 *
 * Constraints: 8 <= N <= 1e4
 *
 * Square Root of 2: This can be expressed as an infinite continued fraction ->
 *      Iteration 1 -> sqrt(2) = 1 + 1/2 = 3/2 = 1.5
 *      Iteration 2 -> 1 + 1/(2 + 1/2) = 7/5 = 1.4
 *      Iteration 3 -> 1 + 1/(2 + (1/(2+1/2))) = 17/12 = 1.41666...
 *      Iteration 4 -> 1 + 1/(2 + (1/(2 + 1/(2 + 1/2)))) = 41/29 = 1.41379...
 *
 * The next 4 expansions are 99/70, 239/169, 577/408, 1393/985. The latter 8th
 * expansion is the 1st expansion where the number of digits in the numerator exceed
 * that of the denominator.
 *
 * e.g.: N = 14
 *       iterations = [8, 13]
 *       expansions are: 1393 / 985 and 114243 / 80782
 */

class SquareRootConvergents {
    /**
     * The infinite continued fractional part is repeatedly calculated by adding its previous
     * value to 2 (= 4/2) then dividing 1 by the result, simply by swapping the numerator and
     * denominator. The result is then added to 1.
     *
     *      e.g. 3rd iteration, with 2nd iteration fractional part = 2/5:
     *           1 + (1 / (4/2 + 2/5)) = 1 + (1 / (12/5))
     *           fractional part becomes 5/12
     *           expansion = 1 + 5/12 = 17/12
     *
     * Consider comparing number of digits by calling log10() instead of casting to string.
     * Remember that log10(10) = 1.0 because 10^1 = 10 and every 2-digit number will be a
     * fraction between 1 and 2. Consider implementing a helper to do so for BigInteger.
     *
     * SPEED (WORSE) 1.18s for N = 2e3
     *
     * @return list of integers representing the iteration where number of digits in the
     * numerator exceeds number of digits in the denominator.
     */
    fun squareRootFractionsManual(n: Int): List<Int> {
        val iterations = mutableListOf<Int>()
        // 1st iteration fractional part
        var infFraction = 1.toBigInteger() to 2.toBigInteger()
        for (i in 2..n) {
            // continued fraction of 1 / (2 + previous fraction)
            infFraction = addFractions(
                4.toBigInteger(), 2.toBigInteger(), infFraction.first, infFraction.second
            ).reversed()
            val fraction = addFractions(
                BigInteger.ONE, BigInteger.ONE, infFraction.first, infFraction.second
            )
            if (
                fraction.first.toString().length > fraction.second.toString().length
            ) {
                iterations.add(i)
            }
        }
        return iterations
    }

    private fun Pair<BigInteger, BigInteger>.reversed() = second to first

    /**
     * Add 2 fractions manually and return in a reduced form.
     *
     * @return pair of (numerator, denominator).
     */
    private fun addFractions(
        n1: BigInteger,
        d1: BigInteger,
        n2: BigInteger,
        d2: BigInteger
    ): Pair<BigInteger, BigInteger> {
        val denominator = d1 * d2 / d1.gcd(d2)
        val numerator = denominator / d1 * n1 + denominator / d2 * n2
        val divisor = numerator.gcd(denominator)
        return numerator / divisor to denominator / divisor
    }

    /**
     * Solution optimised based on the following:
     *
     *  -   Further reducing the above formula:
     *      if a_0 = 1 + 1/2, a_1 = 1 + 1/(2 + 1/2)
     *      then a_(i+1) = 1 + 1/(1 + a_i)
     *
     *      if a_i = n_i / d_i is inserted, the formula becomes:
     *
     *      a_(i+1) = (2d_i + n_i) / (d_i + n_i)
     *
     *  -   Storing the ceiling for current number of digits, i.e. 10 for 1-digit numbers, 100
     *  for 2-digit numbers, etc. This is compared instead of repeatedly calling log10().
     *
     * SPEED (BETTER) 3.73ms for N = 2e3
     *
     * @return list of integers representing the iteration where number of digits in the
     * numerator exceeds number of digits in the denominator.
     */
    fun squareRootFractions(n: Int): List<Int> {
        val iterations = mutableListOf<Int>()
        // 1st iteration expansion
        var infN = 3.toBigInteger()
        var infD = 2.toBigInteger()
        // both start as 1-digit numbers
        var nCeil = BigInteger.TEN
        var dCeil = BigInteger.TEN
        for (i in 2..n) {
            infN += BigInteger.TWO * infD.also {
                // uses previous infN value, not the newly assigned one
                infD += infN
            }
            if (infN >= nCeil) nCeil *= BigInteger.TEN
            if (infD >= dCeil) dCeil *= BigInteger.TEN
            if (nCeil > dCeil) iterations.add(i)
        }
        return iterations
    }
}