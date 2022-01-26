package batch0

import util.RollingQueue

/**
 * Problem 2: Even Fibonacci Numbers
 *
 * https://projecteuler.net/problem=2
 *
 * Goal: Find the sum of all even numbers in the Fibonacci sequence
 * less than N.
 *
 * Constraints: 10 <= N <= 4e16
 *
 * e.g.: N = 44
 *       even fibonacci < N = {2, 8, 34}
 *       sum = 44
 */

class EvenFibonacci {
    /**
     * Generates a list of all fibonacci numbers then sum all evens, based
     * on every third number in sequence being even.
     *
     * SPEED (WORST): 5.9e6ns for N = 4e16
     */
    fun sumOfFibonacciBrute(max: Long): Long {
        if (max < 3) return 0L
        val allFibonacci = allFibonacci(max)
        var sum = 0L
        for (i in 3..allFibonacci.lastIndex step 3) {
            sum += allFibonacci[i]
        }
        return sum
    }

    private fun allFibonacci(max: Long): List<Long> {
        var index = 2
        val fibonacci = mutableListOf(0L, 1L)
        while (true) {
            val nextFib = fibonacci[index - 1] + fibonacci[index - 2]
            if (nextFib < max) {
                fibonacci.add(index++, nextFib)
            } else break
        }
        return fibonacci
    }

    /**
     * Generates a list of only even fibonacci numbers based on the formula
     * F(n) = 4*F(n-3) + F(n-6), then sum resulting list.
     *
     * SPEED (SECOND BEST): 2.5e6ns for N = 4e16
     */
    fun sumOfEvenFibonacci(max: Long): Long {
        return evenFibonacci(max).sum()
    }

    private fun evenFibonacci(max: Long): List<Long> {
        var index = 2
        val fibonacci = mutableListOf(2L, 8L) // F(3), F(6)
        while (true) {
            val nextFib = 4 * fibonacci[index - 1] + fibonacci[index - 2]
            if (nextFib < max) {
                fibonacci.add(index++, nextFib)
            } else break
        }
        return fibonacci
    }

    /**
     * Uses custom class RollingQueue to reduce memory of stored fibonacci
     * numbers as only the 2 prior are needed to accumulate sum.
     *
     * SPEED (THIRD BEST): 5.1e6ns for N = 4e16
     */
    fun evenFibonacciRollingSum(max: Long): Long {
        var sumOfEvens = 0L
        val fibonacci = RollingQueue<Long>(2).apply { addAll(listOf(1L, 1L)) }
        while (true) {
            val nextFib = fibonacci.peek() + fibonacci.peekTail()!!
            if (nextFib < max) {
                fibonacci.add(nextFib)
                if (nextFib % 2 == 0L) sumOfEvens += nextFib
            } else break
        }
        return sumOfEvens
    }

    /**
     * Similar to RollingQueue implementation in that memory is reduced
     * by only using 4 variables, but also avoids the need to check
     * for even numbers by stepping forward to next predictable even in sequence.
     *
     * SPEED (BEST): 3.7e4ns for N = 4e16
     */
    fun evenFibonacciFourVariables(max: Long): Long {
        var sumOfEvens = 0L
        var prev1 = 1L
        var nextFib = 2L
        while (nextFib < max) {
            sumOfEvens += nextFib
            val prev2 = prev1 + nextFib
            prev1 = prev2 + nextFib
            nextFib = prev2 + prev1
        }
        return sumOfEvens
    }
}

