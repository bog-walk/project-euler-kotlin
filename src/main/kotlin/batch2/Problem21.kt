package batch2

import util.sumProperDivisorsOG

/**
 * Problem 21: Amicable Numbers
 *
 * https://projecteuler.net/problem=21
 *
 * Goal: Return the sum of all amicable numbers less than N.
 *
 * Constraints: 1 <= N <= 1e5
 *
 * Proper Divisor: A number x that evenly divides N, where x !=N.
 *
 * Amicable Number: A number X that has a pair Y, where X != Y but
 * d(X) = Y && d(Y) = X, with d(N) = sum{proper divisors of N}.
 *
 * e.g.: N = 300
 *       amicable pairs = [{220, 284}]; since
 *       d(220) = sum{1,2,4,5,10,11,20,22,44,55,110} = 284
 *       d(284) = sum{1,2,4,71,142} = 220
 *       sum = 220 + 284 = 504
 */

class AmicableNumbers {
    fun sumAmicablePairs(num: Int): Int {
        val amicableNums = mutableListOf<Int>()
        for (x in 2 until num) {
            val y = sumProperDivisorsOG(x)
            // The partner of a newly explored amicable number must be larger
            if (y > x && sumProperDivisorsOG(y) == x) {
                amicableNums.add(x)
                // Account for possibility that only 1 of
                // the amicable pair may be under N
                if (y < num) {
                    amicableNums.add(y)
                } else {
                    break
                }
            }
        }
        return amicableNums.sum()
    }
}