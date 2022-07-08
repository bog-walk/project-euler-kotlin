package dev.bogwalk.batch6

/**
 * Problem 65: Convergents Of E
 *
 * https://projecteuler.net/problem=65
 *
 * Goal: Find the sum of the digits in the numerator of the Nth convergent of the continued
 * fraction of Euler's number, the mathematical constant e.
 *
 * Constraints: 1 <= N <= 3e4
 *
 * As seen in Problems 57 and 64, the infinite continued fraction of sqrt(2) can be written as [1;
 * (2)], indicating that 2 repeats ad infinitum. The 1st 10 convergents of sqrt(2) are ->
 * 1, 3/2, 7/5, 17/12, 41/29, 99/70, 239/169, 577/408, 1393/985, 3363/2378, ...
 *
 * Euler's number, e, however can be written as:
 *
 *      [2;1,2,1,1,4,1,1,6,1,...,1,2n,1,...], with its 1st 10 convergents being ->
 *
 *      2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, ...
 *
 *
 * e.g.: N = 10
 *       10th convergent of e = 1457/536
 *       sum = 1 + 4 + 5 + 7 = 17
 */

class ConvergentsOfE {
    /**
     * Solution based on the pattern observed for the numerator in the continued fraction
     * representation:
     *
     *      if n_1_1 = 2, n_1_2 = 3, n_1_3 = 8, with m_1_1 = 1, m_1_2 = 1, m_1_3 = 2, then
     *
     *      8 = 2 + 2 * 3, which continues for the sequences as ->
     *
     *      n_i_j = n_i_j-2 + m_i_j * n_i_j-1
     */
    fun nthConvergentDigitSum(n: Int): Int {
        // represents [nI, nIMinus2, nIMinus1]
        val numerators = arrayOf(0.toBigInteger(), 2.toBigInteger(), 3.toBigInteger())
        for (i in 1 until n / 3 + 1) {
            numerators[0] = 2.toBigInteger() * i.toBigInteger() * numerators[2] + numerators[1]
            numerators[1] = numerators[0] + numerators[2]
            numerators[2] = numerators[0] + numerators[1]
        }
        return numerators[n % 3].toString().sumOf(Char::digitToInt)
    }
}