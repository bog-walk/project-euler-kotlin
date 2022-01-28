package util.tests

import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ReusableTest {
    @Test
    fun `compareSpeed correct with zero argument functions`() {
        val solutions = mapOf(
            "SleepA" to ::sleepA, "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            val (_, time) = getSpeed(solution)
            speeds.add(name to time)
            assertEquals(1, 1)
        }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed normal zero args`() {
        val solutions = mapOf(
            "SleepA" to ::sleepA, "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            val time = measureNanoTime {
                solution()
            }
            speeds.add(name to time)
            assertEquals(1, 1)
        }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed correct with single argument functions`() {
        val n = 3
        val expected = 900
        val solutions = mapOf(
            "Fast" to (::fastFake to n),
            "Medium" to (::mediumFake to n),
            "Slow" to (::slowFake to n)
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            val (actual, time) = getSpeed(solution.first, solution.second)
            speeds.add(name to time)
            assertEquals(expected, actual)
        }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed normal one arg`() {
        val n = 3
        val expected = 900
        val solutions = mapOf(
            "Fast" to (::fastFake to n),
            "Medium" to (::mediumFake to n),
            "Slow" to (::slowFake to n)
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            val (function, arg) = solution
            val actual: Int
            val time = measureNanoTime {
                actual = function(arg)
            }
            speeds.add(name to time)
            assertEquals(expected, actual)
        }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed correct with variable argument functions`() {
        val n = 3
        val expected = 900
        val speeds = mutableListOf<Pair<String, Long>>()
        var result: Pair<Int, Long> = getSpeed(::fakeA, n, repeat=10)
        speeds.add("FakeA" to result.second)
        assertEquals(expected, result.first)
        result = getSpeed(::fakeB, n, 2, repeat=10)
        speeds.add("FakeB" to result.second)
        assertEquals(expected, result.first)
        result = getSpeed(::fakeC, n, 2, repeat=10)
        speeds.add("FakeC" to result.second)
        assertEquals(expected, result.first)
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed normal multiple args`() {
        val n = 3
        val expected = 900
        val speeds = mutableListOf<Pair<String, Long>>()
        var actual: Int
        val aTime = measureNanoTime {
            for (i in 0 until 9) {
                fakeA(n)
            }
            actual = fakeA(n)
        }
        speeds.add("FakeA" to aTime)
        assertEquals(expected, actual)
        val bTime = measureNanoTime {
            for (i in 0 until 9) {
                fakeB(n, 2)
            }
            actual = fakeB(n, 2)
        }
        speeds.add("FakeB" to bTime)
        assertEquals(expected, actual)
        val cTime = measureNanoTime {
            for (i in 0 until 9) {
                fakeC(n, 2)
            }
            actual = fakeC(n, 2)
        }
        speeds.add("FakeC" to cTime)
        assertEquals(expected, actual)
        compareSpeed(speeds)
    }

    @Test
    fun `getTestResource correct for default retrieval`() {
        val path = "src/test/kotlin/util/tests/fakeResource"
        val expectedSize = 5
        val expectedLine = "A, B, C, D, E, F"
        val resource = getTestResource(path)
        assertEquals(expectedSize, resource.size)
        assertEquals(expectedLine, resource.first())
        assertIs<String>(resource.first())
    }

    @Test
    fun `getTestResource correct for transformed retrieval`() {
        val path = "src/test/kotlin/util/tests/fakeResource"
        val expectedSize = 5
        val expectedLine = listOf("a", "b", "c", "d", "e", "f")
        val resource = getTestResource(path, lineSplit=", ") { it.lowercase() }
        assertEquals(expectedSize, resource.size)
        assertEquals(expectedLine, resource.first())
        assertIs<List<String>>(resource.first())
    }
}