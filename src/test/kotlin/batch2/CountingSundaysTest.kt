package batch2

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class CountingSundaysTest {
    @Test
    fun testIsLeapYear_allLeapYears() {
        val years = listOf(2016, 2020, 2000, 1980, 2396, 1944)
        val tool = CountingSundays()
        for (year in years) {
            assertTrue(tool.isLeapYear(year))
        }
    }

    @Test
    fun testIsLeapYear_noneLeapYears() {
        val years = listOf(2100, 2200, 1900, 1986, 2379)
        val tool = CountingSundays()
        for (year in years) {
            assertFalse(tool.isLeapYear(year))
        }
    }
}