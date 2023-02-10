package dev.bogwalk.batch2

import dev.bogwalk.util.maths.sumProperDivisors

/**
 * Problem 21: Amicable Numbers
 *
 * https://projecteuler.net/problem=21
 *
 * Goal: Return the sum of all amicable numbers less than N.
 *
 * Constraints: 1 <= N <= 1e5
 *
 * Proper Divisor: A number x that evenly divides N, where x != N.
 *
 * Amicable Number: A number X that has a pair Y, where X != Y but
 * d(X) = Y && d(Y) = X, with d(N) = sum{proper divisors of N}.
 * First amicable pairs = {(220, 284), (1184, 1210), (2620, 2924)}
 *
 * e.g.: N = 300
 *       amicable pairs = [{220, 284}]; since
 *       d(220) = sum{1,2,4,5,10,11,20,22,44,55,110} = 284
 *       d(284) = sum{1,2,4,71,142} = 220
 *       sum = 220 + 284 = 504
 */

class AmicableNumbers {
    /**
     * Sums amicable numbers < [n] even if larger pair-member is >= [n].
     */
    fun sumAmicablePairs(n: Int): Int {
        val amicableNums = mutableListOf<Int>()
        for (x in 2 until n) {
            val y = sumProperDivisors(x)
            // the partner of a newly explored amicable number must be larger
            if (y > x && sumProperDivisors(y) == x) {
                amicableNums.add(x)
                // account for possibility that only 1 of the amicable pair may be under N
                if (y < n) {
                    amicableNums.add(y)
                } else {
                    // future pairs will be > N
                    break
                }
            }
        }
        return amicableNums.sum()
    }
}