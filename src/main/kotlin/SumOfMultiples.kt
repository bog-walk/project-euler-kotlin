/**
 * Problem 1: Multiples of 3 & 5
 * Goal: Find sum of all natural numbers less than N that are
 * multiples of any of the provided factors, with 1 <= N <= 1e9.
 * e.g. Sum of all multiples of 3 or 5 below 10 (i.e. 3, 5, 6, 9) = 23.
 */
fun Int.sumOfMultiples(vararg factors: Int): Int {
    return (1 until this).filter { num ->
        factors.any { factor ->
            num % factor == 0
        }
    }.sum()
}

class SumOfMultiples {

    fun sumOfMultiplesVersionB(number: Int, vararg factors: Int): Int {
        var sum = 0
        outer@for (n in 1 until number) {
            for (factor in factors) {
                if (n % factor == 0) {
                    sum += n
                    continue@outer
                }
            }
        }
        return sum
    }

    // Issues with factorsList not multiplying upwards properly
    fun sumOfMultiplesVersionC(number: Int, vararg factors: Int): Int {
        var count = 2
        val multiples = mutableSetOf<Int>()
        var factorsList = factors.toList()
        while (factorsList.isNotEmpty()) {
            multiples.addAll(factorsList.filter { it < number })
            factorsList = factorsList.map { it * count++ }
        }
        return multiples.sum()
    }
}