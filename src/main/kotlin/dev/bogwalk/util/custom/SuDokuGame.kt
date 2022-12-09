package dev.bogwalk.util.custom

/**
 * A class representing a Su Doku puzzle grid, with each cell value stored as a Set, so that
 * options can be iterated over until a final value remains.
 */
class SuDokuGame(input: List<String>) {
    private var puzzleCells: Array<Array<Set<Int>>>
    private val hasUnfilledCells: Boolean
        get() = puzzleCells.flatten().any { it.size != 1 }

    init {
        require(input.size == 9) { "Su Doku grid requires 9 rows" }
        require(input.all { it.length == 9 && it.all { ch -> ch.isDigit() } }) {
            "Input contains invalid characters"
        }

        puzzleCells = Array(9) { r ->
            Array(9) { c ->
                val digit = input[r][c].digitToInt()
                if (digit != 0) setOf(digit) else setOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
            }
        }
    }

    fun getGrid(withOptions: Boolean = false): List<String> {
        return if (withOptions) {
            puzzleCells.map { it.joinToString("") }
        } else {
            puzzleCells.map { it.joinToString("") { s ->
                if (s.size == 1) s.single().toString() else "0"
            } }
        }
    }

    /**
     * Solves puzzle by attempting to reduce all possible values, until no changes are made, then
     * a guess is made. So few puzzles make it to the guess stage, but the latter could
     * potentially be optimised by finding the first unsolved cell with the least options.
     */
    fun solve(): Boolean {
        val (wasUpdated, isValid) = reduce()
        if (!isValid) return false
        if (!wasUpdated) {
            val ogVersion = puzzleCells.map { it.toList() }
            for (row in 0..8) {
                for (col in 0..8) {
                    if (puzzleCells[row][col].size == 1) continue
                    val guesses = puzzleCells[row][col]
                    for (guess in guesses) {
                        puzzleCells[row][col] = setOf(guess)
                        clearDigits(row, col, setOf(guess))
                        if (solve() && puzzleCells.all { it.toSet().size == 9 }) {
                            return true
                        }
                        puzzleCells = Array(9) { r -> Array(9) { c-> ogVersion[r][c] } }
                    }
                }
            }
        }
        return true
    }

    fun reduce(): Pair<Boolean, Boolean> {
        while (hasUnfilledCells) {
            var updated = false
            for (row in 0..8) {
                for (col in 0..8) {
                    if (puzzleCells[row][col].size == 1) continue
                    val filtered = puzzleCells[row][col].gridFilter(row, col)
                    if (filtered.isEmpty()) return updated to false
                    puzzleCells[row][col] = filtered
                    if (filtered.size == 1) {
                        updated = true
                        clearDigits(row, col, filtered)
                    }
                }
            }
            if (!updated) updated = clearByAssumption()
            if (!updated) updated = clearByPairAssumption()
            if (!updated) return false to true
        }
        return true to true
    }

    /**
     * For situations when potential values in a box exist on a single column/row, thereby
     * assuring that that column/row must contain the value. This assumption can be used to reduce
     * set options in the corresponding column/row outside the box.
     */
    private fun clearByAssumption(): Boolean {
        var changed = false

        for (boxI in 0..8) {
            val (topRow, topCol) = boxI / 3 * 3 to boxI % 3 * 3
            val rowOptions = List(3) {
                puzzleCells[topRow + it].slice(topCol..topCol + 2).filter { s -> s.size > 1 }
            }
            val colOptions = List(3) {
                listOf(puzzleCells[topRow][topCol + it], puzzleCells[topRow+1][topCol + it],
                    puzzleCells[topRow+2][topCol + it]).filter { s -> s.size > 1 }
            }
            for ((i, row) in rowOptions.withIndex()) {
                if (row.size > 1) {
                    val options = row.reduce(Set<Int>::union)
                    val others = ((topRow..topRow + 2).toSet() - (topRow + i))
                        .flatMap { rowOptions[it % 3] }
                    val outliers = if (others.isEmpty()) options else options - others.reduce(Set<Int>::union)
                    if (outliers.isEmpty()) continue
                    if (clearDigits(
                            topRow + i, topCol, outliers, clearBox = false, clearCol = false
                        )) changed = true
                }
            }
            for ((i, col) in colOptions.withIndex()) {
                if (col.size > 1) {
                    val options = col.reduce(Set<Int>::union)
                    val others = ((topCol..topCol + 2).toSet() - (topCol + i))
                        .flatMap { colOptions[it % 3] }
                    val outliers = if (others.isEmpty()) options else options - others.reduce(Set<Int>::union)
                    if (outliers.isEmpty()) continue
                    if (clearDigits(
                            topRow, topCol + i, outliers, clearBox = false, clearRow = false
                        )) changed = true
                }
            }
        }

        return changed
    }

    /**
     * For situations when potential values in a box exist as a pair; i.e. choosing one in a
     * cell means the other cell must contain the other. If 2 cells have identical sets
     * containing only 2 values, these values can be filtered from all other cells in the box.
     * Could then be followed by another call to clearByAssumption() but this eventually occurs.
     */
    private fun clearByPairAssumption(): Boolean {
        var changed = false

        for (boxI in 0..8) {
            val (topRow, topCol) = boxI / 3 * 3 to boxI % 3 * 3
            val emptyPairCells = List(9) { puzzleCells[topRow + it / 3][topCol + it % 3] }
                .filter { it.size == 2 }
            for (pair in emptyPairCells.distinct()) {
                if (emptyPairCells.count { it == pair } == 2) {
                    for (i in 0..8) {
                        val cell = puzzleCells[topRow + i / 3][topCol + i % 3]
                        if (cell.size == 1 || cell == pair) continue
                        val cleared = cell - pair
                        if (cleared.size < cell.size) {
                            puzzleCells[topRow + i / 3][topCol + i % 3] = cleared
                            changed = true
                            if (cleared.size == 1) {
                                clearDigits(topRow + i / 3, topCol + i % 3, cleared)
                            }
                        }
                    }
                }
            }
        }

        return changed
    }

    /**
     * Accesses the row, column, and box cells associated with a cell at [rowI][colI] and
     * performs 2 actions:
     *      - Reduces the set of possible values based on already filled cell values.
     *      - Checks if the newly reduced set contains a value that is not present in any of the
     *      other cells for each group. This will be returned as a priority; otherwise, the
     *      reduced set will be returned.
     */
    private fun Set<Int>.gridFilter(rowI: Int, colI: Int): Set<Int> {
        val (rowF, rowE) = puzzleCells[rowI]
            .filterIndexed { i, _ -> i != colI }
            .partition { it.size == 1 }
        val (colF, colE) = List(9) {
            if (it != rowI) puzzleCells[it][colI] else emptySet()
        }.partition { it.size == 1 }
        val boxValues = mutableListOf<Set<Int>>()
        val (topRow, topCol) = rowI / 3 * 3 to colI / 3 * 3
        for (row in topRow..topRow + 2) {
            if (row == rowI) {
                val others = (topCol..topCol + 2).toSet() - colI
                boxValues.addAll(others.map { puzzleCells[row][it] })
            } else {
                boxValues.addAll(puzzleCells[row].slice(topCol..topCol + 2))
            }
        }
        val (boxF, boxE) = boxValues.partition { it.size == 1 }

        val reduced = this - (boxF.flatten() union rowF.flatten() union colF.flatten())

        for (group in listOf(boxE.flatten(), rowE.flatten(), colE.flatten())) {
            val single = reduced - group.toSet()
            if (single.size == 1) return single
        }
        return reduced
    }

    /**
     * Supplementary action that clears the value of a newly filled cell from all associated row,
     * column, and box cells. Speed has not been compared, but introducing this second step
     * reduced the overall amount of iterations over the entire puzzle grid.
     */
    private fun clearDigits(
        rowI: Int, colI: Int, digits: Set<Int>,
        clearBox: Boolean = true, clearRow: Boolean = true, clearCol: Boolean = true
    ): Boolean {
        var changed = false
        val (topRow, topCol) = rowI / 3 * 3 to colI / 3 * 3
        val boxRowRange = topRow..topRow + 2
        val boxColRange = topCol..topCol + 2
        for (i in 0..8) {
            var cleared: Set<Int>
            if (clearBox) {
                if (puzzleCells[i / 3 + topRow][i % 3 + topCol].size > 1) {
                    cleared = puzzleCells[i / 3 + topRow][i % 3 + topCol] - digits
                    puzzleCells[i / 3 + topRow][i % 3 + topCol] = cleared
                    if (cleared.size == 1) {
                        changed = true
                        clearDigits(i / 3 + topRow, i % 3 + topCol, cleared)
                    }
                }
            }
            if (clearRow) {
                if (i !in boxColRange && puzzleCells[rowI][i].size > 1) {
                    cleared = puzzleCells[rowI][i] - digits
                    puzzleCells[rowI][i] = cleared
                    if (cleared.size == 1) {
                        changed = true
                        clearDigits(rowI, i, cleared)
                    }
                }
            }
            if (clearCol) {
                if (i !in boxRowRange && puzzleCells[i][colI].size > 1) {
                    cleared = puzzleCells[i][colI] - digits
                    puzzleCells[i][colI] = cleared
                    if (cleared.size == 1) {
                        changed = true
                        clearDigits(i, colI, cleared)
                    }
                }
            }
        }
        return changed
    }
}