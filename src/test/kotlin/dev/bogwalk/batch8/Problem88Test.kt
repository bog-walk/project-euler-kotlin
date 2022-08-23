package dev.bogwalk.batch8

import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProductSumNumbersTest {
    private val tool = ProductSumNumbers()

    @BeforeAll
    fun setUp() {
        tool.generatePSNumbers()
    }

    @ParameterizedTest(name="Sum of 2<=k<={0} = {1}")
    @CsvSource(
        // lower constraints
        "2, 4", "3, 10", "4, 18", "5, 18", "6, 30",
        // lower mid constraints
        "12, 61", "15, 123", "27, 288", "53, 684", "100, 2061", "500, 29836",
        // upper mid constraints
        "1000, 93063", "5000, 1517617", "12000, 7587457",
        // upper constraints
        "60000, 135517061", "100000, 344017453", "200000, 1229547946"
    )
    fun `correct for all constraints`(k: Int, expected: Int) {
        assertEquals(expected, tool.sumOfPSNumbers(k))
    }
}