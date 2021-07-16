import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class SummationOfPrimesTest {
    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        "1, 0", "2, 2", "3, 5", "5, 10", "10, 17"
    )
    fun testSumOfPrimesBrute(n: Int, expected: Int) {
        val tool = SummationOfPrimes()
        assertEquals(expected, tool.sumOfPrimesBrute(n))
    }
}