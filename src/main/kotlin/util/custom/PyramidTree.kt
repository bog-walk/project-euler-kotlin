package util.custom

/**
 * A Node for the following PyramidTree class.
 *
 * A Node stores an integer value, as well as a reference to its 2 children, if it is not a leaf
 * node. Its sole method, draw_node(), allows all Nodes in a PyramidTree to be drawn recursively.
 */
class PyramidNode(
    val value: Int,
    var leftAdjacent: PyramidNode? = null,
    var rightAdjacent: PyramidNode? = null
) {
    /**
     * Returns a String representation of the node and its children.
     *
     * Note that shared adjacent nodes will be duplicated in this algorithm, as the PyramidTree
     * class is not a proper binary tree.
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
 * A Tree with each node having 2 children except for leaf nodes.
 *
 * A PyramidTree, more appropriately a graph, has every node that is not along the left or right
 * edges having 2 parents, unlike in a standard BinaryTree.
 *
 * i.e. Internal children are shared by 2 nodes, with nodeX.rightAdjacent == nodeY.leftAdjacent.
 * Therefore, each level in the tree will have an amount of nodes equivalent to the level number.
 * Also unlike a BinaryTree, nodes in a PyramidTree are not ordered.
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

    /**
     * Post-order traversal visits the left subtree before the right subtree, returning the
     * maximum sum before processing the start node.
     */
    fun maxSumPostOrderTraversal(node: PyramidNode = root): Int {
        val left: Int = node.leftAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        val right: Int = node.rightAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        return maxOf(left, right) + node.value
    }
}