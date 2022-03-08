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
     * SPEED (WORST): 32.16ms for N = 4e16
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
     * Sums every 3rd term in the sequence starting with 2, based on the observed pattern that
     * every 3rd Fibonacci number after 2 is even. This occurs because the sequence begins with 2
     * odd numbers, the sum of which must be even, then the sum of an odd and even number, twice,
     * will produce 2 odd numbers, etc...
     *
     * SPEED (BETTER) 82700ns for N = 4e16
     */
    fun sumOfEvenFibsBrute(n: Long): Long {
        var sum = 0L
        var prev1 = 1L
        var nextFib = 2L
        while (nextFib < n) {
            sum += nextFib
            val prev2 = prev1 + nextFib
            prev1 = prev2 + nextFib
            nextFib = prev2 + prev1
        }
        return sum
    }

    /**
     * Sums every 3rd term in the sequence starting with 2, using the formula:
     *
     *      F(n) = 4F(n - 3) + F(n - 6)
     *
     * SPEED (BEST) 25400ns for N = 4e16
     */
    fun sumOfEvenFibsFormula(n: Long): Long {
        var sum = 10L
        val evenFibs = longArrayOf(2, 8) // F(3), F(6)
        while (true) {
            val nextEvenFib = 4 * evenFibs[1] + evenFibs[0]
            if (nextEvenFib >= n) break
            sum += nextEvenFib
            evenFibs[0] = evenFibs[1]
            evenFibs[1] = nextEvenFib
        }
        return sum
    }
}

