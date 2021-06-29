import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SumOfMultiplesTest {
    // Issues getting multiple values to be converted to varargs
    @ParameterizedTest
    @CsvSource(
            // lower limits for N
            "1, 0, 3", "2, 0, 3", "3, 0, 3", "4, 3, 3",
            // lower limits for factors
            "5, 10, 1",
            // single factors
            "20, 36, 6"
    )
    fun testAllVersion_singleFactor(number: Int, expected: Int, factor: Int) {
        val tool = SumOfMultiples()
        assertEquals(expected, number.sumOfMultiples(factor))
        assertEquals(expected, tool.sumOfMultiplesVersionB(number, factor))
        //assertEquals(expected, tool.sumOfMultiplesVersionC(number, factor))
    }

    @Test
    fun testAllVersions_multipleDifferentFactors() {
        val tool = SumOfMultiples()
        val number = 100
        val expected = 2318
        val factors = intArrayOf(3, 5)
        assertEquals(expected, number.sumOfMultiples(*factors))
        assertEquals(expected, tool.sumOfMultiplesVersionB(number, *factors))
        //assertEquals(expected, tool.sumOfMultiplesVersionC(number, *factors))
    }

    @Test
    fun testAllVersions_multipleEquivalentFactors() {
        val tool = SumOfMultiples()
        val number = 10
        val expected = 18
        val factors = intArrayOf(3, 3, 3)
        assertEquals(expected, number.sumOfMultiples(*factors))
        assertEquals(expected, tool.sumOfMultiplesVersionB(number, *factors))
        //assertEquals(expected, tool.sumOfMultiplesVersionC(number, *factors))
    }

    @Test
    fun testAllVersions_noFactors() {
        val tool = SumOfMultiples()
        val number = 1_000_000
        val expected = 0
        assertEquals(expected, number.sumOfMultiples())
        assertEquals(expected, tool.sumOfMultiplesVersionB(number))
        //assertEquals(expected, tool.sumOfMultiplesVersionC(number))
    }
}