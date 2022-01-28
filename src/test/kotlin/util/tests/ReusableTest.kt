package util.tests

import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class ReusableTest {
    @Test
    fun `compareSpeed correct with zero argument functions`() {
        val solutions = mapOf(
            "SleepA" to ::sleepA, "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            getSpeed(solution).run {
                speeds.add(name to this.second)
            }
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
        val results = mutableListOf<Int>()
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            getSpeed(solution.first, solution.second).run {
                results.add(this.first)
                speeds.add(name to this.second)
            }
        }
        assertTrue { results.all { expected == it } }
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
        val results = mutableListOf<Int>()
        val speeds = mutableListOf<Pair<String, Long>>()
        getSpeed(::fakeA, n).run {
            results.add(this.first)
            speeds.add("FakeA" to this.second)
        }
        getSpeed(::fakeB, n, 2).run {
            results.add(this.first)
            speeds.add("FakeB" to this.second)
        }
        getSpeed(::fakeC, n, 2).run {
            results.add(this.first)
            speeds.add("FakeC" to this.second)
        }
        assertTrue { results.all { expected == it } }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed normal multiple args`() {
        val n = 3
        val expected = 900
        val speeds = mutableListOf<Pair<String, Long>>()
        val results = mutableListOf<Int>()
        val aTime = measureNanoTime {
            results.add(fakeA(n))
        }
        speeds.add("FakeA" to aTime)
        val bTime = measureNanoTime {
            results.add(fakeB(n, 2))
        }
        speeds.add("FakeB" to bTime)
        val cTime = measureNanoTime {
            results.add(fakeC(n, 2))
        }
        speeds.add("FakeC" to cTime)
        assertTrue { results.all { expected == it } }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed correct with multiple repetitions`() {
        val solutions = mapOf(
            "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            getSpeed(solution, repeat = 10).run {
                speeds.add(name to this.second)
            }
            assertEquals(1, 1)
        }
        compareSpeed(speeds)
    }

    @Test
    fun `compareSpeed normal multiple repetitions`() {
        val solutions = mapOf(
            "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
        )
        val speeds = mutableListOf<Pair<String, Long>>()
        for ((name, solution) in solutions.entries) {
            val time = measureNanoTime {
                for (i in 0 until 10) {
                    solution()
                }
            }
            speeds.add(name to time)
            assertEquals(1, 1)
        }
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