package dev.bogwalk.batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GoldbachsOtherConjectureTest {
    private val tool = GoldbachsOtherConjecture()

    @ParameterizedTest(name="Composite = {0}")
    @CsvSource(
        // lower constraints
        "9, 1", "15, 2", "21, 3", "25, 3", "27, 1", "33, 1",
        // mid constraints
        "403, 5", "695, 2", "1599, 12", " 3393, 10",
        // upper constraints
        "23851, 53", "499999, 156",
        // no representation
        "5777, 0"
    )
    fun `HR problem correct`(n: Int, expected: Int) {
        assertEquals(expected, tool.countGoldbachRepresentations(n))
    }

    @Test
    fun `PE problem correct`() {
        val expected = 5777
        assertEquals(expected, tool.smallestFailingComposite())
    }
}