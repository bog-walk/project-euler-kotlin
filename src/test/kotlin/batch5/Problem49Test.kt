package batch5

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class PrimePermutationsTest {
    private val tool = PrimePermutations()

    @Test
    fun testPrimePermSequence_4Digit() {
        val n = 10_000
        val expected = listOf("148748178147", "296962999629")
        assertContentEquals(expected, tool.primePermSequence(n, k=3))
        assertContentEquals(emptyList(), tool.primePermSequence(n, k=4))
        assertContentEquals(expected, tool.primePermSequenceImproved(n, k=3))
        assertContentEquals(emptyList(), tool.primePermSequenceImproved(n, k=4))
    }

    @Test
    fun testPrimePermSequence_5DigitLow() {
        val n = 20_000
        val expected = listOf(
            "148748178147", "296962999629", "114831481318143", "114974171971941",
            "127131321713721", "127391723921739", "127571725721757", "127991729921799",
            "148214812181421", "148313148148131", "148974718979481", "185035180385103",
            "185935189385193", "195433549151439"
        )
        assertContentEquals(expected, tool.primePermSequence(n, k=3))
        assertContentEquals(emptyList(), tool.primePermSequence(n, k=4))
        assertContentEquals(expected, tool.primePermSequenceImproved(n, k=3))
        assertContentEquals(emptyList(), tool.primePermSequenceImproved(n, k=4))
    }

    @Test
    fun testPrimePermSequence_5DigitHigh() {
        val n = 100_000
        val expectedSize = 55
        assertEquals(expectedSize, tool.primePermSequence(n, k=3).size)
        assertEquals(expectedSize, tool.primePermSequenceImproved(n, k=3).size)
        val expected = listOf("83987889379388798837")
        assertContentEquals(expected, tool.primePermSequence(n, k=4))
        assertContentEquals(expected, tool.primePermSequenceImproved(n, k=4))
    }
}