package util

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class PyramidTreeTest {
    @Test
    fun testTree_onlyRoot() {
        val tree = PyramidTree(1, 10)
        assertTrue(tree.root.value == 10)
        assertNull(tree.root.leftAdjacent)
        assertNull(tree.root.rightAdjacent)
        assertEquals(10, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun testTree_twoLevels() {
        val tree = PyramidTree(2, 1, 2, 3)
        assertTrue(tree.root.value == 1)
        assertEquals(2, tree.root.leftAdjacent?.value)
        assertEquals(3, tree.root.rightAdjacent?.value)
        assertEquals(4, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun testTree_multipleLevels() {
        val elements = intArrayOf(3, 7, 4, 2, 4, 6, 8, 5, 9, 3)
        val tree = PyramidTree(4, *elements)
        assertTrue(tree.root.value == 3)
        assertEquals(23, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun testTree_maxLevels_oneRoute() {
        val elements = IntArray(120) { if (it == 100) 99 else 0 }
        val tree = PyramidTree(15, *elements)
        assertEquals(99, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun testTree_maxLevels_twoRoutes() {
        val elements = IntArray(120) { if (it == 108) 11 else if (it == 116) 99 else 1 }
        val tree = PyramidTree(15, *elements)
        assertEquals(113, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun testTree_maxLevels_fullPyramid() {
        val elements = intArrayOf(
            75, 95, 64, 17, 47, 82, 18, 35, 87, 10, 20, 4, 82, 47, 65, 19, 1, 23, 75, 3, 34,
            88, 2, 77, 73, 7, 63, 67, 99, 65, 4, 28, 6, 16, 70, 92, 41, 41, 26, 56, 83, 40,
            80, 70, 33, 41, 48, 72, 33, 47, 32, 37, 16, 94, 29, 53, 71, 44, 65, 25, 43, 91,
            52, 97, 51, 14, 70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57, 91, 71, 52, 38,
            17, 14, 91, 43, 58, 50, 27, 29, 48, 63, 66, 4, 68, 89, 53, 67, 30, 73, 16, 69,
            87, 40, 31, 4, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 4, 23
        )
        val tree = PyramidTree(15, *elements)
        assertEquals(1074, tree.maxSumPostOrderTraversal())
    }
}