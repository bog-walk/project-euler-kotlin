package batch5

import kotlin.test.Test
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.assertContentEquals

internal class PrimePairSetsTest {
    private val tool = PrimePairSets()

    @Test
    fun `primePairSetSum correct for lower constraints`() {
        val n = 100
        val expected = listOf(listOf(107, 123), emptyList(), emptyList())
        for (k in 3..5) {
            assertContentEquals(expected[k-3], tool.concurrentPrimePairSetSum(n, k))
        }
    }

    @Test
    fun `primePairSetSum correct for lower mid constraints`() {
        val n = 1000
        val expected = listOf(
            listOf(107, 119, 123, 231, 239, 331, 381, 405, 447, 459),
            listOf(792, 1838), emptyList()
        )
        for (k in 3..5) {
            if (k == 3) {
                assertContentEquals(expected[0], tool.concurrentPrimePairSetSum(n, k).take(10))
            } else {
                assertContentEquals(expected[k-3], tool.concurrentPrimePairSetSum(n, k))
            }
        }
    }

    @Test
    fun `primePairSetSum correct for upper mid constraints`() {
        val n = 10_000
        val expected = listOf(
            listOf(27375, 27543, 27627, 27771, 27831, 28047, 28287, 28479, 28479, 28971),
            listOf(792, 1838, 2484, 2538, 2648, 3146, 3188, 3398, 3850, 4380)
        )
        assertContentEquals(expected[0], tool.concurrentPrimePairSetSum(n, 3).takeLast(10))
        assertContentEquals(expected[1], tool.concurrentPrimePairSetSum(n, 4).take(10))
    }

    @Test
    fun `primePairSetSum correct for upper constraints`() {
        val n = 20_000
        val k = 5
        val expected = listOf(26033, 34427)
        val solutions = mapOf(
            "Thread" to tool::threadPrimePairSetSum,
            "Coroutine" to tool::concurrentPrimePairSetSum
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                assertContentEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}