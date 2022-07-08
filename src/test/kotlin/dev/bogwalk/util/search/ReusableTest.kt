package dev.bogwalk.util.search

import dev.bogwalk.util.search.binarySearch
import dev.bogwalk.util.search.binarySearchManual
import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReusableTest {
    @Test
    fun `binarySearch returns false if collection empty`() {
        val nums = emptyList<Int>()
        val target = 1
        assertFalse { binarySearchManual(target, nums) }
        assertFalse { binarySearch(target, nums) }
    }

    @Test
    fun `binarySearch correct for collection with 1 element`() {
        val nums = listOf(100)
        val targetA = 100
        val targetB = 1000
        assertTrue { binarySearchManual(targetA, nums) }
        assertTrue { binarySearch(targetA, nums) }
        assertFalse { binarySearchManual(targetB, nums) }
        assertFalse { binarySearch(targetB, nums) }
    }

    @Test
    fun `binarySearch returns true for elements in large collection`() {
        val nums = List(1001) { it }
        val targets = listOf(0, 1000, 500, 43, 267, 681, 800)
        targets.forEach { target ->
            assertTrue { binarySearchManual(target, nums) }
            assertTrue { binarySearch(target, nums) }
        }
    }

    @Test
    fun `binarySearch returns false for elements not in large collection`() {
        val nums = List(1001) { it }.filter { it % 2 == 0 }
        val targets = listOf(-1, 1, 11, 33, 139, 431, 673, 805, 999, 2000)
        targets.forEach { target ->
            assertFalse { binarySearchManual(target, nums) }
            assertFalse { binarySearch(target, nums) }
        }
    }

    @Test
    fun `binarySearch speed`() {
        val nums = List(10_001) { it }
        val target = 9998
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(::binarySearchManual, target, nums).run {
            speeds.add("Manual" to this.second)
            assertTrue { this.first }
        }
        getSpeed(::binarySearch, target, nums).run {
            speeds.add("Built-in" to this.second)
            assertTrue { this.first }
        }
        compareSpeed(speeds)
    }
}