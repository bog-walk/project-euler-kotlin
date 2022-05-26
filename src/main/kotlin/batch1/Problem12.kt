package batch1

import util.maths.gaussSum
import util.maths.primeFactors
import util.maths.primeNumbersOG

/**
 * Problem 12: Highly Divisible Triangular Number
 *
 * https://projecteuler.net/problem=12
 *
 * Goal: Find the value of the first triangle number to have more than N divisors.
 *
 * Constraints: 1 <= N <= 1e3
 *
 * Triangle Number: A number generated by adding all natural numbers prior to & including itself.
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
     * Returns the found triangle number generated as a Gaussian sum that has had its divisors
     * counted using prime decomposition.
     *
     * Since the components of a Gaussian sum (n & n+1) are co-prime (i.e. they can have neither
     * a common prime factor nor a common divisor), the amount of divisors can be assessed based
     * on the cycling formulae:
     *
     *      t represents Gaussian sum = n(n + 1)/2
     *
     *      (even n) D(t) = D(n/2) * D(n+1)
     *      D(n+1) becomes D(n) for the next number, which will be odd.
     *
     *      (odd n) D(t) = D(n) * D((n+1)/2)
     *
     * SPEED (WORST) 559.59ms for N = 1e3
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
        return t.gaussSum().toInt()
    }

    /**
     * Counts unique divisors of [n] using prime decomposition.
     *
     * e.g. 28 = 2^2 * 7^1, therefore
     *
     * number of divisors of 28 = (2 + 1) * (1 + 1) = 6 -> {1, 2, 4, 7, 14, 28}
     */
    fun countDivisors(n: Int): Int {
        return primeFactors(1L * n).values
            .map { it + 1 }
            .reduce { acc, v -> acc * v }
    }

    /**
     * Quick pick solution finds the first triangle over N for every N <= [n] & stores the
     * results as an IntArray that can be repeatedly accessed afterwards.
     *
     * SPEED (WORSE) 214.37ms for N = 1e3
     */
    fun firstTrianglesCache(n: Int): IntArray {
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
                val triangle = nextT.gaussSum().toInt()
                val dn2 = if (nextT % 2 == 0) {
                    countDivisors(nextT + 1)
                } else {
                    countDivisors((nextT + 1) / 2)
                }
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
     * Similar to the original single-pick function above that exploits co-prime property of
     * Gaussian sum but stores cumulative divisor counts in an array for quick access instead of
     * calculating the count for every new [n].
     *
     * Dual cyclic formulae use n - 1 instead of n + 1 to match the index used in the cached list.
     *
     * N.B. n_max was found by exhausting all solutions for n = [1, 1000] & finding the maximum
     * of the ratios of t:n. At n = 1000, the valid triangle number is the 41041st term.
     *
     * SPEED (BEST) 34.45ms for N = 1e3
     */
    fun firstTriangleOverNOptimised(n: Int): Int {
        val nMax = minOf(n * 53, 41100)
        val divisorCount = IntArray(nMax)
        var num = 0
        var dT = 0
        while (dT <= n) {
            num++
            for (i in num until nMax step num) {
                divisorCount[i]++
            }
            dT = if (num % 2 == 0) {
                divisorCount[num/2] * divisorCount[num-1]
            } else {
                divisorCount[num] * divisorCount[(num-1)/2]
            }
        }
        return num * (num - 1) / 2
    }

    /**
     * Generates primes to count number of divisors based on prime factorisation.
     *
     * SPEED (BETTER): 57.53ms for N = 1e3.
     */
    fun firstTriangleOverNUsingPrimes(n: Int): Int {
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
                // when the prime divisor would be greater than the residual n1
                // that residual n1 is the last prime factor with an exponent == 1.
                // so no need to identify it.
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