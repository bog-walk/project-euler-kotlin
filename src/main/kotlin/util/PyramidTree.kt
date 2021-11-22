package util

class PyramidNode(
    val value: Int,
    var leftAdjacent: PyramidNode? = null,
    var rightAdjacent: PyramidNode? = null
) {
    /**
     * Generates graph representation of a PyramidTree if first called on root node.
     * Note that shared children nodes will be duplicated!
     */
    fun draw(prefix: StringBuilder, isTail: Boolean, string: StringBuilder): StringBuilder {
        rightAdjacent?.let {
            rightAdjacent!!.draw(
                StringBuilder().append(prefix).append(if (isTail) "|   " else "    "),
                false,
                string
            )
        }
        string
            .append(prefix)
            .append(if (isTail) "└── " else "┌── ")
            .append(value.toString())
            .append("\n")
        leftAdjacent?.let {
            leftAdjacent!!.draw(
                StringBuilder().append(prefix).append(if (isTail) "    " else "|   "),
                true,
                string
            )
        }
        return string
    }
}

/**
 * A Tree (more appropriately a graph) with each node having 2 children except
 * for leaf nodes. Every node that is not along the left or right edge will have
 * 2 parents. i.e. Internal children are shared by 2 nodes,
 * with nodeX.rightAdjacent == nodeY.leftAdjacent.
 * This means each level in the tree will have an amount of nodes equivalent
 * to the level number.
 * This Tree is not ordered as for a BinaryTree.
 */
class PyramidTree(
    levels: Int,
    vararg elements: Int
) {
    val root: PyramidNode = generateTree(levels, *elements)

    private fun generateTree(levels: Int, vararg elements: Int): PyramidNode {
        val nodes = elements.map { PyramidNode(it) }
        var index = 0
        for (level in 1 until levels) {
            repeat(level) {
                nodes[index].leftAdjacent = nodes[index + level]
                nodes[index].rightAdjacent = nodes[index + level + 1]
                index++
            }
        }
        return nodes[0]
    }

    override fun toString(): String {
        return root.draw(StringBuilder(), true, StringBuilder()).toString()
    }

    fun maxSumPostOrderTraversal(node: PyramidNode = root): Int {
        val left: Int = node.leftAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        val right: Int = node.rightAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        return maxOf(left, right) + node.value
    }
}