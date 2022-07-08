package dev.bogwalk.util.custom

import dev.bogwalk.util.custom.RollingQueue
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class RollingQueueTest {
    @Test
    fun `initial state of un-filled instance correct`() {
        val queue = RollingQueue<Int>(10).apply {
            addAll(arrayOf(2,3,4,5,6))
        }
        val expected = "[2, 3, 4, 5, 6]"
        assertEquals(5, queue.size)
        assertEquals(2, queue.peek())
        assertNull(queue.peekTail())
        assertEquals(expected, queue.toString())
    }

    @Test
    fun `initial state of filled instance correct`() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(5) {
                add(it)
            }
        }
        val expected = "[0, 1, 2, 3, 4]"
        assertEquals(5, queue.size)
        assertEquals(0, queue.peek())
        assertEquals(4, queue.peekTail())
        assertEquals(expected, queue.toString())
    }

    @Test
    fun `add beyond capacity correct`() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(8) {
                add(it)
            }
        }
        assertEquals(5, queue.size)
        assertEquals(3, queue.peek())
        assertEquals(7, queue.peekTail())
    }

    @Test
    fun `add beyond capacity does not block`() {
        val queue = RollingQueue<Int>(5).apply {
            repeat(5) {
                add(it)
            }
        }
        val expected = "[1, 2, 3, 4, 5]"
        assertDoesNotThrow { queue.add(5) }
        assertEquals(expected, queue.toString())
    }
}