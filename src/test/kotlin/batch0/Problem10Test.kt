package batch0

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.primeNumbersOG

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SummationOfPrimesTest {
    private val tool = SummationOfPrimes()
    private val allPrimes = SummationOfPrimes().sumOfPrimesQuickDraw(1_000_000)

    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        "2, 2", "3, 5", "5, 10", "10, 17",
        "100, 1060", "5000, 1548136", "300000, 3709507114",
        "1000000, 37550402023"
    )
    fun testSumOfPrimes(n: Int, expected: Long) {
        assertEquals(expected, tool.sumOfPrimes(n, ::primeNumbersOG))
        assertEquals(expected, allPrimes[n])
    }
}