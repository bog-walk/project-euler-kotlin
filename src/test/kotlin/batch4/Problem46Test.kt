package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GoldbachsOtherConjectureTest {
    private val tool = GoldbachsOtherConjecture()

    @ParameterizedTest(name="Composite={0}")
    @CsvSource(
        // lower constraint
        "9, 1", "15, 2", "21, 3", "25, 3", "27, 1", "33, 1",
        // mid constraint
        "403, 5", "695, 2", "1599, 12", " 3393, 10",
        // upper constraint
        "23851, 53", "499999, 156",
        // no representation
        "5777, 0"
    )
    fun testCountGoldbachRepresentations(n: Int, expected: Int) {
        assertEquals(expected, tool.countGoldbachRepresentations(n))
    }

    @Test
    fun testSmallestFailingComposite() {
        val expected = 5777
        assertEquals(expected, tool.smallestFailingComposite())
    }
}