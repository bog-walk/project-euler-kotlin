package dev.bogwalk.util.custom

import java.util.concurrent.ArrayBlockingQueue

/**
 * A classic circular buffer implementation of a queue for FIFO management, which uses a bounded
 * blocking queue backed by a fixed-sized array.
 */
class RollingQueue<E>(
    capacity: Int
) : ArrayBlockingQueue<E>(capacity) {

    private var tail: E? = null

    /**
     * Inserts element at the tail of this queue without exceeding capacity, by auto-removing
     * the head if full.
     *
     * The original method would block this action if the queue was full by throwing an
     * IllegalStateException.
     *
     * @return true, always.
     */
    override fun add(element: E): Boolean {
        return if (offer(element)) {
            if (remainingCapacity() == 0) tail = element
            true
        } else {
            poll()
            tail = element
            offer(element)
        }
    }

    /**
     * Retrieves, but does not remove, the tail of this queue, or returns null if the queue is
     * either empty or not full.
     */
    fun peekTail(): E? = tail
}