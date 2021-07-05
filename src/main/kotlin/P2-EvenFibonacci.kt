import util.RollingQueue

/**
 * Problem 2: Even Fibonacci Numbers
 * Goal: Find the sum of all even numbers in the Fibonacci sequence
 * that do not exceed N, with 10 <= N <= 4x10^16.
 * e.g. Even Fibonacci < N = 10 is {2, 8} with sum = 10.
 */

class EvenFibonacci {
    fun sumOfFibonacciFindEvens(max: Long): Long {
        if (max <= 2) return 0L
        val allFibonacci = allFibonacci(max)
        var sum = 0L
        for (i in 3..allFibonacci.lastIndex step 3) {
            sum += allFibonacci[i]
        }
        return sum
    }

    private fun allFibonacci(max: Long): List<Long> {
        var index = 2
        val fibonacci = mutableListOf<Long>().apply { addAll(listOf(0L, 1L)) }
        while (true) {
            val nextFib = fibonacci[index - 1] + fibonacci[index - 2]
            if (nextFib < max) {
                fibonacci.add(index++, nextFib)
            } else break
        }
        return fibonacci
    }

    // Will return 0 if list is empty
    fun sumOfEvenFibonacci(max: Long): Long {
        return evenFibonacci(max).sum()
    }

    // Even fibonacci every 3 steps: F(n) = 4*F(n-3) + F(n-6)
    private fun evenFibonacci(max: Long): List<Long> {
        var index = 0
        val fibonacci = mutableListOf<Long>()
        while (true) {
            val nextFib = when (index) {
                0 -> 2L // F(3)
                1 -> 8L // F(6)
                else -> 4 * fibonacci[index - 1] + fibonacci[index - 2]
            }
            if (nextFib < max) {
                fibonacci.add(index++, nextFib)
            } else break
        }
        return fibonacci
    }

    /**
     * Uses class RollingQueue to reduce memory of stored fibonnaci
     * numbers as they are only needed to accumulate sum.
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

    // Avoid the need to check for even numbers for stepping forward to next even
    fun evenFibonacciFourVariables(max: Long): Long {
        var sumOfEvens = 0L
        var prev2 = 1L // not used?
        var prev1 = 1L
        var nextFib = 2L
        while (nextFib < max) {
            sumOfEvens += nextFib
            prev2 = prev1 + nextFib
            prev1 = prev2 + nextFib
            nextFib = prev2 + prev1
        }
        return sumOfEvens
    }
}

