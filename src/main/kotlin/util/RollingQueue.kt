package util

import java.util.concurrent.ArrayBlockingQueue

class RollingQueue<E>(
    capacity: Int
) : ArrayBlockingQueue<E>(capacity) {

    override fun add(element: E): Boolean {
        return try {
            super.add(element)
        } catch (e: IllegalStateException) {
            poll()
            super.add(element)
        }
    }

}