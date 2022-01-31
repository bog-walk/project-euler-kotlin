package util.search

/**
 * Binary search algorithm implementation.
 *
 * @param [collection] an ordered & subscriptable container assumed to be already
 * sorted in ascending order. Currently only works with Lists.
 * @return false if collection is empty or element not present; otherwise, true.
 */
fun <E: Comparable<E>> binarySearch(target: E, collection: List<E>): Boolean {
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