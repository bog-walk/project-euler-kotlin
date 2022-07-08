package dev.bogwalk.batch2

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import dev.bogwalk.util.tests.getTestResource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NamesScoresTest {
    private val tool = NamesScores()

    private val smallList = listOf("ALEX", "LUIS", "JAMES", "BRIAN", "PAMELA")
    private val mediumList = listOf(
        "OLIVIA", "ALEX", "MIA", "LUIS", "LEO", "JAMES", "BRIAN", "NOAH", "PAMELA",
        "AIDEN", "BENJAMIN", "HARPER", "MUHAMMAD", "PENELOPE", "RILEY", "JACOB",
        "SEBASTIAN", "LILY", "ELI", "IVY", "STELLA", "HANNAH", "VIOLET"
    )
    private val longListLocation = "src/test/resources/NamesScores.txt"

    @ParameterizedTest(name="{0} scores {1}")
    @CsvSource(
        "4, 'PAMELA', 240", "937, 'COLIN', 49714",
        "0, 'A', 1", "5199, 'ZZZZZZZZZZ', 1352000"
    )
    fun `nameScore correct`(index: Int, name: String, expected: Int) {
        assertEquals(expected, tool.nameScore(index, name))
    }

    @Test
    fun `nameScore correct for a small list`() {
        val sorted = smallList.sorted()
        val expected = listOf(42, 244, 144, 88, 240)
        for ((index, name) in smallList.withIndex()) {
            val position = sorted.indexOf(name)
            assertEquals(expected[index], tool.nameScore(position, name))
        }
    }

    @Test
    fun `PE problem correct for a small list`() {
        val expected = 758
        assertEquals(expected, tool.sumOfNameScores(smallList))
    }

    @Test
    fun `nameScore correct for a medium list`() {
        val sorted = mediumList.sorted()
        val names = listOf("ELI", "NOAH")
        val expected = listOf(130, 608)
        for ((i, name) in names.withIndex()) {
            assertEquals(
                expected[i],
                tool.nameScore(sorted.indexOf(name), name)
            )
        }
    }

    @Test
    fun `PE problem correct for a large list`() {
        val longList = getTestResource(longListLocation)
        val expected = 871_198_282
        assertEquals(expected, tool.sumOfNameScores(longList))
    }
}