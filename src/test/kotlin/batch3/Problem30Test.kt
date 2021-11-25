package batch3

import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class DigitFifthPowersTest {
    private val tool = DigitFifthPowers()

    @Test
    fun testDigitNthPowers() {
        val expected = listOf(
            listOf(153, 370, 371, 407),
            listOf(1634, 8208, 9474),
            listOf(4150, 4151, 54748, 92727, 93084, 194979),
            listOf(548834)
        )
        for (n in 3..6) {
            val actual = tool.digitNthPowers(n)
            assertContentEquals(expected[n - 3], actual)
        }
    }
}