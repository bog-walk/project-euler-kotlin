package dev.bogwalk.util.custom

/**
 * Custom class that creates a 2-dimensional matrix of Integers, facilitating easy actions like
 * addition, row setting/getting, transposition, getting diagonals, and slicing/clipping. This
 * class is meant to mimic Python's native 2D matrix. Currently only works for square matrices.
 */
class IntMatrix2D(
    private val rows: Int,
    private val cols: Int,
    private val op: (() -> Int)? = null
) : Iterable<IntArray> {
    private val matrix = Array(rows) { IntArray(cols) { op?.invoke() ?: 0 } }
    val size: Int = rows

    override fun toString(): String {
        val output = StringBuilder()
        matrix.forEachIndexed { i, row ->
            output.append(row.joinToString(" ", "[ ", " ]"))
            if (i < rows - 1) output.append("\n")
        }
        return output.toString()
    }

    operator fun get(j: Int): IntArray {
        return matrix[j]
    }

    operator fun set(i: Int, value: IntArray): Boolean {
        if (value.size != cols) return false
        matrix[i] = value
        return true
    }

    /**
     * Adds [n] to every element in the 2D matrix.
     */
    operator fun plus(n: Int) {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                matrix[i][j] += n
            }
        }
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

    fun transpose(): IntMatrix2D {
        val transposed = IntMatrix2D(cols, rows)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposed[j][i] = matrix[i][j]
            }
        }
        return transposed
    }

    /**
     * Returns custom class that extends the iterator interface to allow sequential access of each
     * 2D matrix row, not of the individual elements within each row. As each row is an IntArray,
     * each row already has a built-in iterator().
     */
    override fun iterator(): Iterator<IntArray> {
        return IntMatrix2DIterator(rows, matrix)
    }

    /**
     * Returns a smaller nested square matrix of the provided [size], if found.
     */
    fun clip(startX: Int, startY: Int, size: Int): IntMatrix2D? {
        if (size == 0 || size > rows || size > cols) return null
        if (startX !in 0 until rows - 1 || startY !in 0 until cols - 1) return null
        val endXIncl = startX + size - 1
        val endYIncl = startY + size - 1
        if (endXIncl > rows - 1 || endYIncl > cols - 1) return null
        val clip = matrix.sliceArray(startX..endXIncl).apply {
            forEachIndexed { i, row ->
                this[i] = row.sliceArray(startY..endYIncl)
            }
        }
        return intMatrixOf(clip)
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

fun intMatrixOf(grid: Array<IntArray>): IntMatrix2D {
    val matrix = IntMatrix2D(grid.size, grid[0].size)
    grid.forEachIndexed { i, row ->
        matrix[i] = row
    }
    return matrix
}

fun IntArray.product(): Int {
    return reduce { acc, n -> acc * n }
}