package dev.bogwalk.util.tests

import java.io.File
import kotlin.system.measureNanoTime

internal typealias Benchmark = Triple<Long, Long, Long>
private typealias ZeroArgFunc<R> = () -> R
private typealias SingleArgFunc<T, R> = (T) -> R
private typealias DoubleArgFunc<T, Q, R> = (T, Q) -> R
private typealias TripleArgFunc<T, Q, R> = (T, Q, Q) -> R

/**
 * Using KFunction.call() led to erroneous speed differences, always shown in the first list
 * element's speed output (~600ms extra compared to normal test), regardless of order. Assumed this
 * had something to do with reflection (or the need for warmup) & started from scratch.
 *
 * The only way to avoid using KFunction is by hardcoding function type into overloaded functions
 * with different parameter amounts and types. This is very unattractive, but it works and
 * a more elegant solution is still being sought.
 */
fun <R : Any> getSpeed(
    solution: ZeroArgFunc<R>,
    warmup: Int = 0,
    repeat: Int = 1
): Pair<R, Benchmark> {
    val result: R
    for (i in 0 until warmup) {
        solution()
    }
    var time = measureNanoTime {
        result = solution()
    }
    var best = time
    var worst = time
    for (i in 1 until repeat) {
        val currentTime = measureNanoTime { solution() }
        time += currentTime
        best = minOf(best, currentTime)
        worst = maxOf(worst, currentTime)
    }
    return result to Triple(best, time / repeat, worst)
}

fun <T : Any, R : Any> getSpeed(
    solution: SingleArgFunc<T, R>,
    arg: T,
    warmup: Int = 0,
    repeat: Int = 1
): Pair<R, Benchmark> {
    val result: R
    for (i in 0 until warmup) {
        solution(arg)
    }
    var time = measureNanoTime {
        result = solution(arg)
    }
    var best = time
    var worst = time
    for (i in 1 until repeat) {
        val currentTime = measureNanoTime { solution(arg) }
        time += currentTime
        best = minOf(best, currentTime)
        worst = maxOf(worst, currentTime)
    }
    return result to Triple(best, time / repeat, worst)
}

/**
 * For some reason, this causes slightly slower speeds if solutions provided from a dictionary
 * or if used with solutions that have different argument amounts. It works as expected if each
 * solution is manually called, rather than looped over.
 */
fun <T : Any, Q: Any, R : Any> getSpeed(
    solution: DoubleArgFunc<T, Q, R>,
    arg1: T,
    arg2: Q,
    warmup: Int = 0,
    repeat: Int = 1
): Pair<R, Benchmark> {
    val result: R
    for (i in 0 until warmup) {
        solution(arg1, arg2)
    }
    var time = measureNanoTime {
        result = solution(arg1, arg2)
    }
    var best = time
    var worst = time
    for (i in 1 until repeat) {
        val currentTime = measureNanoTime { solution(arg1, arg2) }
        time += currentTime
        best = minOf(best, currentTime)
        worst = maxOf(worst, currentTime)
    }
    return result to Triple(best, time / repeat, worst)
}

fun <T : Any, Q: Any, R : Any> getSpeed(
    solution: TripleArgFunc<T, Q, R>,
    arg1: T,
    arg2: Q,
    arg3: Q,
    warmup: Int = 0,
    repeat: Int = 1
): Pair<R, Benchmark> {
    val result: R
    for (i in 0 until warmup) {
        solution(arg1, arg2, arg3)
    }
    var time = measureNanoTime {
        result = solution(arg1, arg2, arg3)
    }
    var best = time
    var worst = time
    for (i in 1 until repeat) {
        val currentTime = measureNanoTime { solution(arg1, arg2, arg3) }
        time += currentTime
        best = minOf(best, currentTime)
        worst = maxOf(worst, currentTime)
    }
    return result to Triple(best, time / repeat, worst)
}

/**
 * Compares speed of multiple functions, sorts them from fastest to slowest, and prints each
 * speed in nanoseconds (scientific notation), milliseconds, or seconds (to the required
 * precision), depending on the size of the speed value.
 *
 * Output is formatted as a table to show the best, average, and worst execution times.
 */
fun compareSpeed(speeds: List<Pair<String, Benchmark>>, precision: Int = 2) {
    val template = "%-20s%-12s%-12s%-12s"
    println(template.format("Solution", "Best", "Average", "Worst"))
    println()
    speeds
        .sortedBy { it.second.second }
        .forEach { (name, times) ->
            val timesF = times.toList().map { it.formatSpeed(precision) }
            println(template.format(name, timesF[0], timesF[1], timesF[2]))
        }
}

/**
 * A simplified version that accepts an individual execution time, in the event a manual
 * measureNanoTime() is run on a solution.
 */
fun compareSpeed(speed: Pair<String, Long>, precision: Int = 2) {
    println("${speed.first} solution took: ${speed.second.formatSpeed(precision)}")
}

private fun Long.formatSpeed(precision: Int): String {
    return when {
        this >= 1_000_000_000 -> "%.${precision}fs".format(1.0 * this / 1_000_000_000)
        this >= 1_000_000 -> "%.2fms".format(1.0 * this / 1_000_000)
        this >= 10_000 -> "%.1ens".format(1.0 * this)
        else -> "${this}ns"
    }
}

/**
 * Transforms content of a test resource file into a 2-dimensional grid of size [gridSize], with
 * each grid row corresponding to an IntArray.
 *
 * @param [lineSplit] characters to use as the delimiter when splitting a line.
 */
fun getTestIntGrid(
    filePath: String,
    gridSize: Int,
    lineSplit: String = " "
): Array<IntArray> {
    val input = getTestResource(filePath, lineSplit = lineSplit, transformation = String::toInt)
    return Array(gridSize) { input[it].toIntArray() }
}

/**
 * Transforms content of a test resource file into a 2-dimensional grid of size [gridSize], with
 * each grid row corresponding to an LongArray.
 *
 * @param [lineSplit] characters to use as the delimiter when splitting a line.
 */
fun getTestLongGrid(
    filePath: String,
    gridSize: Int,
    lineSplit: String = " "
): Array<LongArray> {
    val input = getTestResource(filePath, lineSplit = lineSplit, transformation = String::toLong)
    return Array(gridSize) { input[it].toLongArray() }
}

/**
 * Retrieves content of a test resource file, with each line returned as an unaltered String.
 *
 * @param [lineTrim] characters to remove from the left and right of each file line.
 */
fun getTestResource(
    filePath: String,
    lineTrim: CharArray = charArrayOf(' ', '\n')
): List<String> {
    return File(filePath).useLines { lines ->
        lines.map { line ->
            line.trim(*lineTrim)
        }.toList()
    }
}

/**
 * Retrieves content of a test resource file, with each line transformed into a nested list.
 *
 * @param [lineTrim] characters to remove from the left & right of each file line.
 * @param [lineSplit] characters to use as the delimiter when splitting a line.
 * @param [transformation] transformation function that takes either an entire line as an
 * argument or, if split, individual elements in a line.
 */
fun <R> getTestResource(
    filePath: String,
    lineTrim: CharArray = charArrayOf(' ', '\n'),
    lineSplit: String = " ",
    transformation: (String) -> R
): List<List<R>> {
    return File(filePath).useLines { lines ->
        lines.map { line ->
            line.trim(*lineTrim).split(lineSplit).map(transformation)
        }.toList()
    }
}