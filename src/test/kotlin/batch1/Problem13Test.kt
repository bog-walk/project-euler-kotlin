package batch1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import util.tests.Benchmark
import util.tests.compareSpeed
import util.tests.getSpeed
import util.tests.getTestResource
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LargeSumTest {
    private val tool = LargeSum()
    private val threeDigs = listOf("123", "456", "789", "812", "234")
    private val tenDigs = listOf("6041184107", "5351558590", "1833324270")
    private val fiveFiftyDigs = getTestResource("src/test/resources/LargeSum5N.txt")
    private val hundredFiftyDigs = getTestResource("src/test/resources/LargeSum100N.txt")
    private val thousandFiftyDigs = getTestResource("src/test/resources/LargeSum1000N.txt")

    @Test
    fun `setUp correct`() {
        assertEquals(5, fiveFiftyDigs.size)
        assertEquals(50, fiveFiftyDigs.first().length)
        assertEquals(100, hundredFiftyDigs.size)
        assertEquals(1000, thousandFiftyDigs.size)
    }

    @Test
    fun `correct when N = 1`() {
        val number = listOf("123456789123456789")
        val expected = "1234567891"
        assertEquals(expected, tool.addInReverse(number))
        assertEquals(expected, tool.sliceSum(number))
    }

    @Test
    fun `correct when numbers have few digits`() {
        val expected = "2414"
        assertEquals(expected, tool.addInReverse(threeDigs))
        assertEquals(expected, tool.sliceSum(threeDigs))
    }

    @Test
    fun `correct when numbers have medium amount of digits`() {
        val expected = "1322606696"
        assertEquals(expected, tool.addInReverse(tenDigs))
        assertEquals(expected, tool.sliceSum(tenDigs))
    }

    @Test
    fun `correct when number has max amount of digits`() {
        val expected = "2728190129"
        assertEquals(expected, tool.addInReverse(fiveFiftyDigs))
        assertEquals(expected, tool.sliceSum(fiveFiftyDigs))
    }

    @Test
    fun `speed for max digits with mid constraint N`() {
        val solutions = mapOf(
            "Manual" to tool::addInReverse, "BigInteger" to tool::sliceSum
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        val results = mutableListOf<String>()
        for ((name, solution) in solutions) {
            getSpeed(solution, hundredFiftyDigs).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        assertEquals(results[0], results[1])
    }

    @Test
    fun `speed for max digits with upper constraint N`() {
        val solutions = mapOf(
            "Manual" to tool::addInReverse, "BigInteger" to tool::sliceSum
        )
        val speeds = mutableListOf<Pair<String, Benchmark>>()
        val results = mutableListOf<String>()
        for ((name, solution) in solutions) {
            getSpeed(solution, thousandFiftyDigs).run {
                speeds.add(name to second)
                results.add(first)
            }
        }
        compareSpeed(speeds)
        assertEquals(results[0], results[1])
    }
}