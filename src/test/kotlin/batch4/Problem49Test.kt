package batch4

import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class PrimePermutationsTest {
    private val tool = PrimePermutations()

    @Test
    fun `primePermSequence correct for 4-digit permutations`() {
        val n = 10_000
        val expected = listOf(listOf("148748178147", "296962999629"), emptyList())
        for (k in 3..4) {
            assertContentEquals(expected[k-3], tool.primePermSequence(n, k))
            assertContentEquals(expected[k-3], tool.primePermSequenceImproved(n, k))
        }
    }

    @Test
    fun `primePermSequence correct for lower 5-digit permutations`() {
        val n = 20_000
        val expected = listOf(
            listOf(
            "148748178147", "296962999629", "114831481318143", "114974171971941",
            "127131321713721", "127391723921739", "127571725721757", "127991729921799",
            "148214812181421", "148313148148131", "148974718979481", "185035180385103",
            "185935189385193", "195433549151439"
            ),
            emptyList()
        )
        for (k in 3..4) {
            assertContentEquals(expected[k-3], tool.primePermSequence(n, k))
            assertContentEquals(expected[k-3], tool.primePermSequenceImproved(n, k))
        }
    }

    @Test
    fun `primePermSequence correct for higher 5-digit permutations with K equal 3`() {
        val n = 100_000
        val k = 3
        val expectedSize = 55
        assertEquals(expectedSize, tool.primePermSequence(n, k).size)
        assertEquals(expectedSize, tool.primePermSequenceImproved(n, k).size)
    }

    @Test
    fun `primePermSequence correct for higher 5-digit permutations with K equal 4`() {
        val n = 100_000
        val k = 4
        val expected = listOf("83987889379388798837")
        assertContentEquals(expected, tool.primePermSequence(n, k))
        assertContentEquals(expected, tool.primePermSequenceImproved(n, k))
    }

    @Test
    fun `primePermSequence speed`() {
        val n = 1_000_000
        val k = 3
        val expectedSize = 883
        val solutions = mapOf(
            "Original" to tool::primePermSequence, "Improved" to tool::primePermSequenceImproved
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                assertEquals(expectedSize, first.size, "Incorrect $name -> ${first.size}")
            }
        }
        compareSpeed(speeds)
    }
}