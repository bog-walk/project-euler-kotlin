package batch6

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test

internal class TotientPermutationTest {
    private val tool = TotientPermutation()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "100, 21", "200, 21", "300, 291", "400, 291", "500, 291", "1000, 291",
        // mid constraints
        "2900, 2817", "3000, 2991", "5000, 4435", "10000, 4435", "50000, 45421",
        // upper constraints
        "100000, 75841", "1000000, 783169"
    )
    fun `totientPermutation correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.totientPermutation(n))
        assertEquals(expected, tool.totientPermutationRobust(n))
    }

    @Test
    fun `totientPermutation speed`() {
        val n = 10_000_000
        val expected = 8_319_823
        val solutions = mapOf(
            "Original" to tool::totientPermutation, "Robust" to tool::totientPermutationRobust
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }
}