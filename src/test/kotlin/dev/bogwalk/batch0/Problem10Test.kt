package dev.bogwalk.batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.maths.primeNumbersOG
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertContentEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SummationOfPrimesTest {
    private val tool = SummationOfPrimes()
    private lateinit var allPrimes: LongArray
    private lateinit var allPrimesOpt: LongArray

    @BeforeAll
    fun `setup and speed`() {
        val n = 1e6.toInt()
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::sumOfPrimesQuickDraw, n).run {
            speeds.add("Original" to second)
            allPrimes = first
        }
        getSpeed(tool::sumOfPrimesQuickDrawOptimised, n).run {
            speeds.add("Optimised" to second)
            allPrimesOpt = first
        }
        compareSpeed(speeds)
    }

    @Test
    fun `setup allPrimes lists correctly`() {
        assertContentEquals(allPrimes.takeLast(10), allPrimesOpt.takeLast(10))
    }

    @ParameterizedTest(name="N={0} = {1}")
    @CsvSource(
        // lower constraints
        "2, 2", "3, 5", "5, 10", "10, 17",
        // mid constraints
        "100, 1060", "5000, 1_548_136",
        // upper constraints
        "300_000, 3_709_507_114", "1_000_000, 37_550_402_023"
    )
    fun `sumOfPrimes correct`(n: Int, expected: Long) {
        assertEquals(expected, primeNumbersOG(n).sumOf { it.toLong() })
        assertEquals(expected, allPrimes[n])
        assertEquals(expected, allPrimesOpt[n])
    }
}