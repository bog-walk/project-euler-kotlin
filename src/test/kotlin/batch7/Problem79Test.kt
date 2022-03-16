package batch7

import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.getTestResource
import kotlin.test.assertNull

internal class PasscodeDerivationTest {
    private val tool = PasscodeDerivation()

    @Test
    fun `HR problem returns null for invalid passcode`() {
        val logins = listOf(
            listOf("an0", "n/.", ".#a"), listOf("123", "231")
        )
        for (login in logins) {
            assertNull(tool.derivePasscode(login))
            assertNull(tool.derivePassCodeGraph(login))
        }
    }

    @Test
    fun `HR problem correct for valid passcodes`() {
        val logins = listOf(
            listOf("abc"),
            listOf("SMH", "TON", "RNG", "WRO", "THG"),
            listOf("@<!", "@!3", "<R3", "@!R", "<!3", "@R3")
        )
        val expected = listOf("abc", "SMTHWRONG", "@<!R3")
        for ((i, e) in expected.withIndex()) {
            assertEquals(e, tool.derivePasscode(logins[i]))
            assertEquals(e, tool.derivePassCodeGraph(logins[i]))
        }
    }

    @Test
    fun `PE problem correct for 50 login attempts`() {
        val logins = getTestResource("src/test/resources/PasscodeDerivation.txt").distinct()
        val expected = "73162890"
        assertEquals(expected, tool.derivePasscode(logins))
        assertEquals(expected, tool.derivePassCodeGraph(logins))
    }
}