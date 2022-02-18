package util.combinatorics

/**
 * Original implementation of an algorithm that returns [r]-length subLists of [elements].
 *
 * SPEED (EQUAL) 23.8ms for N = 9, r = 6
 * SPEED (WORSE) 485.19ms for N = 19, r = 15
 */
internal fun  <T: Any> getCombinations(elements: Iterable<T>, r: Int): List<List<T>> {
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
    yield(List(r) { input[indices[it]] })
    while (true) {
        var i = r - 1
        while (i >= 0) {
            if (indices[i] != i + n - r) break
            if (i-- == 0) return@sequence
        }
        indices[i]++
        for (j in i + 1 until r) {
            indices[j] = indices[j-1] + 1
        }
        yield(List(r) { input[indices[it]] })
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
    yield(List(r) { input[indices[it]] })
    while (true) {
        var i = r - 1
        while (i >= 0) {
            if (indices[i] != n - 1) break
            if (i-- == 0) return@sequence
        }
        indices = indices.slice(0 until i) + List(r - i) { indices[i] + 1 }
        yield(List(r) { input[indices[it]] })
    }
}

/**
 * Heap's Algorithm to generate all [size]! permutations of a list of [size] characters with
 * minimal movement, so returned list is not necessarily sorted. This is unlike the solution
 * below that returns an already sorted order of permutations & is flexible enough to accept a
 * size smaller than the size of elements available, as well elements other than CharSequence.
 *
 * Initially k = [size], then recursively k decrements and each step generates k! permutations that
 * end with the same [size] - k elements. Each step modifies the initial k - 1 elements with a
 * swap based on k's parity.
 *
 * SPEED (WORSE) 2.46s for N = 10, r = 10
 *
 * @throws OutOfMemoryError if [size] > 10, consider using an iterative approach instead.
 */
internal fun getPermutations(
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
 * Returns Cartesian product of [elements].
 *
 * If [elements] is sorted, product lists will be yielded in lexicographic order.
 *
 * e.g. elements = "ABCD", "xy" -> Ax Ay Bx By Cx Cy Dx Dy
 *
 * This version currently does not enable the product of an iterable with itself.
 */
fun <T: Any> product(vararg elements: Iterable<T>) = sequence {
    if (elements.isEmpty()) return@sequence
    val inputs = elements.map { it.toList() }
    val results = inputs.fold(listOf(listOf<T>())) { acc, list ->
        acc.flatMap { accList -> list.map { e -> accList + e } }
    }
    for (result in results) {
        yield(result)
    }
}