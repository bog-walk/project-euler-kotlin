package batch1

import util.maths.gaussianSum
import util.maths.primeFactors
import util.maths.primeNumbersOG

/**
 * Problem 12: Highly Divisible Triangular Number
 *
 * https://projecteuler.net/problem=12
 *
 * Goal: Find the value of the first triangle number to have more
 * than N divisors.
 *
 * Constraints: 1 <= N <= 1e3
 *
 * Triangle Number: a number generated by adding all natural numbers
 * prior to & including itself.
 *
 * e.g.: N = 5
 *       1st = 1 from [1] -> {1]
 *       2nd = 3 from [1+2] -> {1,3}
 *       3rd = 6 from [1+2+3] -> {1,2,3,6}
 *       4th = 10 from [1+2+3+4] -> {1,2,5,10}
 *       5th = 15 from [1+2+3+4+5] -> {1,3,5,15}
 *       6th = 21 from [1+2+3+4+5+6] -> {1,3,7,21}
 *       7th = 28 from [1+2+3+4+5+6+7] -> {1,2,4,7,14,28}
 *       result = 28
 */

class HighlyDivisibleTriangularNumber {

    /**
     * Prime factor decomposition of 28 = 2^2 * 7^1.
     * Therefore, number of divisors of 28 = (2 + 1) * (7 + 1).
     */
    fun countDivisors(n: Int): Int {
        val primeFactors = primeFactors(1L * n)
        return primeFactors.values
            .map { it + 1 }
            .reduce { acc, v -> acc * v }
    }

    /**
     * Returns IntArray of first triangle number to have more than index divisors.
     *
     * SPEED: 7246ms for N = 1000.
     */
    fun firstTrianglesBounded(n: Int): IntArray {
        val triangles = IntArray(n + 1).apply { this[0] = 1 }
        var lastT = 2
        var lastCount = 2
        array@ for (i in 1..n) {
            if (i < lastCount) {
                triangles[i] = lastT.gaussianSum().toInt()
                continue@array
            }
            var nextT = lastT + 1
            tLoop@ do {
                val triangle = nextT.gaussianSum().toInt()
                val count = countDivisors(triangle)
                if (i < count) {
                    triangles[i] = triangle
                    lastT = nextT
                    lastCount = count
                    break@tLoop
                }
                nextT++
            } while (true)
        }
        return triangles
    }

    /**
     * Since the components of the gaussian sum are co-prime (cannot have either a
     * common prime factor or a common divisor), the amount of divisors (D) can
     * be gotten from :
     * if n is even -> D(t) = D(n/2) * D(n+1) OR
     * if (n+1) is even -> D(t) = D(n) * D((n+1)/2)
     * The second part of the 1st equation will carry over to the next.
     *
     * SPEED (BEST): 150ms for N = 1000.
     */
    fun firstTrianglesImproved(n: Int): IntArray {
        val triangles = IntArray(n + 1).apply {
            this[0] = 1
            this[1] = 3
            this[2] = 6
        }
        var lastT = 3 // D(3) = D(3) * D(2)
        var lastDn1 = 2
        var lastTriangle = 6
        var lastCount = 4
        for (i in 3..n) {
            if (i < lastCount) {
                triangles[i] = lastTriangle
                continue
            }
            var nextT = lastT + 1
            do {
                val triangle = nextT.gaussianSum().toInt()
                val dn2 = if (nextT % 2 == 0) countDivisors(nextT+1) else countDivisors((nextT+1)/2)
                val count = lastDn1 * dn2
                lastDn1 = dn2
                if (i < count) {
                    triangles[i] = triangle
                    lastTriangle = triangle
                    lastT = nextT
                    lastCount = count
                    break
                }
                nextT++
            } while (true)
        }
        return triangles
    }

    /**
     * Returns first triangle number with divisors over N.
     *
     * SPEED: 451ms for N = 1000.
     */
    fun firstTriangleOverN(n: Int): Int {
        if (n == 1) return 3
        var t = 2 // D(2) = D(1) * D(3)
        var dn1 = 2 // D(3) = 2
        var count = 2
        while (count <= n) {
            t++
            val dn2 = if (t % 2 == 0) countDivisors(t+1) else countDivisors((t+1)/2)
            count = dn1 * dn2
            dn1 = dn2
        }
        return t.gaussianSum().toInt()
    }

    /**
     * SPEED (BEST SINGLE-PICK): 46ms for N = 1000.
     */
    fun firstTriangleOverNImproved(n: Int): Int {
        if (n == 1) return 3
        val primes = primeNumbersOG(n * 2)
        var prime = 3
        var dn = 2 // min num of divisors for any prime
        var count = 0
        while (count <= n) {
            prime++
            var n1 = prime
            if (n1 % 2 == 0) n1 /= 2
            var dn1 = 1
            for (i in primes.indices) {
                // When the prime divisor would be greater than the residual n1
                // that residual n1 is the last prime factor with an exponent=1.
                // No need to identify it.
                if (primes[i] * primes[i] > n1) {
                    dn1 *= 2
                    break
                }
                var exponent = 1
                while (n1 % primes[i] == 0) {
                    exponent++
                    n1 /= primes[i]
                }
                if (exponent > 1) dn1 *= exponent
                if (n1 == 1) break
            }
            count = dn * dn1
            dn = dn1
        }
        return prime * (prime - 1) / 2
    }
}