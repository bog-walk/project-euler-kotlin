package batch3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test

internal class CoinSumsTest {
    private val tool = CoinSums()

    @ParameterizedTest(name="N={0}->{1} combos")
    @CsvSource(
        // lower constraints
        "1, 1", "2, 2", "3, 2", "4, 3", "5, 4",
        "6, 5", "7, 6", "8, 7", "9, 8",
        // small values
        "10, 11", "15, 22", "20, 41",
        // large values
        "50, 451", "200, 73682", "500, 6295434",
        // upper constraints
        "10000, 296710490"
    )
    fun testCountCoinCombos(n: Int, expected: Int) {
        assertEquals(expected, tool.countCoinCombos(n))
        assertEquals(expected, tool.countCoinCombosRecursive(n))
    }

    @Test
    fun testCountCoinCombos_speedComparison() {
        val n = 100000
        val ansRecursive: Int
        val ansOptimised: Int
        val timeRecursive = measureNanoTime {
            ansRecursive = tool.countCoinCombosRecursive(n)
        }
        val timeOptimised = measureNanoTime {
            ansOptimised = tool.countCoinCombos(n)
        }
        println("Recursive solution took: ${timeRecursive / 1_000_000}ms\n" +
                "Optimised solution took: ${timeOptimised / 1_000_000}ms")
        assertEquals(ansRecursive, ansOptimised)
    }
}