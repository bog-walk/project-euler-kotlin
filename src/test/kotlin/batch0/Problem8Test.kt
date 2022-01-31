package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargestProductInSeriesTest {
    private val tool = LargestProductInSeries()
    private val thousandDigits = getTestResource(
        "src/test/resources/LargestProductInSeries1000"
    ).joinToString("")

    @ParameterizedTest(name="{0} *..* = {1}")
    @CsvSource(
        "'8', 8", "'1234', 24", "'63972201', 0",
        "'1111111111111', 1", "'3675356291', 1020600"
    )
    fun `stringProduct correct`(number: String, expected: Long) {
        assertEquals(expected, tool.stringProduct(number))
    }

    @ParameterizedTest(name="{0} = {3}")
    @CsvSource(
        // N == 1
        "'8', 1, 1, 8",
        // K == 1
        "'63972201', 8, 1, 9",
        // N == K
        "'1234', 4, 4, 24", "'1111111111111', 13, 13, 1",
        // number contains 0 in all series
        "'2709360626', 10, 5, 0",
        // number contains 0 in some series
        "'12034', 5, 2, 12",
        // number is normal
        "'3675356291', 10, 5, 3150"
    )
    fun `largestSeriesProduct correct`(number: String, n: Int, k: Int, expected: Long) {
        assertEquals(expected, tool.largestSeriesProduct(number, n, k))
        assertEquals(expected, tool.largestSeriesProductRecursive(number, n, k))
    }

    @Test
    fun `largestSeriesProduct correct for 100 digit number`() {
        val n = 100
        val k = 6
        // create a number of all '1's except for 6 adjacent '6's
        val number = List(n) {
            if (it in 60..65) k else 1
        }.joinToString("")
        val expected: Long = 46656 // 6^6
        assertEquals(expected, tool.largestSeriesProduct(number, n, k))
        assertEquals(expected, tool.largestSeriesProductRecursive(number, n, k))
    }

    @Test
    fun `largestSeriesProduct correct for 1000 digit number`() {
        val n = 1000
        val k = 13
        val number = this.thousandDigits
        val expected = 23_514_624_000
        assertEquals(expected, tool.largestSeriesProduct(number, n, k))
        assertEquals(expected, tool.largestSeriesProductRecursive(number, n, k))
    }

    @Test
    fun `largestSeriesProduct speed`() {
        val n = 1000
        val k = 4
        val number = this.thousandDigits
        val expected = 5832L
        val speeds = mutableListOf<Pair<String, Long>>()
        val solutions = mapOf(
            "Recursive" to tool::largestSeriesProductRecursive,
            "Iterative" to tool::largestSeriesProduct
        )
        for ((name, solution) in solutions) {
            getSpeed(solution, number, n, k).run {
                speeds.add(name to this.second)
                assertEquals(expected, this.first)
            }
        }
        compareSpeed(speeds)
    }
}