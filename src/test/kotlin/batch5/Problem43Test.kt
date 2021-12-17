package batch5

import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SubstringDivisibilityTest {
    private val tool = SubstringDivisibility()

    @Test
    fun testSumOfPandigitalSubs() {
        val expected = listOf(
            22212, 711104, 12444480, 189838560, 1099210170, 1113342912, 16695334890
        )
        for (n in 3..9) {
            assertEquals(expected[n - 3], tool.sumOfPandigitalSubstrings(n))
        }
    }

    @Test
    fun testSumOf9PandigitalSubs_speed() {
        val ogAns: Long
        val seqAns: Long
        val ogTime = measureNanoTime {
            ogAns = tool.sumOfPandigitalSubstrings(9)
        }
        val seqTime = measureNanoTime {
            seqAns = tool.sumOf9PandigitalSubstrings()
        }
        println("Original solution took: ${1.0 * ogTime / 1_000_000_000}s\n" +
                "Sequence solution took: ${1.0 * seqTime / 1_000_000_000}s")
        assertEquals(ogAns, seqAns)
    }
}