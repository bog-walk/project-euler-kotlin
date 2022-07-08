package dev.bogwalk.batch5

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.getTestResource

internal class XORDecryptionTest {
    private val tool = XORDecryption()

    @Test
    fun `HR problem correct when message only has letters`() {
        val encryption = listOf(
            32, 66, 50, 20, 11, 0, 42, 66, 33, 19, 13, 20, 47, 66, 37, 14, 58, 67, 43, 23, 14,
            17, 49, 67, 46, 20, 6, 51, 66, 55, 9, 39, 67, 45, 3, 25, 56, 66, 39, 14, 37, 34, 65,
            51, 22, 8, 1, 40, 65, 32, 17, 14, 21, 45, 65, 36, 12, 57, 66, 41, 20, 15, 19, 50, 66,
            44, 23, 7, 49, 65, 54, 11, 36, 66, 47, 0, 24, 58, 65, 38, 12, 38
        )
        val expected = "abc"
        assertEquals(expected, tool.xorDecryption(encryption))
    }

    @Test
    fun `HR problem correct when message has letters & digits`() {
        val encryption = listOf(
            50, 9, 13, 21, 65, 13, 21, 65, 85, 70, 18, 5, 11, 17, 8, 3, 65, 9, 3, 18, 23, 7, 6,
            1, 70, 22, 13, 18, 9, 68, 87, 81, 68, 2, 8, 3, 15, 21, 23, 70, 8, 10, 70, 8, 16, 46,
            4, 22, 3, 65, 13, 21, 65, 87, 70, 0, 10, 2, 65, 86, 95, 65, 5, 8, 5, 68, 82, 81, 84,
            83, 65, 5, 8, 5, 68, 18, 9, 1, 8, 65, 5, 70, 22, 12, 9, 13, 1, 70, 3, 17, 8, 2, 12,
            70, 14, 2, 70, 22, 11, 20, 5, 23, 70, 21, 11, 70, 7, 13, 10, 13, 68, 18, 9, 1, 70, 18,
            13, 28, 4, 68, 20, 4, 21, 19, 8, 22, 3, 12, 1, 8, 21
        )
        val expected = "fad"
        assertEquals(expected, tool.xorDecryption(encryption))
    }

    @Test
    fun `HR problem correct when message has letters, digits, & punctuations`() {
        val filepaths = listOf(
            "src/test/resources/XORDecryptionLDP1.txt", "src/test/resources/XORDecryptionLDP2.txt"
        )
        val expected = listOf("qzd", "lse")
        for ((i, filepath) in filepaths.withIndex()) {
            val encryption = getTestResource(
                filepath, ", \n".toCharArray(), ", "
            ) {
                it.toInt()
            }.flatten()
            assertEquals(expected[i], tool.xorDecryption(encryption))
        }
    }

    @Test
    fun `PE problem correct`() {
        val expected = 129_448
        val encryption = getTestResource(
            "src/test/resources/XORDecryptionPE.txt", ", \n".toCharArray(), ","
            ) {
                it.toInt()
            }.flatten()
            assertEquals(expected, tool.sumOfDecryptedCodes(encryption))
    }
}