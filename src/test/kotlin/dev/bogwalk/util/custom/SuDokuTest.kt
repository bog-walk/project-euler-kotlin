package dev.bogwalk.util.custom

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SuDokuTest {
    private val input = listOf(
        "207013068", "000680340", "803050000", "310470085", "000162004",
        "600008209", "020091000", "439020800", "700300006"
    )
    private val solution = listOf(
        "247913568", "195687342", "863254197", "312479685", "958162734",
        "674538219", "526891473", "439726851", "781345926"
    )
    private lateinit var puzzle: SuDokuGame
    private lateinit var initialState: List<String>

    @BeforeAll
    fun setup() {
        assertDoesNotThrow {
            puzzle = SuDokuGame(input)
            initialState = puzzle.getGrid()
        }
    }

    @Test
    fun `incorrect input caught`() {
        assertThrows<IllegalArgumentException> { SuDokuGame(input.take(1)) }
        assertThrows<IllegalArgumentException> { SuDokuGame(List(9) { "1234" }) }
        assertThrows<IllegalArgumentException> { SuDokuGame(List(9) { "1234a6789" }) }
    }

    @Test
    fun `puzzle sets up correctly`() {

        assertEquals("207013068", initialState.first())
        assertEquals("700300006", initialState.last())
    }

    @Test
    fun `puzzle is solved correctly`() {
        puzzle.solve()
        assertContentEquals(solution, puzzle.getGrid())
    }
}