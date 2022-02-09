package util.custom

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class PyramidTreeTest {
    @Test
    fun `tree with only a root element initialises correctly`() {
        val tree = PyramidTree(1, 10)
        assertTrue(tree.root.value == 10)
        assertNull(tree.root.leftAdjacent)
        assertNull(tree.root.rightAdjacent)
        assertEquals(10, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun `tree with only 2 levels initialises correctly`() {
        val tree = PyramidTree(2, 1, 2, 3)
        assertTrue(tree.root.value == 1)
        assertEquals(2, tree.root.leftAdjacent?.value)
        assertEquals(3, tree.root.rightAdjacent?.value)
        assertEquals(4, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun `correctly finds sole route through max-sized tree`() {
        val elements = IntArray(120) { if (it == 100) 99 else 0 }
        val tree = PyramidTree(15, *elements)
        assertEquals(99, tree.maxSumPostOrderTraversal())
    }

    @Test
    fun `correctly finds max route through max-sized tree`() {
        val elements = IntArray(120) { if (it == 108) 11 else if (it == 116) 99 else 1 }
        val tree = PyramidTree(15, *elements)
        assertEquals(113, tree.maxSumPostOrderTraversal())
    }
}