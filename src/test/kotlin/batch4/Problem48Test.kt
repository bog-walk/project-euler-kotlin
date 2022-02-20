package batch4

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.tests.compareSpeed
import util.tests.getSpeed
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
    fun `selPowersSum correct`(n: Int, expected: Long) {
        assertEquals(expected, tool.selfPowersSum(n))
        assertEquals(expected, tool.selfPowersSumModulo(n))
    }

    @Test
    fun `selfPowersSum speed`() {
        val n = 10_000
        val expected = 6_237_204_500
        val solutions = mapOf(
            "BigInteger" to tool::selfPowersSum, "Modular" to tool::selfPowersSumModulo
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, n).run {
                speeds.add(name to second)
                assertEquals(expected, first, "Incorrect $name -> $first")
            }
        }
        compareSpeed(speeds)
    }
}