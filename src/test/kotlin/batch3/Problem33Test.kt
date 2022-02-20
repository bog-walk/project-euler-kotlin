package batch3

import util.tests.compareSpeed
import util.tests.getSpeed
import kotlin.test.*

internal class DigitCancellingFractionsTest {
    private val tool = DigitCancellingFractions()

    @Test
    fun `isReducedEquivalent returns true for valid fractions`() {
        val fractions = listOf(16 to 64, 26 to 65, 1249 to 9992, 4999 to 9998)
        val k = 1
        fractions.forEach { (num, denom) ->
            val digits = num.toString().length
            assertTrue { tool.isReducedEquivalent(digits, num, denom, k) }
        }
    }

    @Test
    fun `isReducedEquivalent returns false for invalid fractions`() {
        val fractions = listOf(11 to 19, 47 to 71, 328 to 859, 8777 to 7743)
        val k = 1
        fractions.forEach { (num, denom) ->
            val digits = num.toString().length
            assertFalse { tool.isReducedEquivalent(digits, num, denom, k) }
        }
    }

    @Test
    fun `isReducedEquivalent correct for all K`() {
        val fractions = listOf(16 to 64, 166 to 664, 1666 to 6664)
        fractions.forEach { (num, denom) ->
            val digits = num.toString().length
            for (k in 1..3) {
                if (k >= digits) break
                assertTrue { tool.isReducedEquivalent(digits, num, denom, k) }
            }
        }
    }

    @Test
    fun `findNonTrivials correct for K = 1`() {
        val expected = listOf(
            listOf(16 to 64, 19 to 95, 26 to 65, 49 to 98),
            listOf(166 to 664, 199 to 995, 217 to 775, 249 to 996, 266 to 665, 499 to 998),
            listOf(
                1249 to 9992, 1666 to 6664, 1999 to 9995, 2177 to 7775,
                2499 to 9996, 2666 to 6665, 4999 to 9998
            )
        )
        for (n in 2..4) {
            assertContentEquals(expected[n - 2], tool.findNonTrivialsBrute(n))
            assertContentEquals(expected[n - 2], tool.findNonTrivials(n))
        }
    }

    @Test
    fun `findNonTrivials correct for K = 2`() {
        val n = 3
        val k = 2
        val expected = listOf(166 to 664, 199 to 995, 266 to 665, 484 to 847, 499 to 998)
        assertContentEquals(expected, tool.findNonTrivialsBrute(n, k))
        assertContentEquals(expected, tool.findNonTrivials(n, k))
    }

    @Test
    fun `findNonTrivials speed`() {
        val n = 4
        val k = 1
        // sums of numerators & denominators
        val expected = 17255 to 61085
        val solutions = mapOf(
            "Brute" to tool::findNonTrivialsBrute, "Optimised" to tool::findNonTrivials
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        val results = mutableListOf<List<Pair<Int, Int>>>()
        for((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        results.forEach { result ->
            val (numerators, denominators) = result.unzip()
            assertEquals(expected.first, numerators.sum())
            assertEquals(expected.second, denominators.sum())
        }
    }

    @Test
    fun `PE problem correct`() {
        assertEquals(100, tool.productOfNonTrivials())
    }

    @Test
    fun `getCancelledCombos correct`() {
        val nums = listOf("9919", "1233", "1051", "5959")
        val toCancel = listOf(
            listOf('9', '9'), listOf('1', '2', '3'), listOf('1', '5'), listOf('9')
        )
        val expected = listOf(setOf(19, 91), setOf(3), setOf(1, 10), setOf(559, 595))
        nums.forEachIndexed { i, n ->
            assertEquals(expected[i], tool.getCancelledCombos(n, toCancel[i]))
        }
    }

    @Test
    fun `HR problem correct for K = 1`() {
        val k = 1
        val expected = listOf(110 to 322, 77262 to 163_829)
        for (n in 2..3) {
            assertEquals(expected[n - 2], tool.sumOfNonTrivialsBrute(n, k))
            assertEquals(expected[n - 2], tool.sumOfNonTrivialsGCD(n, k))
        }
    }

    @Test
    fun `HR problem correct for K = 2`() {
        val k = 2
        val expected = listOf(7429 to 17305, 3_571_225 to 7_153_900)
        for (n in 3..4) {
            assertEquals(expected[n - 3], tool.sumOfNonTrivialsBrute(n, k))
            assertEquals(expected[n - 3], tool.sumOfNonTrivialsGCD(n, k))
        }
    }

    @Test
    fun `HR problem correct for K = 3`() {
        val n = 4
        val expected = 255_983 to 467_405
        assertEquals(expected, tool.sumOfNonTrivialsBrute(n, k=3))
        assertEquals(expected, tool.sumOfNonTrivialsGCD(n, k=3))
    }

    @Test
    fun `HR problem speed`() {
        val n = 4
        val k = 1
        val expected = 12_999_936 to 28_131_911
        val solutions = mapOf(
            "Brute" to tool::sumOfNonTrivialsBrute, "GCD" to tool::sumOfNonTrivialsGCD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n, k).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}