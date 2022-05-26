package batch2

import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NDigitFibonacciNumberTest {
    private val tool = NDigitFibonacciNumber()

    @Test
    fun `nDigitFibTerm correct`() {
        val digits = listOf(2, 3, 4, 9, 1000)
        val expected = listOf(7, 12, 17, 40, 4782)
        val allTerms = tool.nDigitFibTermsBrute(1000)
        for ((i, digit) in digits.withIndex()) {
            assertEquals(expected[i], allTerms[digit - 2])
            assertEquals(expected[i], tool.nDigitFibTermGoldenFormula(digit))
        }
    }

    @Test
    fun `golden ratio correct for getting Nth fib`() {
        val expected = listOf(
            0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597
        )
        val actual = List(18) { n -> tool.nthFibUsingGoldenRatio(n) }
        assertEquals(expected, actual)
    }

    @Test
    fun `golden ratio correct for getting N-digit term`() {
        val expected = listOf(7, 12, 17, 21, 26, 31, 36, 40)
        for (n in 2..9) {
            assertEquals(expected[n - 2], tool.nDigitFibTermGoldenBrute(n))
        }
    }

    @Test
    fun `nDigitFibTerm speed for low N`() {
        val n = 10
        val expected = 45
        val solutions = mapOf(
            "Golden formula" to tool::nDigitFibTermGoldenFormula,
            "Golden brute" to tool::nDigitFibTermGoldenBrute
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        val bruteActual: Int
        val bruteTime = measureNanoTime {
            bruteActual = tool.nDigitFibTermsBrute(n)[n - 2]
        }
        compareSpeed("Brute check" to bruteTime)
        assertEquals(expected, bruteActual, "Incorrect Brute check -> $bruteActual")
        compareSpeed(speeds)
    }

    @Test
    fun `nDigitFibTerm speed for high N`() {
        val n = 5000
        val expected = 23922
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        val bruteActual: Int
        val bruteTime = measureNanoTime {
            bruteActual = tool.nDigitFibTermsBrute(n)[n - 2]
        }
        compareSpeed("Brute check" to bruteTime)
        assertEquals(expected, bruteActual)
        getSpeed(tool::nDigitFibTermGoldenFormula, n).run {
            speeds.add("Golden formula" to second)
            assertEquals(expected, first, "Incorrect Golden Formula -> $first")
        }
        compareSpeed(speeds)
    }
}