package dev.bogwalk.batch9

import dev.bogwalk.util.custom.SuDokuGame
import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.getTestResource
import org.junit.jupiter.api.*
import kotlin.test.assertContentEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class SuDokuTest {
    private val superEasyPuzzle = listOf("123456780", "456780123", "780123456", "234567801", "567801234",
        "801234567", "345678012", "678012345", "012345678")
    private val easyPuzzle = listOf("610000803", "047080500", "000900006", "000300014", "324016058",
        "560498072", "036509000", "000067005", "700000400")
    private val mediumPuzzle = listOf("050000079", "003008100", "290064500", "506400013",
        "020905607", "000003000", "000000340", "000009720", "400200001")
    private val hardPuzzle = listOf("000000640", "540000807", "000080001", "900060070", "063400105",
        "000078000", "004100000", "306005018", "100002090")
    private lateinit var allPuzzles: List<SuDokuGame>

    @BeforeAll
    fun setUp() {
        val resource = getTestResource("src/test/resources/SuDoku.txt")
        allPuzzles = List(50) {
            val startIndex = it * 10 + 1
            val endIndex = startIndex + 8
            SuDokuGame(resource.slice(startIndex..endIndex))
        }
    }

    @Test
    @Order(1)
    fun `setUp() handles resources appropriately`() {
        assertEquals(50, allPuzzles.size)
        assertEquals("003020600", allPuzzles.first().getGrid().first())
        assertEquals("300200000", allPuzzles.last().getGrid().first())
    }

    @Test
    @Order(2)
    fun `SuDoku class solves individual puzzles correctly`() {
        val puzzles = listOf(superEasyPuzzle, easyPuzzle, mediumPuzzle, hardPuzzle)
        val solutions = listOf(
            listOf("123456789", "456789123", "789123456", "234567891", "567891234",
                "891234567", "345678912", "678912345", "912345678"),
            listOf("615274893", "947683521", "283951746", "879325614", "324716958",
                "561498372", "136549287", "492867135", "758132469"),
            listOf("658132479", "743598162", "291764538", "586427913", "324915687",
                "179683254", "962871345", "815349726", "437256891"),
            listOf("831257649", "549613827", "672984531", "928561374", "763429185",
                "415378962", "284196753", "396745218", "157832496")
        )
        for ((single, solution) in puzzles.zip(solutions)) {
            val puzzle = SuDokuGame(single)
            puzzle.solve()
            assertContentEquals(solution, puzzle.getGrid())
        }
    }

    @Test
    @Order(3)
    fun `PE solution correct`() {
        val tool = SuDoku()

        assertEquals(24702, tool.solveAllSuDoku(allPuzzles))
    }
}