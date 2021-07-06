import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestPrimeFactorTest {
    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        // lower constraint values
        "0, 0", "2, 2", "10, 5",
        // normal values
        "17, 17", "48, 3", "147, 7", "330, 11",
        // larger values
        "13195, 29", "200000, 5", "600851475143"
        // higher constraint values
        //"10000000000000000, "
    )
    fun testAlgorithm_largestPrime(n: Long, expected: Int) {
        val tool = LargestPrimeFactor()
        assertEquals(expected, tool.largestPrime(n))
    }

    @Test
    fun testIsComposite() {
        assertTrue(6.isComposite())
        assertTrue(40.isComposite())
        assertFalse(1.isComposite())
        assertFalse(2.isComposite())
        assertFalse(31.isComposite())
    }

    @ParameterizedTest(name="{0} divided by {1}")
    @CsvSource(
        "12, 2, true, 6", "147, 3, true, 49",
        "12, 11, false, 1", "147, 2, false, 73"
    )
    fun testAlgorithm_divide(n: Long, factor: Int, whole: Boolean, ans: Int) {
        assertEquals(Pair(whole, ans), n.divide(factor))
    }

    @Test
    fun testGetPrimeFactors() {
        val tool = LargestPrimeFactor()
        assertEquals(emptyList<Int>(), tool.getPrimeFactors(1L))
        assertEquals(listOf(2), tool.getPrimeFactors(2L))
        assertEquals(listOf(3), tool.getPrimeFactors(3L))
        assertEquals(listOf(2, 2, 2, 2, 3), tool.getPrimeFactors(48L))
        assertEquals(listOf(2, 2, 3), tool.getPrimeFactors(12L))
        assertEquals(listOf(3, 7, 7), tool.getPrimeFactors(147L))
        assertEquals(listOf(5, 7, 13, 29), tool.getPrimeFactors(13195L))
    }
}