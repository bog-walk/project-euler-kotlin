package dev.bogwalk.batch8

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CubeDigitPairsTest {
    private val tool = CubeDigitPairs()

    @ParameterizedTest(name="{0} dice with {1}^2")
    @CsvSource(
        // 1 dice
        "1, 1, 126", "2, 1, 70", "3, 1, 55",
        // 2 dice
        "2, 2, 9450", "3, 2, 8630", "4, 2, 8150", "5, 2, 3946", "6, 2, 2579", "7, 2, 2365",
        "8, 2, 2365", "9, 2, 1217",
        // 3 dice
        "3, 3, 519805", "4, 3, 518050", "10, 3, 294197", "20, 3, 28954", "30, 3, 9600"
    )
    fun `correct for all constraints`(square: Int, dice: Int, expected: Int) {
        assertEquals(expected, tool.countValidCubes(square, dice))
    }
}