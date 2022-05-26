package batch7

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.system.measureNanoTime

internal class SquareRootDigitalExpansionTest {
    private val tool = SquareRootDigitalExpansion()

    @ParameterizedTest(name="N = {0}, P = {1}")
    @CsvSource(
        // lower constraints
        "1, 1, 0", "2, 10, 29", "2, 20, 76", "2, 100, 475", "2, 10000, 45349",
        "16, 500, 26795", "100, 100, 40886",
        // mid constraints
        "230, 100, 96340", "500, 100, 214519", "900, 1000, 3913848",
        // upper constraints
        "1000, 1000, 4359087"
    )
    fun `correct for all constraints`(n: Int, p: Int, expected: Int) {
        assertEquals(expected, tool.irrationalSquareDigitSum(n, p))
        assertEquals(expected, tool.irrationalSquareDigitSum(n, p, manualRoot = true))
        assertEquals(expected, tool.irrationalSquareDigitSumImproved(n, p))
    }

    @Test
    fun `sqrt function speed for upper constraints`() {
        val n = 100
        val p = 10000
        val expected = 4_048_597
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        val builtinActual: Int
        val builtinTime = measureNanoTime {
            builtinActual = tool.irrationalSquareDigitSum(n, p)
        }
        compareSpeed("Built-in" to builtinTime)
        assertEquals(expected, builtinActual)
        val manualActual: Int
        val manualTime = measureNanoTime {
            manualActual = tool.irrationalSquareDigitSum(n, p, manualRoot = true)
        }
        compareSpeed("Manual" to manualTime)
        assertEquals(expected, manualActual)
        getSpeed(tool::irrationalSquareDigitSumImproved, n, p).run {
            speeds.add("Improved" to second)
            assertEquals(expected, first)
        }
        compareSpeed(speeds)
    }
}