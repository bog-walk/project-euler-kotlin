package util.custom

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class RollingQueueTest {
    @Test
    fun testNormalMethods_withinCapacity() {
        val queue = RollingQueue<Int>(10).apply {
            addAll(arrayOf(1,2,3,4,5,6))
            poll()
        }
        val expected = "[2, 3, 4, 5, 6]"
        assertEquals(5, queue.size)
        assertEquals(2, queue.peek())
        assertEquals(expected, queue.toString())
    }

    @Test
    fun testPeekTail_lessThanCapacity() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(3) {
                add(it)
            }
        }
        assertNull(queue.peekTail())
    }

    @Test
    fun testPeekTail_withinCapacity() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(5) {
                add(it)
            }
        }
        assertEquals(4, queue.peekTail())
    }

    @Test
    fun testPeekTail_beyondCapacity() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(8) {
                add(it)
            }
        }
        assertEquals(7, queue.peekTail())
    }

    @Test
    fun testAdd_withinCapacity() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(5) {
                add(it)
            }
        }
        val expected = "[0, 1, 2, 3, 4]"
        assertEquals(5, queue.size)
        assertEquals(expected, queue.toString())
    }

    @Test
    fun testAdd_oneBeyondCapacity() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(5) {
                add(it)
            }
        }
        val expected = "[1, 2, 3, 4, 5]"
        assertDoesNotThrow { queue.add(5) }
        assertEquals(expected, queue.toString())
    }

    @Test
    fun testAdd_manyBeyondCapacity() {
        val queue = RollingQueue<Int>(8).apply {
            repeat(8) {
                add(it)
            }
            repeat(4) {
                add((it + 1) * 100)
            }
        }
        val expected = "[4, 5, 6, 7, 100, 200, 300, 400]"
        assertEquals(expected, queue.toString())
    }
}