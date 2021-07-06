import kotlin.math.sqrt

/**
 * Problem 3: Largest Prime Factor
 * Goal: Find the largest prime factor of N, given 2 <= N <= 10^12.
 * Prime factorisation involves finding set of prime factors that
 * multiply to form N. e.g. 12 = 2 * 2 * 3. There will only ever be a
 * unique set of prime factors for a number: Fundamental Theorem of Arithmetic.
 * e.g. The largest prime factor of 13195 is 29, from {5, 7, 13, 29}.
 */

fun Long.isComposite(): Boolean {
    for (i in 2 until this) {
        if (this % i == 0L) return true
    }
    return false
}

fun Long.divide(factor: Long): Pair<Boolean, Long> {
    return Pair(this % factor == 0L, this / factor)
}

class LargestPrimeFactor {
    fun largestPrime(primes: List<Long>): Long = primes.maxOrNull()!!

    // Problematic for higher level constraint values (time out)
    fun getPrimeFactors(n: Long): List<Long> {
        var i = 1L
        var dividend = n
        val factors = mutableListOf<Long>()
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

    fun getPrimeFactorsRecursive(
        n: Long,
        primes: MutableList<Long> = mutableListOf()
    ): List<Long> {
        for (i in 2 until n) {
            if (n % i == 0L) {
                primes.add(i)
                return getPrimeFactorsRecursive(n / i, primes)
            }
        }
        primes.add(n)
        return primes
    }

    /**
     * 2 is the only even prime number so it is treated separately, so
     * as to allow stepping through iteration to odd numbers only later
     * on. No need to iterate until the target number as will not have
     * a prime factor greater than its square root.
     */
    fun getPrimeFactorsRecursiveImproved(
        n: Long,
        primes: MutableList<Long> = mutableListOf()
    ): List<Long> {
        var num = n
        while (num % 2 == 0L) {
            primes.add(2)
            num /= 2
        }
        if (num > 1) {
            val maxFactor = sqrt(num.toDouble()).toInt()
            for (i in 3..maxFactor step 2) {
                if (num % i == 0L) {
                    primes.add(i.toLong())
                    return getPrimeFactorsRecursiveImproved(num / i, primes)
                }
            }
        }
        if (num > 2) primes.add(num)
        return primes
    }
}