package dev.bogwalk.util.combinatorics

import dev.bogwalk.util.maths.factorial
import java.math.BigInteger

/**
 * Returns the number of ways to choose [k] items from [n] items without repetition and without
 * order, namely C(n, k).
 *
 * @throws IllegalArgumentException if either Int is negative.
 */
fun binomialCoefficient(n: Int, k: Int): BigInteger {
    require(n >= 0 && k >= 0) { "Both numbers must be non-negative" }
    if (k > n) return BigInteger.ZERO
    return n.factorial() / (k.factorial() * (n - k).factorial())
}

/**
 * Original implementation of an algorithm that returns [r]-length subLists of [elements].
 *
 * SPEED (EQUAL) 23.8ms for N = 9, r = 6
 * SPEED (WORSE) 485.19ms for N = 19, r = 15
 */
private fun  <T: Any> getCombinations(elements: Iterable<T>, r: Int): List<List<T>> {
    if (r == 1) return elements.map { listOf(it) }
    val input = elements.toList()
    if (r > input.size || r == 0) return emptyList()
    val combinations = mutableListOf<List<T>>()
    for (i in input.indices) {
        val element = mutableListOf(input[i])
        for (c in getCombinations(input.slice((i+1)..input.lastIndex), r-1)) {
            combinations.add(element + c)
        }
    }
    return combinations
}

/**
 * Mimics the Python itertools module function and returns [r]-length subLists of [elements].
 *
 * If [elements] is sorted, combinations will be yielded in lexicographic order.
 *
 * If [elements] contains only unique values, there will be no repeat values in each combination.
 *
 * e.g. elements = "ABCD", r = 2 -> AB AC AD BC BD CD
 *
 * The number of yielded combinations, if n = the amount of elements, is:
 *
 * n!/r!/(n - r)!
 *
 * This solution will be preferentially used in future solutions.
 *
 * SPEED (EQUAL) 24.1ms for N = 9, r = 6
 * SPEED (BETTER) 69.72ms for N = 19, r = 15
 */
fun <T: Any> combinations(elements: Iterable<T>, r: Int) = sequence {
    val input = elements.toList()
    val n = input.size
    if (r == 0 || r > n) return@sequence
    val indices = (0 until r).toMutableList()
    while (true) {
        yield(List(r) { input[indices[it]] })
        var i = r - 1
        while (i >= 0) {
            if (indices[i] != i + n - r) break
            if (i-- == 0) return@sequence
        }
        indices[i]++
        for (j in i + 1 until r) {
            indices[j] = indices[j-1] + 1
        }
    }
}

/**
 * Mimics the Python itertools module function and returns [r]-length subLists of [elements]
 * allowing individual elements to be repeated more than once.
 *
 * If [elements] is sorted, combinations will be yielded in lexicographic order.
 *
 * If [elements] contains only unique values, combinations will also be unique.
 *
 * e.g. elements = "ABCD", r = 2 -> AA AB AC AD BB BC BD CC CD DD
 *
 * The number of yielded combinations, if n = the amount of elements, is:
 *
 * (n + r - 1)!/r!/(n - 1)!
 */
fun <T: Any> combinationsWithReplacement(elements: Iterable<T>, r: Int) = sequence {
    val input = elements.toList()
    val n = input.size
    if (r == 0 || n == 0) return@sequence
    var indices = List(r) { 0 }
    while (true) {
        yield(List(r) { input[indices[it]] })
        var i = r - 1
        while (i >= 0) {
            if (indices[i] != n - 1) break
            if (i-- == 0) return@sequence
        }
        indices = indices.slice(0 until i) + List(r - i) { indices[i] + 1 }
    }
}

/**
 * Heap's Algorithm to generate all [size]! permutations of a list of [size] characters with
 * minimal movement, so returned list is not necessarily sorted. This is unlike the solution
 * below that returns an already sorted order of permutations & is flexible enough to accept a
 * size smaller than the size of elements available, as well as elements other than CharSequence.
 *
 * Initially k = [size], then recursively k decrements and each step generates k! permutations that
 * end with the same [size] - k elements. Each step modifies the initial k - 1 elements with a
 * swap based on k's parity.
 *
 * SPEED (WORSE) 2.46s for N = 10, r = 10
 *
 * @throws OutOfMemoryError if [size] > 10, consider using an iterative approach instead.
 */
private fun getPermutations(
    chars: MutableList<Char>,
    size: Int,
    perms: MutableList<String> = mutableListOf()
): List<String> {
    if (size == 1) {
        perms.add(chars.joinToString(""))
    } else {
        repeat(size) { i ->
            getPermutations(chars, size - 1, perms)
            // avoids unnecessary swaps of the kth & 0th element
            if (i < size - 1) {
                val swap = if (size % 2 == 0) i else 0
                chars[size - 1] = chars[swap].also { chars[swap] = chars[size - 1] }
            }
        }
    }
    return perms
}

/**
 * Mimics the Python itertools module function and returns successive [r]-length permutations of
 * [elements].
 *
 * If [elements] is sorted, permutations will be yielded in lexicographic order.
 *
 * If [elements] contains only unique values, there will be no repeat values in each permutation.
 *
 * e.g. elements = "ABCD", r = 2 -> AB AC AD BA BC BD CA CB CD DA DB DC
 *
 * The number of yielded permutations, if n = the amount of elements, is:
 *
 * n!/(n - r)!
 *
 * This solution will be preferentially used in future solutions.
 *
 * SPEED (BETTER) 20500ns for N = 10, r = 10
 */
fun <T: Any> permutations(elements: Iterable<T>, r: Int = -1) = sequence {
    val input = elements.toList()
    val n = input.size
    val k = if (r == -1) n else r
    if (k == 0 || k > n) return@sequence
    var indices = (0 until n).toMutableList()
    val cycles = (n downTo n - k + 1).toMutableList()
    yield(List(k) { input[indices[it]] })
    while (true) {
        var i = k - 1
        while (i >= 0) {
            if (--cycles[i] == 0) {
                indices = (indices.take(i) + indices.slice(i + 1..indices.lastIndex) +
                        indices[i]).toMutableList()
                cycles[i] = n - i
                if (i-- == 0) return@sequence
            } else {
                val j = cycles[i]
                indices[n-j] = indices[i].also { indices[i] = indices[n-j] }
                yield(List(k) { input[indices[it]] })
                break
            }
        }
    }
}

/**
 * Generates a hash key based on the amount of repeated digits, represented as an ordered LTR
 * array.
 *
 * Hash key must be stored as an array rather than a number to allow for the possibility that [n]
 * may have a digit that is repeated more than 9 times.
 *
 * e.g. 1487 -> [0, 1, 0, 0, 1, 0, 0, 1, 1]
 *      2214 -> [0, 1, 2, 0, 4]
 *      1_000_000_000_000 -> [12, 1]
 *
 * N.B. An alternative hash key would be the sorted string cast of the number, using
 * num.toString().toList().sorted().joinToString("").
 * e.g. 1487 -> "1478", 2023 -> "0223".
 *
 * N.N.B. Permutation ID results should be compared using contents of the IntArray, as comparing
 * joined String representations, may lead to errors; e.g. 1_000_000_000_000 has the same String
 * ID ("121") as 1012.
 *
 * This solution would only be used if arguments were expected to consistently be greater than
 * 15-digits long or performance was not a concern.
 *
 * SPEED (WORSE) 30.59ms for N = Long.MAX_VALUE
 *
 * @param [n] Number may have digits that are duplicated any number of times.
 */
internal fun permutationIDOG(n: Long): IntArray {
    var perm = n
    val permID = IntArray(n.toString().maxOf { it }.digitToInt() + 1)
    while (perm > 0) {
        val digit = (perm % 10).toInt()
        permID[digit]++
        perm /= 10
    }
    return permID
}

/**
 * Generates a hash key based on the amount of repeated digits, represented as a String.
 *
 * Instead of storing digits counts in an array, a bitwise left shift accumulates a Long value
 * that is unique, based on the fact that a left arithmetic shift by x is equivalent to
 * multiplying by 2^x, such that:
 *
 *      1 << x * d == (2^x)^d,
 *      with x equal 4, since 2^4 is the first to exceed a single-digit value.
 *
 * If 2 is used, 2222222222222222 will have the same hash as 3333 (i.e. 256) because
 * (16 * (2^2)^2) == (4 * 4^3).
 * If 3 is used, 22222222 will have the same hash as 3 (i.e. 512) because
 * (8 * (2^3)^2) == (1 * 8^3).
 *
 * This means that the following solution cannot allow a digit to occur 16 times,
 * e.g. 2222222222222222 will have the same hash as 3 (i.e. 4096) because
 * (16 * (2^4)^2) == (1 * 16^3).
 *
 * This solution will be preferentially used in future solutions when speed is necessary and
 * arguments are not expected to be greater than 15-digits long.
 *
 * SPEED (BETTER) 1.8e+04ns for N = Long.MAX_VALUE
 *
 * @param [n] Number should not be greater than 15-digits long to ensure duplicates of 16 digits
 * are not present.
 */
fun permutationID(n: Long): String {
    var perm = n
    var permID = 0L
    while (perm > 0) {
        val digit = (perm % 10).toInt()
        permID += 1L shl 4 * digit
        perm /= 10
    }
    return permID.toString()
}

/**
 * Returns Cartesian product of [elements].
 *
 * To return the product of an iterable with itself, specify the number of repetitions using
 * [repeat].
 *
 * If [elements] is sorted, product lists will be yielded in lexicographic order.
 *
 * e.g. elements = "ABCD", "xy" -> Ax Ay Bx By Cx Cy Dx Dy
 * e.g. elements = "AB", repeat = 2 -> AA AB BA BB
 */
fun <T: Any> product(
    vararg elements: Iterable<T>,
    repeat: Int = 1
) = sequence {
    if (elements.isEmpty()) return@sequence
    val inputs = mutableListOf<List<T>>()
    for (i in 0 until repeat) {
        inputs += elements.map { it.toList() }
    }
    val results = inputs.fold(listOf(listOf<T>())) { acc, list ->
        acc.flatMap { accList -> list.map { e -> accList + e } }
    }
    for (result in results) {
        yield(result)
    }
}