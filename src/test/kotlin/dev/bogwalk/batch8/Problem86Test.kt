package dev.bogwalk.batch8

import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CuboidRouteTest {
    private val tool = CuboidRoute()
    private lateinit var allCounts: LongArray

    @BeforeAll
    fun setup() {
        allCounts = tool.cuboidCountsQuickDraw()
    }

    @ParameterizedTest(name = "M = {0}")
    @CsvSource(
        // lower constraints
        "1, 0", "2, 0", "3, 2", "10, 14", "99, 1975", "100, 2060",
        // mid constraints
        "1999, 1_226_406", "5612, 10_848_941"
    )
    fun `HR problem correct for lower and mid constraints`(m: Int, expected: Long) {
        assertEquals(expected, tool.countDistinctCuboids(m))
        assertEquals(expected, allCounts[m])
    }

    @Test
    fun `HR problem speed for upper-mid constraints`() {
        val m = 10_000
        val expected = 36_553_574L
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        getSpeed(tool::countDistinctCuboids, m).run {
            speeds.add("Original" to second)
            assertEquals(expected, first)
        }
        getSpeed(tool::cuboidCountsQuickDraw).run {
            speeds.add("Quick draw" to second)
            assertEquals(expected, first[m])
        }
        compareSpeed(speeds)
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val expected = listOf(
            100_000 to 4_487_105_091, 200_000 to 18_954_950_955, 400_000 to 79_838_573_329
        )
        for ((m, e) in expected) {
            assertEquals(e, allCounts[m])
        }
    }

    @Test
    fun `PE problem correct`() {
        val target = 1_000_000
        val expected = 1818
        assertEquals(expected, tool.getLeastM(target))
    }
}