package dev.bogwalk.util.tests

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException
import kotlin.system.measureNanoTime
import kotlin.test.*

internal class ReusableTest {
    @Nested
    @DisplayName("compareSpeed() test suite")
    inner class CompareSpeed {
        @Test
        fun `correct with zero argument functions`() {
            val solutions = mapOf(
                "SleepA" to ::sleepA, "SleepB" to ::sleepB,
                "SleepC" to ::sleepC, "SleepD" to ::sleepD
            )
            val speeds = mutableListOf<Pair<String, Benchmark>>()
            for ((name, solution) in solutions.entries) {
                getSpeed(solution).run {
                    speeds.add(name to this.second)
                    assertIs<Unit>(this.first)
                }
            }
            compareSpeed(speeds)
        }

        @Test
        fun `correct with normal zero args`() {
            val solutions = mapOf(
                "SleepA" to ::sleepA, "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
            )
            for ((name, solution) in solutions.entries) {
                val actual: Unit
                val time = measureNanoTime {
                    actual = solution()
                }
                assertIs<Unit>(actual)
                compareSpeed(name to time)
            }
        }

        @Test
        fun `correct with single argument functions`() {
            val n = 3
            val expected = 900
            val solutions = mapOf(
                "Fast" to ::fastFake, "Medium" to ::mediumFake, "Slow" to ::slowFake
            )
            val speeds = mutableListOf<Pair<String, Benchmark>>()
            for ((name, solution) in solutions.entries) {
                getSpeed(solution, n).run {
                    speeds.add(name to this.second)
                    assertEquals(expected, this.first)
                }
            }
            compareSpeed(speeds)
        }

        @Test
        fun `correct with normal one arg`() {
            val n = 3
            val expected = 900
            val solutions = mapOf(
                "Fast" to ::fastFake, "Medium" to ::mediumFake, "Slow" to ::slowFake
            )
            for ((name, solution) in solutions.entries) {
                val actual: Int
                val time = measureNanoTime {
                    actual = solution(n)
                }
                assertEquals(expected, actual)
                compareSpeed(name to time)
            }
        }

        @Test
        fun `correct with variable argument functions`() {
            val n = 3
            val expected = 900
            val results = mutableListOf<Int>()
            val speeds = mutableListOf<Pair<String, Benchmark>>()
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
        fun `correct with normal multiple args`() {
            val n = 3
            val expected = 900
            val results = mutableListOf<Int>()
            val aTime = measureNanoTime {
                results.add(fakeA(n))
            }
            compareSpeed("FakeA" to aTime)
            val bTime = measureNanoTime {
                results.add(fakeB(n, 2))
            }
            compareSpeed("FakeB" to bTime)
            val cTime = measureNanoTime {
                results.add(fakeC(n, 2))
            }
            compareSpeed("FakeC" to cTime)
            assertTrue { results.all { expected == it } }
        }

        @Test
        fun `correct with warmup and multiple repetitions`() {
            val solutions = mapOf(
                "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
            )
            val speeds = mutableListOf<Pair<String, Benchmark>>()
            for ((name, solution) in solutions.entries) {
                getSpeed(solution, warmup = 3, repeat = 10).run {
                    speeds.add(name to this.second)
                }
                assertEquals(1, 1)
            }
            compareSpeed(speeds)
        }

        @Test
        fun `correct with normal multiple repetitions`() {
            val solutions = mapOf(
                "SleepB" to ::sleepB, "SleepC" to ::sleepC, "SleepD" to ::sleepD
            )
            val repetitions = 10
            for ((name, solution) in solutions.entries) {
                val time = measureNanoTime {
                    for (i in 0 until repetitions) {
                        solution()
                    }
                }
                assertEquals(1, 1)
                compareSpeed(name to time / repetitions)
            }
        }
    }

    @Nested
    @DisplayName("getTestResource() test suite")
    inner class GetTestResource {
        @Test
        fun `correct for default retrieval`() {
            val path = "src/test/kotlin/dev/bogwalk/util/tests/fakeResource.txt"
            val expectedSize = 5
            val expectedLine = "1, 2, 3, 4, 5"
            val resource = getTestResource(path)
            assertEquals(expectedSize, resource.size)
            assertEquals(expectedLine, resource.first())
            assertIs<String>(resource.first())
        }

        @Test
        fun `correct for transformed retrieval`() {
            val path = "src/test/kotlin/dev/bogwalk/util/tests/fakeResource.txt"
            val expectedSize = 5
            val expectedLine = listOf("1A", "2A", "3A", "4A", "5A")
            val resource = getTestResource(path, lineSplit=", ") { it + 'A' }
            assertEquals(expectedSize, resource.size)
            assertEquals(expectedLine, resource.first())
            assertIs<List<String>>(resource.first())
        }

        @Test
        fun `throws exception if file not found`() {
            val path = "src/test/kotlin/dev/bogwalk/util/tests/notReal.txt"

            assertThrows<FileNotFoundException> { getTestResource(path) }
        }
    }

    @Nested
    @DisplayName("getTestGrid() test suite")
    inner class GetTestXGrid {
        @Test
        fun `getTestIntGrid() correct`() {
            val path = "src/test/kotlin/dev/bogwalk/util/tests/fakeResource.txt"
            val expectedSize = 5
            val expectedFirst = intArrayOf(1, 2, 3, 4, 5)
            val actual = getTestIntGrid(path, expectedSize, ", ")
            assertEquals(expectedSize, actual.size)
            assertContentEquals(expectedFirst, actual.first())
            assertIs<IntArray>(actual.first())
        }

        @Test
        fun `getTestLongGrid() correct`() {
            val path = "src/test/kotlin/dev/bogwalk/util/tests/fakeResource.txt"
            val expectedSize = 5
            val expectedFirst = longArrayOf(1, 2, 3, 4, 5)
            val actual = getTestLongGrid(path, expectedSize, ", ")
            assertEquals(expectedSize, actual.size)
            assertContentEquals(expectedFirst, actual.first())
            assertIs<LongArray>(actual.first())
        }
    }
}