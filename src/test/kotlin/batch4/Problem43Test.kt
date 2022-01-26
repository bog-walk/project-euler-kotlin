package batch4

import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SubstringDivisibilityTest {
    private val tool = SubstringDivisibility()

    @Test
    fun testSumOfPandigitalSubs() {
        val expected = listOf<Long>(
            22212, 711104, 12444480, 189838560, 1099210170, 1113342912
        )
        for (n in 3..8) {
            assertEquals(expected[n - 3], tool.sumOfPandigitalSubstrings(n))
            assertEquals(expected[n - 3], tool.sumOfPandigitalSubstrings_alt(n))
        }
    }

    @Test
    fun testSumOf9PandigitalSubs_speed() {
        val n = 9
        val expected = 16695334890
        val solutions = LongArray(3)
        val ogTime = measureNanoTime {
            solutions[0] = tool.sumOfPandigitalSubstrings(n)
        }
        val altTime = measureNanoTime {
            solutions[1] = tool.sumOfPandigitalSubstrings_alt(n)
        }
        val seqTime = measureNanoTime {
            solutions[2] = tool.sumOf9PandigitalSubstrings()
        }
        println("Original solution took: ${1.0 * ogTime / 1_000_000_000}s\n" +
                "Alt solution took: ${1.0 * altTime / 1_000_000_000}s\n" +
                "Sequence solution took: ${1.0 * seqTime / 1_000_000_000}s")
        assertTrue { solutions.all { expected == it } }
    }
}