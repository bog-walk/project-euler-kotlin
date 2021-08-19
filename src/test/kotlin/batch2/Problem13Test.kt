package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargeSumTest {
    private val hundredDigs = getTestDigits("src/test/resources/LargeSum100N")
    private val fiftyDigs = getTestDigits("src/test/resources/LargeSum5N")
    private val tenDigs = listOf(
        "6041184107", "5351558590", "1833324270"
    )
    private val threeDigs = listOf("123", "456", "789", "812", "234")

    @Test
    fun testSmallDigs() {
        val tool = LargeSum()
        val expected = threeDigs.sumOf { it.toInt() }.toString()
        assertEquals(expected, tool.addInReverse(threeDigs))
    }

    @Test
    fun testMidDigs() {
        val tool = LargeSum()
        assertEquals("1322606696", tool.addInReverse(tenDigs))
    }

    @Test
    fun testLargeDigs() {
        val tool = LargeSum()
        assertEquals("2728190129", tool.addInReverse(fiftyDigs))
    }

    @Test
    fun testVeryLargeDigs() {
        val tool = LargeSum()
        assertEquals("5537376230", tool.addInReverse(hundredDigs))
    }

    private fun getTestDigits(filename: String): List<String> {
        val digits = mutableListOf<String>()
        File(filename).forEachLine {
            digits.add(it)
        }
        return digits
    }
}