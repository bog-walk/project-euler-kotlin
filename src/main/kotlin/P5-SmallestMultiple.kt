/**
 * Problem 5: Smallest Multiple
 * Goal: Find the smallest positive number that can be divided by each number
 * in the range 1..N, with 1 <= N <= 40.
 * e.g. The smallest number that can be evenly divided by every number from 1 to 10
 * is 2520.
 */

class SmallestMultiple {
    fun smallestMultiple(rangeMax: Int): Long {
        return when (rangeMax) {
            1 -> 1L
            2 -> 2L
            3 -> 6L
            4 -> 12L
            else -> {
                val range = (rangeMax - 2) downTo (rangeMax / 2 + 1)
                val maxMultiple = 1L * rangeMax * (rangeMax - 1)
                lcm(maxMultiple, range)
            }
        }
    }

    private fun lcm(max: Long, range: IntProgression): Long {
        var multiple = max * range.first
        outer@ while (true) {
            for (i in range) {
                if (multiple % i != 0L) {
                    multiple += max
                    continue@outer
                }
            }
            break@outer
        }
        return multiple
    }
}