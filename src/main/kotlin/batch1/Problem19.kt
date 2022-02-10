package batch1

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Problem 19: Counting Sundays
 *
 * https://projecteuler.net/problem=19
 *
 * Goal: Find the number of Sundays that fell on the 1st day of the month between
 * 2 dates YYYY MM DD inclusive.
 *
 * Constraints: 1900 <= Y1 <= 1e16, Y1 <= Y2 <= Y1 + 1000
 *              1 <= M1, M2 <= 12
 *              1 <= D1, D2 <= 31
 *
 * e.g.: Y1 M1 D1 = 1900 1 1, Y2 M2 D2 = 1910 1 1
 *       num of Sundays on the 1st = 18
 */

class CountingSundays {
    /**
     * Uses the java.time library to abstract movement through the given dates, as well as
     * built-in determination of the associated weekday and comparison of the dates to end the
     * search.
     *
     * For the dates to work with the library functions, the years must first be normalised using
     * the pattern that the calendar cycles every 400 years, unless that period crosses over into
     * a new century (it cycles every 28 years if that century is a leap year).
     *
     * SPEED (WORSE) 29.00ms for 1000 year delta in the upper constraints
     */
    fun countSundayFirstsLibrary(
        startY: Long, startM: Int, startD: Int,
        endY: Long, endM: Int, endD: Int
    ): Int {
        val yearNorm = (startY % 400).toInt() + 400
        val endYNorm = (endY - startY).toInt() + yearNorm
        var start = LocalDate.of(yearNorm, startM, startD)
        val end = LocalDate.of(endYNorm, endM, endD)
        var sundays = 0
        var step = 1L
        while (start <= end) {
            // find first day of month that is a Sunday, if any
            if (start.dayOfMonth == 1 && start.dayOfWeek == DayOfWeek.SUNDAY) {
                sundays++
                // check only every Sunday after that
                step = 7L
            }
            start = start.plusDays(step)
        }
        return sundays
    }

    /**
     * This solution will not tolerate years > 1e6 well.
     *
     * SPEED (BETTER) 23.79ms for 1000 year delta in the upper constraints
     */
    fun countSundayFirsts(
        startY: Long, startM: Int, startD: Int,
        endY: Long, endM: Int, endD: Int
    ): Int {
        val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        // adjust starting month & year
        var currentYear = startY
        var currentMonth = startM
        if (startD > 1L) {
            currentMonth = startM % 12 + 1
            if (currentMonth == 1) currentYear++
        }
        var sundays = 0
        // get weekday that corresponds to Jan 1st of starting year
        val janFirst = getJanFirstOfYear(currentYear)
        // use above weekday to find first Sunday in January that year
        var day = if (janFirst == 0) 1 else 8 - janFirst
        if (currentYear == endY && currentMonth > endM) return 0
        if (day == 1) sundays++
        while (currentYear <= endY) {
            // jump forward a week as only interested in checking every Sunday
            day += 7
            val monthDays = if (currentMonth == 2 && isLeapYear(currentYear)) {
                29
            } else {
                daysInMonth[currentMonth.toInt() - 1]
            }
            if (day > monthDays) {
                day -= monthDays
                currentMonth++
            }
            if (currentYear == endY && currentMonth == endM && day > endD) break
            if (day == 1) sundays++
            if (currentMonth > 12) {
                currentYear++
                currentMonth = 1
            }
        }
        return sundays
    }

    /**
     * Brute search finds weekday on January 1st of provided year, based on the fact that
     * Jan 1st, 1900 was a Monday.
     *
     * @return Int from 0 to 6 with 0 = Sunday.
     */
    fun getJanFirstOfYear(year: Long): Int {
        var start = 1900L
        var day = 1
        while (start < year) {
            day = if (isLeapYear(start)) (day + 2) % 7 else (day + 1) % 7
            start++
        }
        return day
    }

    fun isLeapYear(year: Long): Boolean {
        return year % 4 == 0L && (year % 100 != 0L || year % 400 == 0L)
    }

    /**
     * Solution using Zeller's Congruence algorithm helper function.
     *
     * SPEED (BEST) 5.77ms for 1000 year delta in the upper constraints
     */
    fun countSundayFirstsZeller(
        startY: Long, startM: Int, startD: Int,
        endY: Long, endM: Int
    ): Int {
        // adjust starting month forward
        // as only the weekday on the first of the month matters
        var currentYear = startY
        var currentMonth = startM
        if (startD > 1L) {
            currentMonth = startM % 12 + 1
            // adjust starting year forward if date has rolled over
            // e.g. Dec 2, 2020 would become Jan 1, 2021
            if (currentMonth == 1) currentYear++
        }
        var sundays = 0
        // end loop when end month & year exceeded, as end day is not relevant
        while (currentYear <= endY) {
            if (currentYear == endY && currentMonth > endM) break
            // check if first of month is Sunday
            if (getWeekday(1, currentMonth, currentYear) == 1) sundays++
            // move forward to next month
            currentMonth = currentMonth % 12 + 1
            if (currentMonth == 1) currentYear++
        }
        return sundays
    }

    /**
     * Find the weekday for a date using Zeller's Congruence algorithm.
     *
     * Zeller's Congruence algorithm is based on the formula:
     *
     * h = (day + 13(month + 1)/5 + K + K/4 + J/4 + 5J) % 7
     *
     * with month & year being adjusted to have January and February as the 13th & 14th months of
     * the preceding year, and (K, J) = (year % 100, year / 100). Note that this only applies to
     * the Gregorian calendar.
     *
     * @return Int from 0 to 6 with 0 = Saturday, 1 = Sunday, ..., 6 = Friday.
     */
    fun getWeekday(day: Int, month: Int, year: Long): Int {
        var m = month
        var y = year
        if (month < 3) {
            m += 12
            y--
        }
        val k = y % 100
        val j = y / 100
        return ((day + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7).toInt()
    }
}