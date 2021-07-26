package batch1

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
            //assertNull(tool.findTripletsLoop(n))
            assertNull(tool.findTripletsLoopImproved(n))
            //assertNull(tool.findTripletsParametrisation(n))
        }
    }

    @ParameterizedTest(name="{0} has ({1}, {2}, {3})")
    @CsvSource(
        "12, 3, 4, 5", "24, 6, 8, 10",
        "30, 5, 12, 13", "90, 15, 36, 39",
        "650, 25, 312, 313", "1000, 200, 375, 425",
        "2214, 533, 756, 925", "3000, 750, 1000, 1250"
    )
    fun testFindTriplets_found(n: Int, a: Int, b: Int, c: Int) {
        val tool = SpecialPythagoreanTriplet()
        val expected = Triple(a, b, c)
        //assertEquals(expected, tool.findTripletsLoop(n))
        assertEquals(expected, tool.findTripletsLoopImproved(n))
        //assertEquals(expected, tool.findTripletsParametrisation(n))
    }

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        "1, -1", "10, -1", "1231, -1",
        "12, 60", "90, 21060", "1000, 31875000",
        "3000, 937500000"
    )
    fun testMaxTripletsProduct(n: Int, expected: Long) {
        val tool = SpecialPythagoreanTriplet()
        assertEquals(expected, tool.maxTripletProduct(n))
    }

}