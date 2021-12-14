package batch4

import java.math.BigInteger
import kotlin.math.pow

/**
 * Problem 40: Champernowne's Constant
 *
 * https://projecteuler.net/problem=40
 *
 * Goal: Calculate the value of the expression d_i1 * d_i2 * .. d_i7
 * if d_in represents the nth digit of the fractional part of Champernowne's Constant.
 *
 * Constraints: 1 <= i1..i7 <= 1e18
 *
 * Champernowne's Constant: The irrational decimal fraction created by concatenating
 * all positive integers to the right of a decimal point, such that:
 * C = 0.123456789101112131415161718192021...
 *
 * e.g.: i1..i7 = 1 2 3 4 5 6 7
 *       expression = 1 * 2 * 3 * 4 * 5 * 6 * 7
 *       result = 5040
 */

class ChampernownesConstant {
    // Increase efficiency by pre-computing amount of digits in all series
    private val kDigits = Array(19) { k ->
        BigInteger.TEN.pow(k) * (k + 1).toBigInteger() * 9.toBigInteger()
    }

    /**
     * Assuming that the positive integers of Champernowne's Constant are separated
     * into series representing the number of digits of each successive integer, k, and
     * the range of each series is calculated by 10^(k-1)..(10^k)-1, then each series
     * will have 9 * 10^(k-1) terms and, thereby, a total of k * 9 * 10^(k-1) digits.
     * The cumulative total digits per series can be used to determine in which series
     * range the requested index sits as well as its position in the series.
     *
     * e.g. Searching for the 2000th digit in Champernowne's Constant (C):
     * - 2000 - 180 - 9 = 1811 < 2700, therefore
     * - It is found in the 1811th digit in series 3.
     * - 1811 is first zero-indexed (as C does not start with 0), then
     * - As series 3 has 3 digits in every term, 1810 // 3 gives the location of the
     * 1810th digit as being in the 603rd term -> 100 + 603 = 703.
     * - 1810 % 3 = 1, so the 1810th digit is at index 1 of 703, therefore
     * - The 2000th digit in C is 0.
     */
    fun getConstant(index: Long): Int {
        var k = 1
        var i = index.toBigInteger()
        while (i > kDigits[k - 1]) {
            // final reduced i represents the ith digit in the kth series
            i -= kDigits[k - 1]
            k++
        }
        i -= BigInteger.ONE
        // kTerm represents the ordinal position in the series
        // termI represents the index of the digit within the term found below
        val (kTerm, termI) = i.divideAndRemainder(k.toBigInteger())
        val term = BigInteger.TEN.pow(k - 1) + kTerm
        return term.toString()[termI.intValueExact()].digitToInt()
    }

    fun champernownesProduct(digits: List<Long>): Int {
        return digits.map { i ->
            getConstant(i)
        }.fold(1) { acc, c ->
            acc * c
        }
    }
}