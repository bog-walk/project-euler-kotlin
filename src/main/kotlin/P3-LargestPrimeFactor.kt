/**
 * Problem 3: Largest Prime Factor
 * Goal: Find the largest prime factor of N, given 10 <= N <= 10^12.
 * Prime factorisation involves finding set of prime factors that
 * multiply to form N. e.g. 12 = 2 * 2 * 3. There will only ever be a
 * unique set of prime factors for a number: Fundamental Theorem of Arithmetic.
 * e.g. The largest prime factor of 13195 is 29, from {5, 7, 13, 29}.
 */

fun Int.isComposite(): Boolean {
    for (i in 2 until this) {
        if (this % i == 0) return true
    }
    return false
}

fun Long.divide(factor: Int): Pair<Boolean, Long> {
    return Pair(this % factor == 0L, this / factor)
}

class LargestPrimeFactor {
    fun largestPrime(n: Long): Int {
        val primeFactors = getPrimeFactors(n)
        // could use max, but largest should be last element as ordered
        return if (primeFactors.isEmpty()) {
            0
        } else {
            primeFactors.last()
        }
    }

    fun getPrimeFactors(n: Long): List<Int> {
        var i = 1
        var dividend = n
        val factors = mutableListOf<Int>()
        while (i <= n) {
            i++
            if (i.isComposite()) continue
            var division = dividend.divide(i)
            while (division.first) {
                factors.add(i)
                dividend = division.second
                division = dividend.divide(i)
            }
        }
        return factors
    }
}