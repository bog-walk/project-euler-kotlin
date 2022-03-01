package batch6

import kotlin.test.Test
import kotlin.test.assertEquals
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource
import kotlin.system.measureNanoTime

internal class MaximumPathSum2Test {
    private val tool = MaximumPathSum2()

    @Test
    fun `HR problem correct for 30 row pyramid`() {
        val n = 30
        val expected = 2156
        val input = getTestResource(
            "src/test/resources/MaximumPathSum2With30Rows"
        ) { it.toInt() }
        val elements = input.flatten().toIntArray()
        val pyramid = input.map { it.toIntArray() }.toTypedArray()

        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::maxPathSumDynamic, n, pyramid).run {
            speeds.add("Dynamic" to second)
            assertEquals(expected, first)
        }
        val customActual: Int
        val customTime = measureNanoTime {
            customActual = tool.maxPathSum(n, *elements)
        }
        speeds.add("Custom" to customTime)
        assertEquals(expected, customActual)
        compareSpeed(speeds)
    }

    @Test
    fun `HR problem correct for 35 row pyramid`() {
        val n = 35
        val expected = 2487
        val input = getTestResource(
            "src/test/resources/MaximumPathSum2With35Rows"
        ) { it.toInt() }
        val elements = input.flatten().toIntArray()
        val pyramid = input.map { it.toIntArray() }.toTypedArray()

        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(tool::maxPathSumDynamic, n, pyramid).run {
            speeds.add("Dynamic" to second)
            assertEquals(expected, first)
        }
        val customActual: Int
        val customTime = measureNanoTime {
            customActual = tool.maxPathSum(n, *elements)
        }
        speeds.add("Custom" to customTime)
        assertEquals(expected, customActual)
        compareSpeed(speeds)
    }

    @Test
    fun `PE problem correct for 100 row pyramid`() {
        val n = 100
        val expected = 7273
        val pyramid = getTestResource(
            "src/test/resources/MaximumPathSum2With100Rows"
        ) { it.toInt() }.map { it.toIntArray() }.toTypedArray()

        getSpeed(tool::maxPathSumDynamic, n, pyramid).run {
            compareSpeed(listOf("Dynamic" to second))
            assertEquals(expected, first)
        }
    }
}