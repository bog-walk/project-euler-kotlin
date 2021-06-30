/**
 * Problem 2: Even Fibonacci Numbers
 * Goal: Find the sum of all even numbers in the Fibonacci sequence
 * that do not exceed the provided N.
 * e.g. Even Fibonacci < N = 10 is {2, 8} with sum = 10.
 */

class EvenFibonacci {
    fun sumOfEvenFibonacci(n: Int): Int {

    }

    private fun evenFibonacci(n: Int): List<Int> {
        if (n == 0) return listOf(0)
        val fibonacci = mutableListOf<Int>().apply { listOf(0, 1) }
        while (true) {
            fibonacci.addAll(
                evenFibonacci(fibonacci.last()) +
                        evenFibonacci(fibonacci[fibonacci.lastIndex - 1])
            )
            if (fibonacci.last() >= n) break
        }
    }
}

