/**
 * Problem 5: Smallest Multiple
 * Goal: Find the smallest positive number that can be divided by each number
 * in the range 1..N, with 1 <= N <= 40.
 * e.g. The smallest number that can be evenly divided by every number from 1 to 10
 * is 2520.
 */

class SmallestMultiple {
    fun smallestMultiple(rangeMax: Int): Long {
        val range = (rangeMax - 1) downTo (rangeMax / 2 + 1)
        return lcm(rangeMax.toLong(), range)
    }

    private fun lcm(max: Long, range: IntProgression): Long {
        var lcm = max
        var step = max
        for (i in range) {
            while (lcm % i != 0L) {
                lcm += step
            }
            step = lcm
        }
        return lcm
    }
}