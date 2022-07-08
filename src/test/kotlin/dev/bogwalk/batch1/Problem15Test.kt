package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigInteger

internal class LatticePathsTest {
    private val tool = LatticePaths()

    @ParameterizedTest(name="{0}x{1} grid = {2}")
    @CsvSource(
        // lower constraints
        "1, 1, 2", "2, 2, 6", "3, 3, 20",
        // normal values
        "20, 20, 846527861",
        // square grid
        "3, 2, 10"
    )
    fun `latticePathRoutes correct`(n: Int, m: Int, expected: String) {
        assertEquals(BigInteger(expected), tool.latticePathRoutes(n, m))
    }
}