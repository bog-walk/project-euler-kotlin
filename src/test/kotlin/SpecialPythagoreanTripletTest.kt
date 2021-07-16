import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class SpecialPythagoreanTripletTest {
    @Test
    fun testFindTriplets_noneFound() {
        val tool = SpecialPythagoreanTriplet()
        val nums = listOf(1, 4, 6, 31, 99, 100)
        for (n in nums) {
            assertNull(tool.findTriplets(n))
        }
    }

    @ParameterizedTest(name="{0} has ({1}, {2}, {3})")
    @CsvSource(
        "12, 3, 4, 5", "24, 6, 8, 10",
        "30, 5, 12, 13", "90, 9, 40, 41",
        "650, 25, 312, 313", "2214, 533, 756, 925",
        "1000, 200, 375, 425"
    )
    fun testFindTriplets_found(n: Int, a: Int, b: Int, c: Int) {
        val tool = SpecialPythagoreanTriplet()
        val expected = Triple(a, b, c)
        assertEquals(expected, tool.findTriplets(n))
    }

}