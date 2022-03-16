package batch7

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import util.combinatorics.combinations
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource
import kotlin.random.Random
import kotlin.test.assertNull

internal class PasscodeDerivationTest {
    private val tool = PasscodeDerivation()

    @Test
    fun `HR problem returns null for invalid passcode`() {
        val logins = listOf("an0", "n/.", ".#a")
        assertNull(tool.derivePasscode(logins))
    }

    @Test
    fun `HR problem correct for valid passcodes`() {
        val logins = listOf(
            listOf("SMH", "TON", "RNG", "WRO", "THG"),
            listOf("@<!", "@!3", "<R3", "@!R", "<!3", "@R3")
        )
        val expected = listOf("SMTHWRONG", "@<!R3")
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.derivePasscode(logins[i]))
        }
    }

    @Test
    fun `HR problem correct for random passcode`() {
        val length = 10
        val passcode = List(length) {
            Random.nextInt(33, 127).toChar()
        }.joinToString("")
        val logins = combinations((0..9), 3).toList().shuffled().take(20).map {
            it.map { i -> passcode[i] }.joinToString("")
        }
        val actual = tool.derivePasscode(logins)
        assertEquals(passcode, actual)
    }

    @Test
    fun `PE problem correct for 50 login attempts`() {
        val logins = getTestResource("src/test/resources/PasscodeDerivation.txt").distinct()
        val expected = "73162890"
        assertEquals(expected, tool.derivePasscode(logins))
    }
}