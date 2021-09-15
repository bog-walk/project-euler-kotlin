package batch3

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NonAbundantSumsTest {
    private val tool = NonAbundantSums()

    @Test
    fun testIsAbundant_allTrue() {
        val nums = listOf(12, 18, 20, 24, 70, 104, 120, 945)
        nums.forEach { n ->
            assertTrue { tool.isAbundant(n) }
        }
    }

    @Test
    fun testIsAbundant_allFalse() {
        val nums = listOf(6, 9, 21, 43, 86, 115)
        nums.forEach { n ->
            assertFalse { tool.isAbundant(n) }
        }
    }

    @Test
    fun testIsSumOfAbundants_allTrue() {
        val nums = listOf(24, 110, 158, 234, 957, 20162, 28122, 28123, 28124, 100000)
        nums.forEach { n ->
            assertTrue { tool.isSumOfAbundants(n) }
        }
    }

    @Test
    fun testIsSumOfAbundants_allFalse() {
        val nums = listOf(0, 10, 12, 13, 27, 49, 121, 20161)
        nums.forEach { n ->
            assertFalse { tool.isSumOfAbundants(n) }
        }
    }

    @Test
    fun testAllIntegersExpressed() {
        val cannotBeExpressed = mutableListOf<Int>()
        for (n in 20162..28123) {
            if (!tool.isSumOfAbundants(n)) {
                cannotBeExpressed.add(n)
            }
        }
        assertTrue { cannotBeExpressed.isEmpty() }
    }
}