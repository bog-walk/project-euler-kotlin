package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

internal class LargestProductInSeriesTest {
    private val tool = LargestProductInSeries()

    @ParameterizedTest(name="{0} *..* = {1}")
    @CsvSource(
        "'8', 8", "'1234', 24", "'63972201', 0",
        "'1111111111111', 1", "'3675356291', 1020600"
    )
    fun testStringProduct(number: String, expected: Long) {
        assertEquals(expected, tool.stringProduct(number))
    }

    @ParameterizedTest(name="{0} = {3}")
    @CsvSource(
        // N == 1
        "'8', 1, 1, 8",
        // K == 1
        "'63972201', 8, 1, 9",
        // N == K
        "'1234', 4, 4, 24", "'1111111111111', 13, 13, 1",
        // number contains 0 in all series
        "'2709360626', 10, 5, 0",
        // number contains 0 in some series
        "'12034', 5, 2, 12",
        // number is not very large
        "'3675356291', 10, 5, 3150"
    )
    fun testLargestSeriesProduct(number: String, digits: Int, seriesSize: Int, expected: Long) {
        assertEquals(expected, tool.largestSeriesProduct(number, digits, seriesSize))
    }

    @Test
    fun test100DigitNumber() {
        val number = StringBuilder()
        // Create a number of all '1's except for 6 adjacent '6's
        repeat(100) { i ->
            number.append(if (i in 60..65) 6 else 1)
        }
        val expected: Long = 46656 // 6^6
        assertEquals(expected, tool.largestSeriesProduct(number.toString(), 100, 6))
    }

    @Test
    fun test1000DigitNumber() {
        val number = "73167176531330624919225119674426574742355349194934" +
                "96983520312774506326239578318016984801869478851843" +
                "85861560789112949495459501737958331952853208805511" +
                "12540698747158523863050715693290963295227443043557" +
                "66896648950445244523161731856403098711121722383113" +
                "62229893423380308135336276614282806444486645238749" +
                "30358907296290491560440772390713810515859307960866" +
                "70172427121883998797908792274921901699720888093776" +
                "65727333001053367881220235421809751254540594752243" +
                "52584907711670556013604839586446706324415722155397" +
                "53697817977846174064955149290862569321978468622482" +
                "83972241375657056057490261407972968652414535100474" +
                "82166370484403199890008895243450658541227588666881" +
                "16427171479924442928230863465674813919123162824586" +
                "17866458359124566529476545682848912883142607690042" +
                "24219022671055626321111109370544217506941658960408" +
                "07198403850962455444362981230987879927244284909188" +
                "84580156166097919133875499200524063689912560717606" +
                "05886116467109405077541002256983155200055935729725" +
                "71636269561882670428252483600823257530420752963450"
        assertEquals(5832L, tool.largestSeriesProduct(number, 1000, 4))
        assertEquals(23514624000, tool.largestSeriesProduct(number, 1000, 13))
    }
}