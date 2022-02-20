package batch2

import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class DigitFifthPowersTest {
    private val tool = DigitFifthPowers()

    @Test
    fun `digitNthPowers correct for all but upper N`() {
        val expected = listOf(
            listOf(153, 370, 371, 407),
            listOf(1634, 8208, 9474),
            listOf(4150, 4151, 54748, 92727, 93084, 194_979)
        )
        for (n in 3..5) {
            assertContentEquals(expected[n - 3], tool.digitNthPowersBrute(n))
            assertContentEquals(expected[n - 3], tool.digitNthPowers(n))
        }
    }

    @Test
    fun `digitNthPowers speed for upper constraints`() {
        val n = 6
        val expected = listOf(548_834)
        val solutions = mapOf(
            "Brute" to tool::digitNthPowersBrute, "Combinatorics" to tool::digitNthPowers
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertContentEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}