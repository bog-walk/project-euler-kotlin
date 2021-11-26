package batch4

/**
 * Problem 32: Pandigital Products
 *
 * https://projecteuler.net/problem=32
 *
 * Goal: Find the sum of all products whose identity expression
 * can be written as an N-pandigital number.
 *
 * Constraints: 4 <= N <= 9
 *
 * Pandigital Number: An N-digit number that uses all digits
 * from 1 to N exactly once, e.g. N = 5 -> 15234.
 *
 * Identity expression: 39(multiplicand) * 186(multiplier) = 7254(product).
 * Therefore, 7254 is a product of a pandigital identity expression.
 *
 * e.g.: N = 4
 *       identities = {3 * 4 = 12}
 *       sum = 12
 */

class PandigitalProducts {
    fun sumPandigitalProducts(n: Int): Int {
        val products = mutableSetOf<Int>()
        // ('0' + n) if digitToChar() not supported by Kotlin version
        val digits = ('1'..n.digitToChar()).toList()
        val aRange = if (n < 7) 2..9 else 2..99
        val bMax = when (n) {
            8 -> 999
            9 -> 9999
            else -> 99
        }
        for (a in aRange) {
            for (b in (a+1)..bMax) {
                val prod = a * b
                if (digits == "$a$b$prod".toList().sorted()) {
                    products.add(prod)
                }
            }
        }
        return products.sum()
    }
}