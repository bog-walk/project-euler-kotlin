package batch2

/**
 * Problem 19: Counting Sundays
 *
 * https://projecteuler.net/problem=19
 *
 * Goal: Find the number of Sundays that fell on the 1st day of the month between
 * 2 dates YYYY MM DD inclusive.
 *
 * Constraints: 1900 <= Y1 <= 10^16, Y1 <= Y2 <= Y1 + 1000, 1 <= M1, M2 <= 12, 1 <= D1, D2 <= 31
 *
 * e.g.: Y1 M1 D1 = 1900 1 1, Y2 M2 D2 = 1910 1 1
 *       num of Sundays on the 1st = 18
 */

class CountingSundays {
    private val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    /**
     * Returns day of week on January 1st as an integer, with Sunday = 0.
     */
    fun getJanFirstOfYear(year: Int): Int {
        var start = 1900
        var day = 1 // January 1st 1900 was a Monday
        while (start < year) {
            day = if (isLeapYear(start)) (day + 2) % 7 else (day + 1) % 7
            start++
        }
        return day
    }

    /**
     * 1. Adjust starting month (and year) based on starting day of month.
     * e.g. Dec 20, 2020 would be moved forward to Jan 2021 as only the weekday on the
     * first day of the month matters.
     * 2. Get weekday that is the Jan 1st & use it to calculate the first Sunday that month.
     * 3. Jump forward 7 days (as only interested in Sundays), repeatedly checking if the day
     * of the month exceeds the num of days in that month. If so, increment month (+/- year) &
     * adjust day of month to reflect increment.
     * 4. Increment count if a 7 day jump falls on the first of the month.
     * 5. End loop when end date exceeded.
     */
    fun countSundayFirsts(
        startY: Int, startM: Int, startD: Int,
        endY: Int, endM: Int, endD: Int
    ): Int {
        var count = 0
        var currentYear = startY
        var currentMonth = if (startD == 1) startM else {
            if (startM == 12) {
                currentYear++
            }
            (startM + 1) % 12
        }
        val janFirst = getJanFirstOfYear(currentYear)
        var sunday = if (janFirst == 0) 1 else 8 - janFirst
        if (sunday == 1) count++
        while (currentYear <= endY) {
            sunday += 7
            val monthDays = when (currentMonth) {
                2 -> if (isLeapYear(currentYear)) 29 else 28
                else -> daysInMonth[currentMonth - 1]
            }
            if (sunday > monthDays) {
                sunday -= monthDays
                currentMonth++ // Move forward to next month
            }
            if (sunday == 1) count++
            if (currentYear == endY && currentMonth == endM && sunday > endD) break
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
     * h = (q + (13*(m+1)/5) + K + (K/4) + (J/4) + 5*J) % 7
     * This algorithm considers January & February to be the 13th & 14th month
     * of the preceding year.
     * @return Int from 0 to 6 with 0 being Saturday, 1 Sunday, 2 Monday,...
     */
    fun getWeekday(day: Int, month: Int, year: Int): Int {
        var m = month
        var y = year
        if (month < 3) {
            m = if (month == 1) 13 else 14
            y--
        }
        val k = y % 100
        val j = y / 100
        return (day + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
    }

    /**
     * 1. Adjust starting month (and year) based on starting day of month.
     * e.g. Dec 20, 2020 would be moved forward to Jan 2021 as only the weekday on the
     * first day of the month matters.
     * 2. Get weekday of the 1st day of the month & increment count if a Sunday.
     * 3. Move to next month & repeat.
     * 4. End loop when end date exceeded. Note that end day not important in this
     * solution, only end month.
     */
    fun countSundayFirstsZeller(
        startY: Int, startM: Int, startD: Int,
        endY: Int, endM: Int
    ): Int {
        var count = 0
        var currentYear = startY
        var currentMonth = if (startD == 1) startM else {
            if (startM == 12) {
                currentYear++
            }
            (startM + 1) % 12
        }
        while (currentYear <= endY) {
            val weekdayOfMonthFirst = getWeekday(1, currentMonth, currentYear)
            if (weekdayOfMonthFirst == 1) count++ // 1 == Sunday using Zeller's Congruence
            currentMonth++
            if (currentYear == endY && currentMonth > endM) break
            if (currentMonth > 12) {
                currentYear++
                currentMonth = 1
            }
        }
        return count
    }
}