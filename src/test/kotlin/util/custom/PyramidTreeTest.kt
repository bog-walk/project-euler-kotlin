package util.custom

import org.junit.jupiter.api.Assertions.*
import util.custom.PyramidTree
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
}