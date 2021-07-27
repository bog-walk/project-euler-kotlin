package util

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class IntMatrix2DTest {
    @Test
    fun testInitialState_default() {
        val matrix = IntMatrix2D(3, 3)
        val expected = "[ 0 0 0 ]\n" +
                "[ 0 0 0 ]\n" +
                "[ 0 0 0 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testInitialState_customPrefill() {
        val matrix = IntMatrix2D(3, 3) { 100 }
        val expected = "[ 100 100 100 ]\n" +
                "[ 100 100 100 ]\n" +
                "[ 100 100 100 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testGet() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[1] = intArrayOf(1, 1, 1)
        }
        val expected = "[ 0 0 0 ]\n" +
                "[ 1 1 1 ]\n" +
                "[ 0 0 0 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testPlus() {
        val matrix = IntMatrix2D(3, 3).apply { this + 4 }
        val expected = "[ 4 4 4 ]\n" +
                "[ 4 4 4 ]\n" +
                "[ 4 4 4 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testProduct() {
        val matrix = IntMatrix2D(3, 3).apply { this + 2 }
        for (row in matrix.iterator()) {
            assertEquals(8, matrix.product(row))
        }
    }

    @Test
    fun testGetDiagonals() {
        val leading = intArrayOf(1, 2, 3)
        val counter = intArrayOf(1, 2, 0)
        val matrix = IntMatrix2D(3, 3).apply {
            this[0] = intArrayOf(1, 1, 1)
            this[1] = intArrayOf(2, 2, 2)
            this[2] = intArrayOf(0, 1, 3)
        }
        val actual = matrix.getDiagonals()
        assertTrue(leading.contentEquals(actual[0]))
        assertTrue(counter.contentEquals(actual[1]))
    }

    @Test
    fun testTranspose() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[0] = intArrayOf(1, 2, 3)
            this[1] = intArrayOf(4, 5, 6)
            this[2] = intArrayOf(7, 8, 9)
        }
        val expected = "[ 1 4 7 ]\n" +
                "[ 2 5 8 ]\n" +
                "[ 3 6 9 ]"
        val actual = matrix.transpose()
        assertEquals(expected, actual.toString())
    }
}