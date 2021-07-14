import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class SpecialPythagoreanTripletTest {
    @Test
    fun testFindTriplets_noneFound() {
        val tool = SpecialPythagoreanTriplet()
        val nums = listOf(1, 4, 6, 100)
        for (n in nums) {
            assertNull(tool.findTriplets(n))
        }
    }

    @ParameterizedTest(name="{0} has ({1}, {2}, {3})")
    @CsvSource(
        "12, 3, 4, 5"
    )
    fun testFindTriplets_found(n: Int, a: Int, b: Int, c: Int) {
        val tool = SpecialPythagoreanTriplet()
        val expected = Triple(a, b, c)
        assertEquals(expected, tool.findTriplets(n))
    }

}