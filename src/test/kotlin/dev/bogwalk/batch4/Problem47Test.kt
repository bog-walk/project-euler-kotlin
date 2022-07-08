package dev.bogwalk.batch4

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class DistinctPrimesFactorsTest {
    private val tool = DistinctPrimesFactors()

    @Test
    fun `HR problem correct for K equal 2`() {
        val k = 2
        val nums = listOf(20, 100)
        val expected = listOf(
            listOf(14, 20),
            listOf(
                14, 20, 21, 33, 34, 35, 38, 39, 44, 45, 50, 51, 54, 55, 56,
                57, 62, 68, 74, 75, 76, 85, 86, 87, 91, 92, 93, 94, 95, 98, 99
            )
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k))
        }
    }

    @Test
    fun `HR problem correct for K equal 3`() {
        val k = 3
        val nums = listOf(644, 1000)
        val expected = listOf(listOf(644), listOf(644, 740, 804, 986))
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k))
        }
    }

    @Test
    fun `HR problem correct for K equal 4`() {
        val k = 4
        val nums = listOf(10_000, 100_000, 300_000)
        val expected = listOf(
            emptyList(), emptyList(), listOf(134_043, 238_203, 253_894, 259_368)
        )
        for ((i, n) in nums.withIndex()) {
            assertContentEquals(expected[i], tool.consecutiveDistinctPrimes(n, k))
        }
    }

    @Test
    fun `PE problem correct`() {
        val expected = 134_043
        assertEquals(expected, tool.first4DistinctPrimes())
    }
}