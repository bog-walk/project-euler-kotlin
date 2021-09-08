package batch3

import kotlin.math.sqrt

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
    /**
     * This solution is optimised based on the following:
     * N == 1 has no proper divisor but 1 is a proper divisor of all other naturals;
     * A perfect square would duplicate divisors if included in the loop range;
     * Loop range differs for odd numbers as they cannot have even divisors.
     */
    fun sumProperDivisors(num: Int): Int {
        if (num < 2) return 0
        var sum = 1
        var maxDivisor = sqrt(1.0 * num).toInt()
        if (maxDivisor * maxDivisor == num) {
            sum += maxDivisor
        }
        val range = if (num % 2 != 0) (3..maxDivisor step 2) else (2..maxDivisor)
        for (d in range) {
            if (num % d == 0) {
                sum += d + num / d
            }
        }
        return sum
    }

    fun sumProperDivisorsPrimeFactors(num: Int): Int {
        if (num < 2) return 0
        var n = num
        var sum = 1
        var p = 2
        while (p * p <= num && n > 1) {
            if (n % p == 0) {
                var j = p * p
                n /= p
                while (n % p == 0) {
                    j *= p
                    n /= p
                }
                sum *= (j - 1)
                sum /= (p - 1)
            }
            if (p == 2) {
                p++
            } else {
                p += 2
            }
        }
        if (n > 1) {
            sum *= (n + 1)
        }
        return sum - num
    }

    fun sumAmicablePairs(num: Int): Int {
        val amicables = mutableListOf<Int>()
        for (x in 2 until num) {
            val y = sumProperDivisors(x)
            // The partner of a newly explored amicable number must be larger
            if (y > x && sumProperDivisors(y) == x) {
                amicables.add(x)
                // Account for possibility that only 1 of
                // the amicable pair may be under N
                if (y < num) {
                    amicables.add(y)
                } else {
                    break
                }
            }
        }
        return amicables.sum()
    }
}