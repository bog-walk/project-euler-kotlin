package util

class IntMatrix2D(
    val rows: Int,
    val cols: Int,
    val op: (() -> Int)? = null
) : Iterable<IntArray> {
    private val matrix = Array(rows) { IntArray(cols) { op?.invoke() ?: 0 } }

    override fun toString(): String {
        val output = StringBuilder()
        matrix.forEachIndexed { i, row ->
            output.append(row.joinToString(" ", "[ ", " ]"))
            if (i < rows - 1) output.append("\n")
        }
        return output.toString()
    }

    operator fun plus(n: Int) {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                matrix[i][j] += n
            }
        }
    }

    fun product(row: IntArray): Int {
        return row.reduce { acc, n -> acc * n }
    }

    fun getDiagonals(): List<IntArray> {
        if (rows != cols) return emptyList()
        val leading = IntArray(cols)
        val counter = IntArray(cols)
        for ((step, i) in (0 until rows).withIndex()) {
            leading[i] = matrix[i][i]
            counter[i] = matrix[i][cols - 1 - step]
        }
        return listOf(leading, counter)
    }

    // To iterate through rows, not individual elements
    override fun iterator(): Iterator<IntArray> {
        return IntMatrix2DIterator(rows, matrix)
    }

    fun transpose(): IntMatrix2D {
        val transposed = IntMatrix2D(cols, rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposed[j][i] = matrix[i][j]
            }
        }
        return transposed
    }

    operator fun get(j: Int): IntArray {
        return matrix[j]
    }

    operator fun set(i: Int, value: IntArray): Boolean {
        if (value.size != cols) return false
        matrix[i] = value
        return true
    }
}

class IntMatrix2DIterator(
    private val rows: Int,
    private val matrix: Array<IntArray>
) : Iterator<IntArray> {
    private var initRow = 0

    override fun hasNext(): Boolean {
        return initRow <= rows - 1
    }

    override fun next(): IntArray {
        return matrix[initRow++]
    }
}