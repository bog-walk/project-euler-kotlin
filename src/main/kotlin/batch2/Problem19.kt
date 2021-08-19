package batch2

/**
 * Problem 19: Counting Sundays
 * Goal: Count how many Sundays fell on the 1st day of the month between 2 dates inclusive?
 * The provided year will have 1900 <= Y1 <= 10^16, & Y1 <= Y2 <= Y1 + 1000.
 * Input provided as Y M D (twice).
 * - January 1, 1900 was a Monday.
 * - Days in month = 28 {Feb}, 30 {Sept, Apr, June, Nov}, 31 {all the rest}.
 * - Leap Year: any year divisible by 4, except centuries, unless latter divisible by 400.
 * Test: during the 20th century (Jan 1 1901 to Dec 31 2000),
 * between 1900 1 1 & 1910 1 1 = 18,
 * between 2000 1 1 and 2020 1 1 = 35.
 */

class CountingSundays {
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }
}