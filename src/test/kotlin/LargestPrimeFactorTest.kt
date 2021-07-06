import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestPrimeFactorTest {
    @ParameterizedTest(name="N={0} gives {1}")
    @CsvSource(
        // lower constraint values
        "2, 2", "3, 3", "10, 5",
        // normal values
        "17, 17", "48, 3", "147, 7", "330, 11",
        // larger values
        "13195, 29", "200000, 5", "600851475143, 6857",
        // large prime numbers, including Euler's
        "7919, 7919", "2147483647, 2147483647", "67280421310721, 67280421310721",
        // higher constraint values
        "9007199254740991, 20394401", "10000000000000000, 5"
    )
    fun testAlgorithm_largestPrime_recursive(n: Long, expected: Long) {
        val tool = LargestPrimeFactor()
        //val primes = tool.getPrimeFactorsRecursive(n)
        val primesImproved = tool.getPrimeFactorsRecursiveImproved(n)
        //assertEquals(expected, tool.largestPrime(primes))
        assertEquals(expected, tool.largestPrime(primesImproved))
    }

    @Test
    fun testIsComposite() {
        assertTrue(6L.isComposite())
        assertTrue(40L.isComposite())
        assertFalse(1L.isComposite())
        assertFalse(2L.isComposite())
        assertFalse(31L.isComposite())
    }

    @ParameterizedTest(name="{0} divided by {1}")
    @CsvSource(
        "12, 2, true, 6", "147, 3, true, 49",
        "12, 11, false, 1", "147, 2, false, 73"
    )
    fun testAlgorithm_divide(n: Long, factor: Long, whole: Boolean, ans: Long) {
        val (actualFirst, actualSecond) = n.divide(factor)
        assertEquals(whole, actualFirst)
        assertEquals(ans, actualSecond)
    }

    @Test
    fun testGetPrimeFactors() {
        val nums = listOf<Long>(2, 3, 12, 17, 48, 147, 330, 13195)
        val expected = listOf<List<Long>>(
            listOf(2), listOf(3), listOf(2, 2, 3), listOf(17), listOf(2, 2, 2, 2, 3),
            listOf(3, 7, 7), listOf(2, 3, 5, 11), listOf(5, 7, 13, 29)
        )
        val tool = LargestPrimeFactor()
        nums.forEachIndexed { index, num ->
            assertEquals(expected[index], tool.getPrimeFactors(num))
        }
    }

    @Test
    fun testGetPrimeFactors_recursive() {
        val nums = listOf<Long>(2, 3, 12, 17, 48, 147, 330, 13195, 200000, 600851475143)
        val expected = listOf<List<Long>>(
            listOf(2), listOf(3), listOf(2, 2, 3), listOf(17), listOf(2, 2, 2, 2, 3),
            listOf(3, 7, 7), listOf(2, 3, 5, 11), listOf(5, 7, 13, 29),
            listOf(2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5),
            listOf(71, 839, 1471, 6857)
        )
        val tool = LargestPrimeFactor()
        nums.forEachIndexed { index, num ->
            assertEquals(expected[index], tool.getPrimeFactorsRecursive(num))
            assertEquals(expected[index], tool.getPrimeFactorsRecursiveImproved(num))
        }
    }
}