/**
 * Problem 5: Smallest Multiple
 * Goal: Find the smallest positive number that can be divided by each number
 * in the range 1..N, with 1 <= N <= 40.
 * e.g. The smallest number that can be evenly divided by every number from 1 to 10
 * is 2520.
 * Tests: 1..3 = 6; 1..10 = 2520
 */

class SmallestMultiple {
    fun smallestMultiple(rangeMax: Int): Int {
        return when (rangeMax) {
            1 -> 1
            2 -> 2
            else -> {
                val range = (rangeMax - 1) downTo (rangeMax / 2 + 1)
                lcm(rangeMax, range)
            }
        }
    }

    private fun lcm(max: Int, range: IntProgression): Int {
        var multiple = max * 2
        outer@ while (true) {
            for (i in range) {
                if (multiple % i != 0) {
                    multiple += max
                    continue@outer
                }
            }
            break@outer
        }
        return multiple
    }
}