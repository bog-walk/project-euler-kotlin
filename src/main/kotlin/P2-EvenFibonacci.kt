import kotlin.streams.toList

/**
 * Problem 2: Even Fibonacci Numbers
 * Goal: Find the sum of all even numbers in the Fibonacci sequence
 * that do not exceed the provided N.
 * e.g. Even Fibonacci < N = 10 is {2, 8} with sum = 10.
 */

class EvenFibonacci {
    fun sumOfEvenFibonacci(max: Int): Int {
        if (max <= 2) return 0
        val allFibonacci = evenFibonacci(max)
        var sum = 0
        for (i in 2..allFibonacci.lastIndex step 3) {
            sum += allFibonacci[i]
        }
        return sum
    }

    private fun evenFibonacci(max: Int): List<Int> {
        var index = 2
        val fibonacci = mutableListOf<Int>().apply { addAll(listOf(0, 1)) }
        while (true) {
            val nextFib = fibonacci[index - 1] + fibonacci[index - 2]
            if (nextFib < max) {
                fibonacci.add(index++, nextFib)
            } else break
        }
        return fibonacci
    }
}

