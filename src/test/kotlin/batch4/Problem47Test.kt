package batch4

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class DistinctPrimesFactorsTest {
    private val tool = DistinctPrimesFactors()

    @Test
    fun testConsecutiveDistinctPrimes_k2() {
        val nums = listOf(20, 100)
        val expected = listOf(
            listOf(14, 20),
            listOf(
                14, 20, 21, 33, 34, 35, 38, 39, 44, 45, 50, 51, 54, 55, 56,
                57, 62, 68, 74, 75, 76, 85, 86, 87, 91, 92, 93, 94, 95, 98, 99
            )
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k=2))
        }
    }

    @Test
    fun testConsecutiveDistinctPrimes_k3() {
        val nums = listOf(644, 1000)
        val expected = listOf(listOf(644), listOf(644, 740, 804, 986)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k=3))
        }
    }

    @Test
    fun testConsecutiveDistinctPrimes_k4() {
        val nums = listOf(10_000, 100_000, 300_000)
        val expected = listOf(
            emptyList(), emptyList(), listOf(134043, 238203, 253894, 259368)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k=4))
        }
    }

    @Test
    fun testFirst4DistinctPrimes() {
        val expected = 134043
        assertEquals(expected, tool.first4DistinctPrimes())
    }
}