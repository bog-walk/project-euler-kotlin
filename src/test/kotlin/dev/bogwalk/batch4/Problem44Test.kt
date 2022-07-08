package dev.bogwalk.batch4

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PentagonNumbersTest {
    private val tool = PentagonNumbers()

    @Test
    fun `HR problem correct for lower constraints`() {
        val n = 10
        val ks = listOf(1, 2, 3)
        val expected = listOf(setOf(92L), setOf(70L), setOf(70L))
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun `HR problem correct for mid constraints`() {
        val n = 1000
        val ks = listOf(10, 20)
        val expected = listOf(
            setOf<Long>(
                715, 1247, 1926, 7957, 8855, 69230, 92877, 173_230, 199_655, 336_777, 1_078_232
            ),
            setOf<Long>(6305, 73151, 82720, 270_725, 373_252, 747_301)
        )
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun `HR problem correct for upper constraints`() {
        val n = 100_000
        val ks = listOf(1000, 5000)
        val expected = listOf(
            setOf(
                16_832_075, 34_485_640, 78_253_982, 215_310_551, 472_957_695, 661_804_535,
                937_412_502, 3_345_135_652, 5_174_142_370, 7_562_742_551, 10_836_947_507
            ),
            setOf(323_114_155, 891_564_410, 1_141_136_295, 3_802_809_626)
        )
        for ((i, k) in ks.withIndex()) {
            assertEquals(expected[i], tool.pentagonNumbersHR(n, k))
        }
    }

    @Test
    fun `PE problem correct`() {
        val expected = 5_482_660
        assertEquals(expected, tool.pentagonNumbersPE())
    }
}