package dev.bogwalk.batch9

import dev.bogwalk.util.tests.Benchmark
import dev.bogwalk.util.tests.compareSpeed
import dev.bogwalk.util.tests.getSpeed
import kotlin.test.Test
import kotlin.test.assertEquals
import dev.bogwalk.util.tests.getTestResource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargestExponentialTest {
    private val tool = LargestExponential()
    private lateinit var resource: List<String>

    @BeforeAll
    fun setUp() {
        resource = getTestResource("src/test/resources/LargestExponential.txt")
    }

    @Test
    fun `HR problem correct for small exponentials`() {
        val inputs = listOf("4 7", "3 7", "2 11")
        val k = 2
        val expected = 3 to 7
        assertEquals(expected, tool.kSmallestExponential(inputs, k))
        assertEquals(expected, tool.kSmallestExponentialAlt(inputs, k))
    }

    @Test
    fun `HR problem correct for medium exponentials`() {
        val inputs = listOf("2 350", "5 150", "9 50", "7 850")
        val k = 1
        val expected = 9 to 50
        assertEquals(expected, tool.kSmallestExponential(inputs, k))
        assertEquals(expected, tool.kSmallestExponentialAlt(inputs, k))
    }

    @Test
    fun `HR problem correct for large exponentials`() {
        val inputs = listOf(
            "895447 504922", "44840 646067", "45860 644715", "463487 530404", "398164 536654",
            "894483 504959", "619415 518874"
        )
        val k = 7
        val expected = 895447 to 504922
        assertEquals(expected, tool.kSmallestExponential(inputs, k))
        assertEquals(expected, tool.kSmallestExponentialAlt(inputs, k))
    }

    @Test
    fun `HR problem speed`() {
        val inputs = resource.map { it.replace(',', ' ') }
        val k = 1000
        val solutions = mapOf(
            "Original" to tool::kSmallestExponential, "Alt" to tool::kSmallestExponentialAlt
        )
        val expected = 895447 to 504922
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        for ((name, solution) in solutions) {
            getSpeed(solution, inputs, k).run {
                speeds.add(name to second)
                assertEquals(expected, first)
            }
        }
        compareSpeed(speeds)
    }

    @Test
    fun `PE problem correct`() {
        val expected = Triple(895447, 504922, 709)
        val actual = tool.largestExponential(resource)
        assertEquals(expected, actual)
    }
}