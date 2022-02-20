package batch4

import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class TriPentHexTest {
    private val tool = TriPentHex()

    @Test
    fun `HR problem correct for triangle-pentagonal combos`() {
        val a = 3
        val b = 5
        val nums = listOf<Long>(2, 10, 10_000, 1_000_000)
        val expected = listOf<List<Long>>(
            listOf(1), listOf(1), listOf(1, 210), listOf(1, 210, 40755)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.commonNumbers(n, a, b))
            assertContentEquals(expected[i], tool.commonNumbersFormula(n, a, b))
        }
    }

    @Test
    fun `HR problem correct for pentagonal-hexagonal combos`() {
        val a = 5
        val b = 6
        val nums = listOf<Long>(2, 100_000, 10_000_000)
        val expected = listOf<List<Long>>(
            listOf(1), listOf(1, 40755), listOf(1, 40755)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.commonNumbers(n, a, b))
            assertContentEquals(expected[i], tool.commonNumbersFormula(n, a, b))
        }
    }

    @Test
    fun `HR problem speed`() {
        val a = 5
        val b = 6
        val n = 200_000_000_000_000
        val expected = listOf(1, 40755, 1_533_776_805, 57_722_156_241_751)
        val solutions = mapOf(
            "Original" to tool::commonNumbers, "Diophantine" to tool::commonNumbersFormula
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, a, b).run {
                speeds.add(name to second)
                assertContentEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `PE problem correct`() {
        val expected = 1_533_776_805L
        assertEquals(expected, tool.nextTripleType())
    }
}