import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SmallestMultipleTest {
    @ParameterizedTest(name="1..{0} = {1}")
    @CsvSource(
       // lower constraints
        "1, 1", "2, 2", "3, 6",
        // normal values
        "6, 120", "10, 2520", "20, 232792560",
        // higher constraints - TOO LONG
        //"30, 2329089562800", "40, "
    )
    fun testSmallestMultiple(n: Int, expected: Long) {
        val tool = SmallestMultiple()
        assertEquals(expected, tool.smallestMultiple(n))
    }
}