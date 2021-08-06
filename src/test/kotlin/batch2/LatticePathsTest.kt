package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class LatticePathsTest {
    // Note that the 20x20 grid result without reduction is 137846528820
    @ParameterizedTest(name="{0}x{1} grid = {2}")
    @CsvSource(
        "1, 1, 2", "2, 2, 6", "3, 2, 10",
        "3, 3, 20", "20, 20, 846527861"
    )
    fun testLatticePaths(n: Int, m: Int, expected: Int) {
        val tool = LatticePaths()
        assertEquals(expected, tool.latticePathsReduced(n, m))
    }
}