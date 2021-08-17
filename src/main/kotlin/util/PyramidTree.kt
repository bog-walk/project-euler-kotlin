package util

/**
 * A Tree with each node having 2 children, but children that are not
 * along the left or right edge will have 2 parents, i.e. internal children
 * are shared by 2 nodes, with nodeX.rightAdjacent == nodeY.leftAdjacent.
 * This means each level in the tree will have an amount of nodes equivalent
 * to the level number. This Tree is not ordered as for a BinaryTree.
 */
class PyramidNode(
    val value: Int,
    var leftAdjacent: PyramidNode? = null,
    var rightAdjacent: PyramidNode? = null
)

class PyramidTree(levels: Int, vararg elements: Int) {
    lateinit var root: PyramidNode

    init {
        generateTree(levels, *elements)
    }

    private fun generateTree(levels: Int, vararg elements: Int){
        val nodes = elements.map { PyramidNode(it) }
        var index = 0
        root = nodes[index]
        for (level in 1 until levels) {
            repeat(level) {
                nodes[index].leftAdjacent = nodes[index + level]
                nodes[index].rightAdjacent = nodes[index + level + 1]
                index++
            }
        }
    }

    // This will duplicate shared children nodes!
    private fun PyramidNode.drawTree(prefix: StringBuilder, isTail: Boolean, string: StringBuilder): StringBuilder {
        rightAdjacent?.let {
            rightAdjacent!!.drawTree(StringBuilder().append(prefix).append(if (isTail) "|   " else "    "), false, string)
        }
        string.append(prefix).append(if (isTail) "└── " else "┌── ").append(value.toString()).append("\n")
        leftAdjacent?.let {
            leftAdjacent!!.drawTree(StringBuilder().append(prefix).append(if (isTail) "    " else "|   "), true, string)
        }
        return string
    }

    override fun toString(): String {
        return root.drawTree(StringBuilder(), true, StringBuilder()).toString()
    }

    fun maxSumPostOrderTraversal(node: PyramidNode = root): Int {
        val left: Int = node.leftAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        val right: Int = node.rightAdjacent?.run { maxSumPostOrderTraversal(this) } ?: 0
        return maxOf(left, right) + node.value
    }
}