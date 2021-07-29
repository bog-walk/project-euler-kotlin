package batch2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargeSumTest {
    private val fiftyDigs = getTestDigits("LargeSum5N")
    private val tenDigs = listOf(
        "6041184107", "5351558590", "1833324270"
    )

    private fun getTestDigits(filename: String): List<String> {
        return File(filename).readLines()
    }
}