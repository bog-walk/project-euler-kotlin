package util.search

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReusableTest {
    @Test
    fun `binarySearch returns false if collection empty`() {
        val nums = emptyList<Int>()
        val target = 1
        assertFalse { binarySearch(target, nums) }
    }

    @Test
    fun `binarySearch correct for collection with 1 element`() {
        val nums = listOf(100)
        val targetA = 100
        val targetB = 1000
        assertTrue { binarySearch(targetA, nums) }
        assertFalse { binarySearch(targetB, nums) }
    }

    @Test
    fun `binarySearch returns true for elements in large collection`() {
        val nums = List(1001) { it }
        val targets = listOf(0, 1000, 500, 43, 267, 681, 800)
        targets.forEach { target ->
            assertTrue { binarySearch(target, nums) }
        }
    }

    @Test
    fun `binarySearch returns false for elements not in large collection`() {
        val nums = List(1001) { it }.filter { it % 2 == 0 }
        val targets = listOf(-1, 1, 11, 33, 139, 431, 673, 805, 999, 2000)
        targets.forEach { target ->
            assertFalse { binarySearch(target, nums) }
        }
    }
}