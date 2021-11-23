package batch3

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
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
    private val longListLocation = "src/test/resources/NamesScores"

    @ParameterizedTest(name="{0} scores {1}")
    @CsvSource(
        "4, 'PAMELA', 240", "937, 'COLIN', 49714",
        "0, 'A', 1", "5199, 'ZZZZZZZZZZ', 1352000"
    )
    fun testNameScore(index: Int, name: String, expected: Int) {
        assertEquals(expected, tool.nameScore(index, name))
    }

    @Test
    fun testNameScore_smallList() {
        val sorted = smallList.sorted()
        val expected = listOf(42, 244, 144, 88, 240)
        for ((index, name) in smallList.withIndex()) {
            val position = sorted.indexOf(name)
            assertEquals(expected[index], tool.nameScore(position, name))
        }
    }

    @Test
    fun testSumOfNameScores_smallList() {
        assertEquals(758, tool.sumOfNameScores(smallList))
    }

    @Test
    fun testNameScore_mediumList() {
        val sorted = mediumList.sorted()
        assertEquals(130, tool.nameScore(sorted.indexOf("ELI"), "ELI"))
        assertEquals(608, tool.nameScore(sorted.indexOf("NOAH"), "NOAH"))
    }

    @Test
    fun testSumOfNameScores_largeList() {
        val input = File(longListLocation).readLines()
        assertEquals(871198282, tool.sumOfNameScores(input))
    }
}