package batch0

import util.custom.RollingQueue

/**
 * Problem 2: Even Fibonacci Numbers
 *
 * https://projecteuler.net/problem=2
 *
 * Goal: Find the sum of all even numbers in the Fibonacci sequence less than N.
 *
 * Constraints: 10 <= N <= 4e16
 *
 * e.g.: N = 44
 *       even fibonacci < N = {2, 8, 34}
 *       sum = 44
 */

class EvenFibonacci {
    /**
     * Uses custom class RollingQueue to reduce memory of stored fibonacci numbers as only the 2
     * prior are needed to accumulate sum. Provides an easier alternative to using an array or
     * multiple variables as it handles value reassignment & swapping within the class.
     *
     * SPEED (WORST): 6.3e+04ns for N = 4e16
     */
    fun sumOfEvenFibsRolling(n: Long): Long {
        var sum = 0L
        val fibonacci = RollingQueue<Long>(2).apply { addAll(listOf(1L, 1L)) }
        while (true) {
            val nextFib = fibonacci.peek() + fibonacci.peekTail()!!
            if (nextFib >= n) break
            fibonacci.add(nextFib)
            if (nextFib % 2 == 0L) sum += nextFib
        }
        return sum
    }

    /**
     * Brute iteration over all Fibonacci terms using the formula:
     *
     *      F_n = F_{n-2} + F_{n-1}
     *
     * SPEED (BETTER): 1867ns for N = 4e16
     */
    fun sumOfEvenFibsNaive(n: Long): Long {
        var sum = 0L
        var prev2 = 1L
        var prev1 = 2L
        while (prev1 < n) {
            if (prev1 and 1 == 0L) sum += prev1
            val nextFib = prev1 + prev2
            prev2 = prev1
            prev1 = nextFib
        }
        return sum
    }

    /**
     * Sums every 3rd term in the sequence starting with 2, based on the observed pattern that
     * every 3rd Fibonacci number after 2 is even. This occurs because the sequence begins with 2
     * odd numbers, the sum of which must be even, then the sum of an odd and even number, twice,
     * will produce 2 odd numbers, etc...
     *
     * SPEED (BETTER) 1848ns for N = 4e16
     */
    fun sumOfEvenFibsBrute(n: Long): Long {
        var sum = 0L
        var prev = 1L
        var evenFib = 2L
        while (evenFib < n) {
            sum += evenFib
            val next = prev + evenFib
            prev = next + evenFib
            evenFib = next + prev
        }
        return sum
    }

    /**
     * Sums every 3rd term in the sequence starting with 2, using the formula:
     *
     *      F_n = F_{n-6} + 4F_{n-3}
     *
     * SPEED (BEST) 1237ns for N = 4e16
     */
    fun sumOfEvenFibsFormula(n: Long): Long {
        var sum = 10L
        val evenFibs = longArrayOf(2, 8) // [F(3), F(6)]
        while (true) {
            val nextEvenFib = evenFibs[0] + 4 * evenFibs[1]
            if (nextEvenFib >= n) break
            sum += nextEvenFib
            evenFibs[0] = evenFibs[1]
            evenFibs[1] = nextEvenFib
        }
        return sum
    }
}