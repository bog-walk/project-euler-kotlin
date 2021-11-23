package batch2

/**
 * Problem 19: Counting Sundays
 *
 * https://projecteuler.net/problem=19
 *
 * Goal: Find the number of Sundays that fell on the 1st day of the month between
 * 2 dates YYYY MM DD inclusive.
 *
 * Constraints: 1900 <= Y1 <= 10^16, Y1 <= Y2 <= Y1 + 1000,
 *              1 <= M1, M2 <= 12,
 *              1 <= D1, D2 <= 31
 *
 * e.g.: Y1 M1 D1 = 1900 1 1, Y2 M2 D2 = 1910 1 1
 *       num of Sundays on the 1st = 18
 */

class CountingSundays {
    private val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    fun isLeapYear(year: Long): Boolean {
        return year % 4 == 0L && (year % 100 != 0L || year % 400 == 0L)
    }

    /**
     * Iterative search returns day of week on January 1st of
     * provided year, based on the fact that Jan 1st, 1900 was
     * a Monday. Sunday = 0.
     */
    fun getJanFirstOfYear(year: Long): Long {
        var start = 1900L
        var day = 1L // January 1st 1900 was a Monday
        while (start < year) {
            day = if (isLeapYear(start)) (day + 2) % 7 else (day + 1) % 7
            start++
        }
        return day
    }

    /**
     * Consider using memoisation to improve performance when evaluating
     * dates in the upper constraints. Or use Zeller's Congruence solution instead.
     */
    fun countSundayFirsts(
        startY: Long, startM: Long, startD: Long,
        endY: Long, endM: Long, endD: Long
    ): Int {
        // Adjust starting month & year
        var currentYear = startY
        var currentMonth = startM
        if (startD > 1L) {
            currentMonth = startM % 12L + 1L
            if (currentMonth == 1L) currentYear++
        }
        var count = 0
        // Get weekday that corresponds to Jan 1st of starting year
        val janFirst = getJanFirstOfYear(currentYear)
        // Use above weekday to find first Sunday in January that year
        var sunday = if (janFirst == 0L) 1 else 8 - janFirst
        if (currentYear == endY && currentMonth > endM) return count
        if (sunday == 1L) count++
        while (currentYear <= endY) {
            // Jump forward a week as only interested in checking every Sunday
            sunday += 7
            val monthDays = if (currentMonth == 2L && isLeapYear(currentYear)) {
                29
            } else {
                daysInMonth[currentMonth.toInt() - 1]
            }
            if (sunday > monthDays) {
                sunday -= monthDays
                currentMonth++
            }
            if (currentYear == endY && currentMonth == endM && sunday > endD) break
            if (sunday == 1L) count++
            if (currentMonth > 12) {
                currentYear++
                currentMonth = 1
            }
        }
        return count
    }

    /**
     * Zeller's Congruence algorithm returns the weekday for any date based
     * on the formula (note this applies to Gregorian not Julian calendars):
     * h = (day + (13*(month+1)/5) + K + (K/4) + (J/4) + 5*J) % 7;
     * with month & year being adjusted to have January and February as
     * the 13th & 14th months of the preceding year,
     * and (K, J) = (year % 100, year / 100).
     *
     * @return Int from 0 to 6 with 0 = Saturday, 1 = Sunday, ..., 6 = Friday.
     */
    fun getWeekday(day: Long, month: Long, year: Long): Long {
        var m = month
        var y = year
        if (month < 3) {
            m += 12
            y--
        }
        val k = y % 100
        val j = y / 100
        return (day + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    }

    fun countSundayFirstsZeller(
        startY: Long, startM: Long, startD: Long,
        endY: Long, endM: Long
    ): Int {
        // Adjust starting month based on starting day of month,
        // as only the weekday on the first day of the month matters.
        var currentYear = startY
        var currentMonth = startM
        if (startD > 1L) {
            currentMonth = startM % 12L + 1L
            // Adjust starting year forward if date has rolled over
            // e.g. Dec 2, 2020 would become Jan 1, 2021
            if (currentMonth == 1L) currentYear++
        }
        var count = 0
        // End loop when end month & year exceeded, as end day is not relevant
        while (currentYear <= endY) {
            if (currentYear == endY && currentMonth > endM) break
            // Use Zeller's congruence to check if first of month is Sunday
            if (getWeekday(1, currentMonth, currentYear) == 1L) count++
            // Move forward to next month
            currentMonth = currentMonth % 12L + 1L
            if (currentMonth == 1L) currentYear++
        }
        return count
    }
}