package dev.bogwalk.util.custom

import dev.bogwalk.util.custom.IntMatrix2D
import dev.bogwalk.util.custom.intMatrixOf
import dev.bogwalk.util.custom.product
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class IntMatrix2DTest {
    @Test
    fun `initial state correct with auto-fill`() {
        val matrix = IntMatrix2D(3, 3)
        val expected = "[ 0 0 0 ]\n" +
                "[ 0 0 0 ]\n" +
                "[ 0 0 0 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun `initial state correct with custom-fill`() {
        val matrix = IntMatrix2D(3, 3) { 100 }
        val expected = "[ 100 100 100 ]\n" +
                "[ 100 100 100 ]\n" +
                "[ 100 100 100 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun `row getter correct`() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[1] = intArrayOf(1, 1, 1)
        }
        val expected = intArrayOf(1, 1, 1)
        assertContentEquals(expected, matrix[1])
    }

    @Test
    fun `row setter correct`() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[1] = intArrayOf(1, 1, 1)
        }
        val expected = "[ 0 0 0 ]\n" +
                "[ 1 1 1 ]\n" +
                "[ 0 0 0 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun `matrix plus int correctly changes all elements`() {
        val matrix = IntMatrix2D(3, 3).apply { this + 4 }
        val expected = "[ 4 4 4 ]\n" +
                "[ 4 4 4 ]\n" +
                "[ 4 4 4 ]"
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun `getDiagonals correct`() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[0] = intArrayOf(1, 1, 1)
            this[1] = intArrayOf(2, 2, 2)
            this[2] = intArrayOf(0, 1, 3)
        }
        val expectedLeading = intArrayOf(1, 2, 3)
        val expectedCounter = intArrayOf(1, 2, 0)
        val actual = matrix.getDiagonals()
        assertContentEquals(expectedLeading, actual[0])
        assertContentEquals(expectedCounter, actual[1])
    }

    @Test
    fun `transpose correct`() {
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

    @Test
    fun `iterator implemented correctly`() {
        val matrix = IntMatrix2D(3, 3).apply {
            this[0] = intArrayOf(1, 2, 3)
            this[1] = intArrayOf(4, 5, 6)
            this[2] = intArrayOf(7, 8, 9)
        }
        var actual = ""
        for (row in matrix) {
            actual += "Row ${row.contentToString()} "
        }
        val expected = "Row [1, 2, 3] Row [4, 5, 6] Row [7, 8, 9] "
        assertEquals(expected, actual)
    }

    @Test
    fun `clip returns null with invalid input`() {
        val matrix = IntMatrix2D(3, 3)
        assertNull(matrix.clip(0, 0, 5)) // too large
        assertNull(matrix.clip(4, 5, 2)) // out of bounds start
        assertNull(matrix.clip(1, 1, 3)) // out of bounds end
    }

    @Test
    fun `clip returns a new matrix with valid input`() {
        val matrix = IntMatrix2D(4, 4).apply {
            this[0] = intArrayOf(1, 2, 3, 4)
            this[1] = intArrayOf(4, 5, 6, 7)
            this[2] = intArrayOf(7, 8, 9, 9)
        }
        val expected = "[ 6 7 ]\n" +
                "[ 9 9 ]"
        val actual = matrix.clip(1, 2, 2)
        assertEquals(expected, actual.toString())
    }

    @Test
    fun `top-level function creates new matrix`() {
        val array = Array(2) { r -> IntArray(4) { c -> r * c } }
        val expected = "[ 0 0 0 0 ]\n" +
                "[ 0 1 2 3 ]"
        val actual = intMatrixOf(array)
        assertInstanceOf(IntMatrix2D::class.java, actual)
        assertEquals(expected, actual.toString())
    }

    @Test
    fun `product extension function of IntArray correct`() {
        val matrix = IntMatrix2D(3, 3).apply { this + 2 }
        for (row in matrix.iterator()) {
            assertEquals(8, row.product())
        }
    }
}