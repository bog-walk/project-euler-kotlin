/**
 * Problem 2: Even Fibonacci Numbers
 * Goal: Find the sum of all even numbers in the Fibonacci sequence
 * that do not exceed the provided N.
 * e.g. Even Fibonacci < N = 10 is {2, 8} with sum = 10.
 */

class EvenFibonacci {
    fun sumOfEvenFibonacci(max: Long): Long {
        if (max <= 2) return 0L
        val allFibonacci = evenFibonacci(max)
        var sum = 0L
        for (i in 3..allFibonacci.lastIndex step 3) {
            sum += allFibonacci[i]
        }
        return sum
    }

    private fun evenFibonacci(max: Long): List<Long> {
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
}

