package batch2

import util.getPrimeFactors
import util.getPrimesUsingSieve

/**
 * Problem 12: Highly Divisible Triangular Number
 * Goal: Find the value of the first triangle number to have more
 * than N divisors, with 1 <= N <= 10^3.
 * A triangle number is generated by adding all natural numbers
 * prior to & including it, e.g. the 3rd is 6 = 1+2+3.
 * The first 10 triangle numbers are: 1, 3, 6, 10, 15, 21, 28, 36, 45, 55.
 */

class HighlyDivisibleTriangularNumber {
    private fun Int.gaussSum(): Int = this * (this + 1) / 2

    /**
     * e.g. 28 = 2^2 * 7^1 (prime factors)
     * Therefore, num of divisors of 28 = (2 + 1) * (7 + 1)
     */
    fun countDivisors(n: Int): Int {
        val primeFactors = getPrimeFactors(1L * n)
        return primeFactors.values
            .map { it + 1 }
            .reduce { acc, v -> acc * v }
    }

    fun firstTrianglesBounded(n: Int): IntArray {
        val triangles = IntArray(n + 1).apply { this[0] = 1 }
        var lastT = 2
        var lastCount = 2
        array@ for (i in 1..n) {
            if (i < lastCount) {
                triangles[i] = lastT.gaussSum()
                continue@array
            }
            var nextT = lastT + 1
            tLoop@ do {
                val triangle = nextT.gaussSum()
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
     * Since the components of the gaussian sum are co-prime (cannot have a
     * common prime factor and no common divisor, the amount of divisors (D) can
     * be gotten from :
     * [if n is even] D(t) = D(n/2) * D(n+1) OR
     * [if (n+1) is even] D(t) = D(n) * D((n+1)/2)
     * The second part of the 1st equation will carry over to the next.
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
                val triangle = nextT.gaussSum()
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
        return t.gaussSum()
    }

    fun firstTriangleOverNImproved(n: Int): Int {
        if (n == 1) return 3
        val primes = getPrimesUsingSieve(n * 2)
        var prime = 3
        var dn = 2 // min num of divisors for any prime
        var count = 0
        while (count <= n) {
            prime++
            var n1 = prime
            if (n1 % 2 == 0) n1 /= 2
            var dn1 = 1
            for (i in primes.indices) {
                //When the prime divisor would be greater than the residual n1
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