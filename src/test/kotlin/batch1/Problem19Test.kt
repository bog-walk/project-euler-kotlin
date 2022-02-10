package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import kotlin.system.measureNanoTime
import kotlin.test.Test

internal class CountingSundaysTest {
    private val tool = CountingSundays()

    @ParameterizedTest(name="Jan 1st {0}")
    @CsvSource(
        "1900, 1", "1901, 2", "1920, 4", "1986, 3", "2000, 6", "2020, 3"
    )
    fun `getJanFirstOfYear correct`(year: Long, expected: Int) {
        assertEquals(expected, tool.getJanFirstOfYear(year))
    }

    @Test
    fun `isLeapYear returns true for all leap years`() {
        val years = listOf<Long>(2016, 2020, 2000, 1980, 2396, 1944)
        for (year in years) {
            assertTrue(tool.isLeapYear(year))
        }
    }

    @Test
    fun `isLeapYear returns false for non-leap years`() {
        val years = listOf<Long>(2100, 2200, 1900, 1986, 2379)
        for (year in years) {
            assertFalse(tool.isLeapYear(year))
        }
    }

    @ParameterizedTest(name="{0}/{1}/{2}")
    @CsvSource(
        "1, 1, 1900, 2", "17, 10, 2021, 1", "24, 8, 2000, 5", "25, 12, 1982, 0"
    )
    fun `getWeekday correct`(day: Int, month: Int, year: Long, expected: Int) {
        assertEquals(expected, tool.getWeekday(day, month, year))
    }

    @ParameterizedTest(name="{0} to {1} has {2} Sunday 1sts")
    @CsvSource(
        // start date exceeds end date
        "'1925 6 16', '1924 6 6', 0",
        // start date == end date
        "'1905 1 1', '1905 1 1', 1", "'2022 2 10', '2022 2 10', 0",
        // one day between dates
        "'1999 12 31', '2000 1 1', 0", "'2022 12 31', '2023 1 1', 1",
        // months between dates in same year
        "'2020 5 10', '2020 11 29', 1",
        // months between dates over different years
        "'1988 3 25', '1989 7 13', 2",
        // years between dates
        "'1924 6 6', '1925 6 16', 2", "'1995 12 6', '1998 4 2', 5",
        // decade between dates
        "'1900 1 1', '1910 1 1', 18", "'2000 1 1', '2020 1 1', 35",
        // century between dates
        "'1901 1 1', '2000 12 31', 171",
        // adjusted start date exceeds end date
        "'1900 1 4', '1900 1 5', 0", "'1999 12 20', '1999 12 31', 0",
        // future cases
        "'4699 12 12', '4710 1 1', 18",
    )
    fun `countSundayFirsts correct for lower constraints`(
        start: String, end: String, expected: Int
    ) {
        val (y1, m1, d1) = start.split(" ").map(String::toLong)
        val (y2, m2, d2) = end.split(" ").map(String::toLong)
        assertEquals(
            expected, tool.countSundayFirsts(
                y1, m1.toInt(), d1.toInt(), y2, m2.toInt(), d2.toInt()
            )
        )
        assertEquals(
            expected, tool.countSundayFirstsZeller(
                y1, m1.toInt(), d1.toInt(), y2, m2.toInt()
            )
        )
        assertEquals(
            expected, tool.countSundayFirstsLibrary(
                y1, m1.toInt(), d1.toInt(), y2, m2.toInt(), d2.toInt()
            )
        )
    }

    @Test
    fun `countSundayFirsts correct for upper constraints`() {
        val startY = 1e12.toLong()
        val endY = startY + 1000L
        val startM = 2
        val endM = startM + 1
        val day = 2
        val expected = 1720
        assertEquals(
            expected, tool.countSundayFirstsZeller(startY, startM, day, endY, endM)
        )
        assertEquals(
            expected, tool.countSundayFirstsLibrary(startY, startM, day, endY, endM, day)
        )
    }

    @Test
    fun `countSundayFirsts speed`() {
        val startY = 1e6.toLong()
        val endY = startY + 1000L
        val month = 1
        val day = 1
        val expected = 1720
        val speeds = mutableListOf<Pair<String, Long>>()
        val results = mutableListOf<Int>()
        val ogActual: Int
        val ogTime = measureNanoTime {
            ogActual = tool.countSundayFirsts(startY, month, day, endY, month, day)
        }
        speeds.add("Original" to ogTime)
        results.add(ogActual)
        val zellerActual: Int
        val zellerTime = measureNanoTime {
            zellerActual = tool.countSundayFirstsZeller(startY, month, day, endY, month)
        }
        speeds.add("Zeller's" to zellerTime)
        results.add(zellerActual)
        val libActual: Int
        val libTime = measureNanoTime {
            libActual = tool.countSundayFirstsLibrary(startY, month, day, endY, month, day)
        }
        speeds.add("Library" to libTime)
        results.add(libActual)
        compareSpeed(speeds)
        assertTrue { results.all { expected == it } }
    }
}