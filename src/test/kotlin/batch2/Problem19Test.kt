package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class CountingSundaysTest {
    @Test
    fun testIsLeapYear_allLeapYears() {
        val years = listOf<Long>(2016, 2020, 2000, 1980, 2396, 1944)
        val tool = CountingSundays()
        for (year in years) {
            assertTrue(tool.isLeapYear(year))
        }
    }

    @Test
    fun testIsLeapYear_noneLeapYears() {
        val years = listOf<Long>(2100, 2200, 1900, 1986, 2379)
        val tool = CountingSundays()
        for (year in years) {
            assertFalse(tool.isLeapYear(year))
        }
    }

    @ParameterizedTest(name="Jan 1st {0}")
    @CsvSource(
        "1900, 1", "1901, 2", "1920, 4", "1986, 3", "2000, 6", "2020, 3"
    )
    fun testGetJanFirstOfYear(year: Long, expected: Long) {
        val tool = CountingSundays()
        assertEquals(expected, tool.getJanFirstOfYear(year))
    }

    @ParameterizedTest(name="{0}/{1}/{2}")
    @CsvSource(
        "1, 1, 1900, 2", "17, 10, 2021, 1", "24, 8, 2000, 5", "25, 12, 1982, 0"
    )
    fun testGetWeekday(day: Long, month: Long, year: Long, expected: Long) {
        val tool = CountingSundays()
        assertEquals(expected, tool.getWeekday(day, month, year))
    }

    @ParameterizedTest(name="{0} to {1} has {2} Sunday 1sts")
    @CsvSource(
        // whole decades
        "'1900 1 1', '1910 1 1', 18", "'2000 1 1', '2020 1 1', 35",
        // mid-year dates
        "'2020 5 10', '2020 11 29', 1", "'1995 12 6', '1998 4 2', 5",
        // days between
        "'1999 12 31', '2000 1 1', 0", "'2022 12 31', '2023 1 1', 1",
        // a century
        "'1901 1 1', '2000 12 31', 171",
        // adjusted start date brings it after end date
        "'1900 1 4', '1900 1 5', 0", "'1999 12 20', '1999 12 31', 0",
        // extra cases
        "'1988 3 25', '1989 7 13', 2", "'4699 12 12', '4710 1 1', 18",
        "'1924 6 6', '1925 6 16', 2",
        "'1925 6 16', '1924 6 6', 0", "'1905 1 1', '1905 1 1', 1"
    )
    fun testCountSundayFirsts(start: String, end: String, expected: Int) {
        val tool = CountingSundays()
        val (y1, m1, d1) = start.split(" ").map(String::toLong)
        val (y2, m2, d2) = end.split(" ").map(String::toLong)
        assertEquals(expected, tool.countSundayFirsts(y1, m1, d1, y2, m2, d2))
        assertEquals(expected, tool.countSundayFirstsZeller(y1, m1, d1, y2, m2))
    }

    @Test
    fun testUpperConstraint() {
        val tool = CountingSundays()
        val (y1, m1, d1) = "1000000000000 2 2".split(" ").map(String::toLong)
        val (y2, m2) = "1000000001000 3 2".split(" ").map(String::toLong)
        assertEquals(1720, tool.countSundayFirstsZeller(y1, m1, d1, y2, m2))
    }
}