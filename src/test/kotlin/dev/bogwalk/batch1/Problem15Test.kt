package dev.bogwalk.batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigInteger

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LatticePathsTest {
    private val tool = LatticePaths()
    private lateinit var lattice: Array<LongArray>

    @BeforeAll
    fun setUp() {
        lattice = tool.latticePathRoutesBFS(500, 500)
    }

    @ParameterizedTest(name="{0}x{1} grid = {2}")
    @CsvSource(
        // lower constraints
        "1, 1, 2", "2, 2, 6", "3, 3, 20",
        // mid constraints
        "20, 20, 846527861", "100, 100, 407336795",
        // upper constraints
        "200, 200, 587893473", "400, 400, 358473912",
        // square grid
        "3, 2, 10", "9, 500, 663599170", "31, 399, 569249261"
    )
    fun `latticePathRoutes correct`(n: Int, m: Int, expected: String) {
        assertEquals(BigInteger(expected), tool.latticePathRoutes(n, m))
        assertEquals(expected.toLong(), lattice[n][m])
    }
}