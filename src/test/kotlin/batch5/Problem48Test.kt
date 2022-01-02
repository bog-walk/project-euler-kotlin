package batch5

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SelfPowersTest {
    private val tool = SelfPowers()

    @ParameterizedTest(name="N = {0}")
    @CsvSource(
        // lower constraints
        "1, 1", "2, 5", "3, 32", "4, 288", "6, 50069", "10, 405071317",
        // mid constraints
        "99, 9027641920", "1000, 9110846700", "8431, 2756754292"
    )
    fun testSelfPowersSum(n: Int, expected: Long) {
        assertEquals(expected, tool.selfPowersSum(n))
        assertEquals(expected, tool.selfPowersSumModulo(n))
    }

    @Test
    fun testSelfPowerSumSpeed() {
        val n = 100_000
        val expected = 3031782500
        val solutions = listOf(
            tool::selfPowersSum, tool::selfPowersSumModulo
        )
        val times = LongArray(solutions.size)
        solutions.forEachIndexed { i, solution ->
            times[i] = measureNanoTime {
                assertEquals(expected, solution(n))
            }
        }
        print("Builtin solution took: ${1.0 * times[0] / 1_000_000_000}s\n" +
                "Mod solution took: ${1.0 * times[1] / 1_000_000_000}s\n")
    }
}