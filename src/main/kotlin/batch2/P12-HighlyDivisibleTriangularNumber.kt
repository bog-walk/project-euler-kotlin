package batch2

import util.getPrimeFactors

/**
 * Problem 12: Highly Divisible Triangular Number
 * Goal: Find the value of the first triangle number to have more
 * than N divisors, with 1 <= N <= 10^3.
 * A triangle number is generated by adding all natural numbers
 * prior to & including it, e.g. the 3rd is 6 = 1+2+3.
 * The first 10 triangle numbers are: 1, 3, 6, 10, 15, 21, 28, 36, 45, 55.
 * Tests: > 1 = 3 {1,3}; > 2 = 6 {1,2,3,6};
 * > 3 = 6 {1,2,3,6}; > 4 = 28 {1,2,4,7,14,28}
 */

class HighlyDivisibleTriangularNumber {
    private fun Int.gaussSum(): Int = this * (this + 1) / 2

    fun countDivisors(n: Int): Int {
        val primeFactors = getPrimeFactors(1L * n)
        return primeFactors.values
            .map { it + 1 }
            .reduce { acc, v -> acc * v }
    }

    fun firstTrianglesBounded(n: Int): IntArray {
        val triangles = IntArray(n + 1).apply { this[0] = 1 }
        var lastT = 2
        for (i in 1..n) {
            var nextT = lastT
            do {
                val triangle = nextT.gaussSum()
                if (countDivisors(triangle) > i) {
                    triangles[i] = triangle
                    lastT = nextT
                    break
                }
                nextT++
            } while (true)
        }
        return triangles
    }
}

fun main() {
    val tool = HighlyDivisibleTriangularNumber()
    val triangles = tool.firstTrianglesBounded(1000)
    println(triangles.sliceArray(990..1000).contentToString())
}