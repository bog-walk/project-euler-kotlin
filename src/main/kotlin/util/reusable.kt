package util

import kotlin.math.sqrt

fun getPrimeFactors(
    n: Int,
    primes: MutableList<Int> = mutableListOf()
): List<Int> {
    var num = n
    while (num % 2 == 0) {
        primes.add(2)
        num /= 2
    }
    if (num > 1) {
        val maxFactor = sqrt(num.toDouble()).toInt()
        for (i in 3..maxFactor step 2) {
            if (num % i == 0) {
                primes.add(i)
                return getPrimeFactors(num / i, primes)
            }
        }
    }
    if (num > 2) primes.add(num)
    return primes
}