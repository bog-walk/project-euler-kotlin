package util

import java.util.concurrent.ArrayBlockingQueue

class RollingQueue<E>(
    capacity: Int
) : ArrayBlockingQueue<E>(capacity) {

    /**
     * Inserts specified element at tail of queue without
     * exceeding capacity, by removing head if full. Returns
     * true upon success.
     */
    override fun add(element: E): Boolean {
        return try {
            super.add(element)
        } catch (e: IllegalStateException) {
            poll()
            super.add(element)
        }
    }

    /**
     * Retrieves, but does not remove tail of queue,
     * or returns null if queue is empty.
     */
    fun peekTail(): E? {
        var last: E? = null
        for (e in super.iterator()) {
            last = e
        }
        return last
    }

}