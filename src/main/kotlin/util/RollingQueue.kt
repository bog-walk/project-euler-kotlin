package util

import java.util.concurrent.ArrayBlockingQueue

/**
 * This bounded blocking queue backed by a fixed-sized array is a
 * classic circular buffer implementation of a queue for FIFO. Both enqueue
 * and dequeue are O(1).
 */
class RollingQueue<E>(
    capacity: Int
) : ArrayBlockingQueue<E>(capacity) {

    private var tail: E? = null

    /**
     * Inserts specified element at tail of queue without
     * exceeding capacity, by removing head if full. Returns
     * true upon success. Original method would block if
     * queue was full prior to adding new element. Could use
     * offer() as a condition if wanting to avoid catching
     * exceptions.
     */
    override fun add(element: E): Boolean {
        return try {
            super.add(element)
        } catch (e: IllegalStateException) {
            poll()
            super.add(element)
        } finally {
            if (remainingCapacity() == 0) tail = element
        }
    }

    /**
     * Retrieves, but does not remove tail of queue,
     * or returns null if queue is empty. Tail in this case
     * will also be null if queue is not full.
     */
    fun peekTail(): E? = tail

}