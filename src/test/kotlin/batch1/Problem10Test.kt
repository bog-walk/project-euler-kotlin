package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.primeNumbers
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SummationOfPrimesTest {
    private val allPrimes = SummationOfPrimes().sumOfPrimesQuickDraw()

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        "2, 2", "3, 5", "5, 10", "10, 17",
        "100, 1060", "5000, 1548136", "300000, 3709507114",
        "1000000, 37550402023"
    )
    fun testSumOfPrimes(n: Int, expected: Long) {
        val tool = SummationOfPrimes()
        //assertEquals(expected, tool.sumOfPrimes(n, ::getPrimeNumbers))
        //assertEquals(expected, tool.sumOfPrimes(n, tool::getPrimesUsingSieve))
        assertEquals(expected, allPrimes[n])
    }

    @Test
    fun testGetPrimesUsingSieve() {
        val tool = SummationOfPrimes()
        val list = listOf(2, 3, 5, 10, 25, 71, 100, 500, 999, 2000, 10000)
        list.forEach {
            assertEquals(primeNumbers(it), tool.getPrimesUsingSieve(it))
        }
    }
}