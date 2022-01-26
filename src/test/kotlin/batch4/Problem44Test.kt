package batch4

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PentagonNumbersTest {
    private val tool = PentagonNumbers()

    @Test
    fun testPentagonNumbersHR_low() {
        val n = 10
        val ks = listOf(1, 2, 3)
        val expected = listOf(setOf(92L), setOf(70L), setOf(70L))
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun testPentagonNumbersHR_mid() {
        val n = 1000
        val ks = listOf(10, 20)
        val expected = listOf(
            setOf<Long>(715, 1247, 1926, 7957, 8855, 69230, 92877, 173230,
                199655, 336777, 1078232),
            setOf<Long>(6305, 73151, 82720, 270725, 373252, 747301)
        )
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun testPentagonNumbersHR_high() {
        val n = 100000
        val ks = listOf(1000, 5000)
        val expected = listOf(
            setOf(16832075, 34485640, 78253982, 215310551, 472957695,
                661804535, 937412502, 3345135652, 5174142370, 7562742551, 10836947507),
            setOf(323114155, 891564410, 1141136295, 3802809626)
        )
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun testPentagonNumbersPE() {
        assertEquals(5482660L, tool.pentagonNumbersPE())
    }
}