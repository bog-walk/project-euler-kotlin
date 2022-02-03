package util.search

/**
 * Binary search algorithm implementation that was written before realising Kotlin already
 * implements this algorithm in its standard library.
 *
 * SPEED (WORSE) 79900ns for edge target in 1e4-sized list
 *
 * @param [collection] an ordered & subscriptable container assumed to be already
 * sorted in ascending order. Currently only works with Lists.
 * @return false if collection is empty or element not present; otherwise, true.
 */
fun <E : Comparable<E>> binarySearchManual(target: E, collection: List<E>): Boolean {
    var low = 0
    var high = collection.size - 1
    while (low <= high) {
        val middle = (low + high) / 2
        val found = collection[middle]
        if (found == target) return true
        if (found < target) {
            low = middle + 1
        } else {
            high = middle - 1
        }
    }
    return false
}

/**
 * Standard library implementation of the binary search algorithm.
 *
 * The function has extra functionality that can be used, e.g. defining the given list to a
 * smaller search range, or including a comparator, or calling the function on an Array. It
 * normally returns the index position of the target, if found; otherwise its inverse index (i.e.
 * the position at which the element should be inserted to maintain a sorted collection.
 *
 * This version will be preferentially used in all solutions.
 *
 * SPEED (BETTER) 52000ns for edge target in 1e4-sized list
 *
 * @return false if collection is empty or element not present; otherwise, true.
 */
fun <E : Comparable<E>> binarySearch(target: E, collection: List<E>) : Boolean {
    return collection.binarySearch(target) >= 0
}