package batch3

import util.combinatorics.permutations
import util.strings.isPandigital

/**
 * Problem 32: Pandigital Products
 *
 * https://projecteuler.net/problem=32
 *
 * Goal: Find the sum of all products whose identity expression can be written as an
 * N-pandigital number.
 *
 * Constraints: 4 <= N <= 9
 *
 * Pandigital Number: An N-digit number that uses all digits from 1 to N exactly once,
 * e.g. N = 5 -> 15234.
 *
 * Identity expression: 39(multiplicand) * 186(multiplier) = 7254(product).
 * Therefore, 7254 is a product of a pandigital identity expression.
 *
 * e.g.: N = 4
 *       identities = {3 * 4 = 12}
 *       sum = 12
 */

class PandigitalProducts {
    /**
     * Solution uses helper permutations() to assess all arrangements of the required digits for
     * a valid identity expression.
     *
     * SPEED (WORSE) 884.66ms for N = 9
     */
    fun sumPandigitalProductsAllPerms(n: Int): Int {
        // stored as set to ensure no duplicate permutation results
        val products = mutableSetOf<Int>()
        // neither multiplicand nor multiplier can have more digits than product
        val prodMaxDigits = when (n) {
            8, 9 -> 4
            7 -> 3
            else -> 2
        }
        for (perm in permutations(('1'..n.digitToChar()))) {
            // if multiplicand has 1 digit, it would be found at index 0
            for (a in 1..2) {
                // find start index of product based on multiplicand & product digits
                val pIndex = a + minOf(prodMaxDigits, n - prodMaxDigits - a)
                val multiplicand = perm.slice(0 until a)
                    .joinToString("")
                    .toInt()
                val multiplier = perm.slice(a until pIndex)
                    .joinToString("")
                    .toInt()
                val product = perm.slice(pIndex..perm.lastIndex)
                    .joinToString("")
                    .toInt()
                if (multiplicand * multiplier == product) {
                    products.add(product)
                    break
                }
                // expressions with < 7 digits can only have multiplier be 1 digit
                if (n < 7) break
            }
        }
        return products.sum()
    }

    /**
     * Iterative solution assesses the pandigital quality of all identity expressions produced by
     * multiplier, multiplicand combinations within a specified limit.
     *
     * SPEED (BETTER) 97.43ms for N = 9
     */
    fun sumPandigitalProductsBrute(n: Int): Int {
        // stored as set to ensure no duplicate permutation results
        val products = mutableSetOf<Int>()
        val multiplicandRange = if (n < 7) 2..9 else 2..98
        val multiplierMax = when (n) {
            8 -> 987
            9 -> 9876
            else -> 98
        }
        for (a in multiplicandRange) {
            for (b in (a+1)..multiplierMax) {
                val prod = a * b
                if ("$a$b$prod".isPandigital(n)) {
                    products.add(prod)
                }
            }
        }
        return products.sum()
    }
}