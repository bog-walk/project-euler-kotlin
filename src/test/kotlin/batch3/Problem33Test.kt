package batch3

import kotlin.system.measureNanoTime
import kotlin.test.*

internal class DigitCancellingFractionsTest {
    private val tool = DigitCancellingFractions()

    @Test
    fun testIsReducedEquivalent_allTrue() {
        val fractions = listOf(16 to 64, 26 to 65, 1249 to 9992, 4999 to 9998)
        fractions.forEach { (num, denom) ->
            val digits = num.toString().length
            assertTrue { tool.isReducedEquivalent(digits, num, denom, 1) }
        }
    }

    @Test
    fun testIsReducedEquivalent_allFalse() {
        val fractions = listOf(11 to 19, 47 to 71, 328 to 859, 8777 to 7743)
        fractions.forEach { (num, denom) ->
            val digits = num.toString().length
            assertFalse { tool.isReducedEquivalent(digits, num, denom, 1) }
        }
    }

    @Test
    fun testIsReducedEquivalent_allK() {
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
    fun testFindNonTrivials_K1() {
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
            val improvedActual = tool.findNonTrivials(n)
            assertEquals(expected[n - 2].size, improvedActual.size)
            improvedActual.forEach { fraction ->
                assertTrue { fraction in expected[n - 2] }
            }
        }
    }

    @Test
    fun testFindNonTrivials_K2() {
        val n = 3
        val k = 2
        val expected = listOf(166 to 664, 199 to 995, 266 to 665, 484 to 847, 499 to 998)
        assertContentEquals(expected, tool.findNonTrivialsBrute(n, k))
        val improvedActual = tool.findNonTrivials(n, k)
        assertEquals(expected.size, improvedActual.size)
        improvedActual.forEach { fraction ->
            assertTrue { fraction in expected }
        }
    }

    @Test
    fun testFindNonTrivials_speedComparison() {
        val n = 4
        // sums of numerators & denominators
        val expected = 17255 to 61085
        val ansBrute: List<Pair<Int, Int>>
        val ansOptimised: List<Pair<Int, Int>>
        val timeBrute = measureNanoTime {
            ansBrute = tool.findNonTrivialsBrute(n, k=1)
        }
        val timeOptimised = measureNanoTime {
            ansOptimised = tool.findNonTrivials(n, k=1)
        }
        println("Brute solution took: ${timeBrute / 1_000_000}ms\n" +
                "Optimised solution took: ${timeOptimised / 1_000_000}ms")
        val (bruteN, bruteD) = ansBrute.unzip()
        val (optN, optD) = ansOptimised.unzip()
        assertEquals(expected.first, bruteN.sum())
        assertEquals(expected.first, optN.sum())
        assertEquals(expected.second, bruteD.sum())
        assertEquals(expected.second, optD.sum())
    }

    @Test
    fun testProductOfNonTrivials() {
        assertEquals(100, tool.productOfNonTrivials())
    }

    @Test
    fun testGetCancelledCombos() {
        val nums = listOf("9919", "1233", "1051", "5959")
        val toCancel = listOf(
            listOf('9', '9'), listOf('1', '2', '3'),
            listOf('1', '5'), listOf('9')
        )
        val expected = listOf(
            setOf(19, 91), setOf(3), setOf(1, 10), setOf(559, 595)
        )
        nums.forEachIndexed { i, n ->
            assertEquals(expected[i], tool.getCancelledCombos(n, toCancel[i]))
        }
    }

    @Test
    fun testGetCombinations() {
        val nums = listOf("12", "345", "6789")
        val expected = listOf(
            setOf(listOf('1'), listOf('2')),
            setOf(listOf('3', '4'), listOf('3', '5'), listOf('4', '5')),
            setOf(listOf('6', '7', '8'), listOf('6', '7', '9'), listOf('6', '8', '9'), listOf('7', '8', '9'))
        )
        for (k in 1..3) {
            assertEquals(expected[k - 1], tool.getCombinations(nums[k - 1], k=k))
        }
    }

    @Test
    fun testFindSumOfNonTrivials_K1() {
        val expected = listOf(110 to 322, 77262 to 163829)
        for (n in 2..3) {
            assertEquals(expected[n - 2], tool.sumOfNonTrivialsBrute(n, k=1))
            assertEquals(expected[n - 2], tool.sumOfNonTrivialsGCD(n, k=1))
        }
    }

    @Test
    fun testFindSumOfNonTrivials_K2() {
        val expected = listOf(7429 to 17305, 3571225 to 7153900)
        for (n in 3..4) {
            assertEquals(expected[n - 3], tool.sumOfNonTrivialsBrute(n, k=2))
            assertEquals(expected[n - 3], tool.sumOfNonTrivialsGCD(n, k=2))
        }
    }

    @Test
    fun testFindSumOfNonTrivials_K3() {
        val n = 4
        val expected = 255983 to 467405
        assertEquals(expected, tool.sumOfNonTrivialsBrute(n, k=3))
        assertEquals(expected, tool.sumOfNonTrivialsGCD(n, k=3))
    }

    @Test
    fun testFindSumOfNonTrivials_speedComparison() {
        val n = 4
        val expected = 12999936 to 28131911
        val timeBrute = measureNanoTime {
            assertEquals(expected, tool.sumOfNonTrivialsBrute(n, k=1))
        }
        val timeGCD = measureNanoTime {
            assertEquals(expected, tool.sumOfNonTrivialsGCD(n, k=1))
        }
        println("Brute solution took: ${timeBrute / 1_000_000_000}s\n" +
                "GCD solution took: ${timeGCD / 1_000_000_000}s")
    }
}