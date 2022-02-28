package batch5

import util.maths.isPrimeMRBI

/**
 * Problem 58: Spiral Primes
 *
 * https://projecteuler.net/problem=58
 *
 * Goal: By repeatedly completing a new layer on a square spiral grid, as detailed below, find
 * the side length at which the ratio of primes along both diagonals first falls below N%.
 *
 * Constraints: 8 <= N <= 60
 *
 * Spiral Pattern: Start with 1 & move to the right in an anti-clockwise direction, incrementing
 * the numbers. This creates a grid with all odd squares (area of odd-sided grid & therefore
 * composite) along the bottom right diagonal.
 *
 * e.g.: N = N = 60
 *       grid = *17*  16  15  14  *13*
 *               18  *5*  4  *3*   12
 *               19   6   1   2    11
 *               20  *7*  8   9    10
 *               21   22  23  24   25
 *       side length = 5, as 5/9 = 55.55% are prime
 */

class SpiralPrimes {
    /**
     * Generates new diagonal values (except bottom-right) using previous square spiral side
     * length & checks their primality.
     *
     * A newer Miller-Rabin algorithm version is used to check primality, especially meant for very
     * large values. The diagonal values in this problem reach very high as the layers increase.
     *
     * e.g. side length 26241, at which point the ratio only just falls below 10%, has a
     * bottom-right diagonal value of 688_590_081.
     *
     * @param [percent] integer value representing the percentage under which the ratio should
     * first fall before returning the final result.
     */
    fun spiralPrimeRatio(percent: Int): Int {
        val ratio = percent / 100.0
        var side = 3
        var diagonals = 5
        var primes = 3L
        while (ratio <= 1.0 * primes / diagonals) {
            // check new diagonal elements of next layer using previous side length
            val prevArea = 1L * side * side
            primes += (1..3).sumOf { i ->
                if ((prevArea + i * (side + 1)).isPrimeMRBI()) 1L else 0L
            }
            side += 2
            diagonals += 4
        }
        return side
    }
}