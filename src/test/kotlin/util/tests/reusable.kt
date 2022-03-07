package util.tests

import java.io.File
import kotlin.system.measureNanoTime

private typealias ZeroArgFunc<R> = () -> R
private typealias SingleArgFunc<T, R> = (T) -> R
private typealias DoubleArgFunc<T, Q, R> = (T, Q) -> R
private typealias TripleArgFunc<T, Q, R> = (T, Q, Q) -> R

/**
 * Using KFunction.call() led to erroneous speed differences, always shown in the first list
 * element's speed output (~600ms extra compared to normal test), regardless of order. Assumed this
 * had something to do with reflection & started from scratch.
 *
 * The only way to avoid using KFunction is by hardcoding function type into overloaded functions
 * with different parameter amounts and types. This is very unattractive, but it works and
 * hopefully one day you'll be smart enough to find a more elegant solution, like you did in PY.
 */
fun <R : Any> getSpeed(
    solution: ZeroArgFunc<R>,
    repeat: Int = 1
): Pair<R, Long> {
    val result: R
    val time = measureNanoTime {
        for (i in 0 until repeat - 1) {
            solution()
        }
        result = solution()
    }
    return result to time
}

fun <T : Any, R : Any> getSpeed(
    solution: SingleArgFunc<T, R>,
    arg: T,
    repeat: Int = 1
): Pair<R, Long> {
    val result: R
    val time = measureNanoTime {
        for (i in 0 until repeat - 1) {
            solution(arg)
        }
        result = solution(arg)
    }
    return result to time
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
    repeat: Int = 1
): Pair<R, Long> {
    val result: R
    val time = measureNanoTime {
        for (i in 0 until repeat - 1) {
            solution(arg1, arg2)
        }
        result = solution(arg1, arg2)
    }
    return result to time
}

fun <T : Any, Q: Any, R : Any> getSpeed(
    solution: TripleArgFunc<T, Q, R>,
    arg1: T,
    arg2: Q,
    arg3: Q,
    repeat: Int = 1
): Pair<R, Long> {
    val result: R
    val time = measureNanoTime {
        for (i in 0 until repeat - 1) {
            solution(arg1, arg2, arg3)
        }
        result = solution(arg1, arg2, arg3)
    }
    return result to time
}

/**
 * Compares speed of multiple functions, sorts them from fastest to slowest, and prints each
 * speed in nanoseconds (scientific notation), milliseconds, or seconds (to the required
 * precision), depending on the size of the speed value.
 */
fun compareSpeed(speeds: List<Pair<String, Long>>, precision: Int = 2) {
    speeds
        .sortedBy { it.second }
        .forEach { (name, time) ->
            val timeF = when {
                time >= 1_000_000_000 -> {
                    "%.${precision}fs".format(1.0 * time / 1_000_000_000)
                }
                time >= 1_000_000 -> {
                    "%.2fms".format(1.0 * time / 1_000_000)
                }
                time >= 10_000 -> {
                    "%.1ens".format(1.0 * time)
                }
                else -> "${time}ns"
            }
            println("$name solution took: $timeF")
        }
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
    val resource = mutableListOf<String>()
    File(filePath).useLines { lines ->
        lines.forEach { line ->
            resource.add(line.trim(*lineTrim))
        }
    }
    return resource
}

/**
 * Retrieves content of a test resource file, with each line transformed into a nested list.
 *
 * @param [lineTrim] characters to remove from the left & right of each file line.
 * @param [lineSplit] characters to use as the delimiter when splitting a line.
 * @param [transformation] transformation function that takes either an entire line as an
 * argument or, if split, individual elements in a line.
 */
fun <R : Any> getTestResource(
    filePath: String,
    lineTrim: CharArray = charArrayOf(' ', '\n'),
    lineSplit: String = " ",
    transformation: (String) -> R
): List<List<R>> {
    val resource = mutableListOf<List<R>>()
    File(filePath).useLines { lines ->
        lines.forEach { line ->
            val transformed = line.trim(*lineTrim).split(lineSplit).map(transformation)
            resource.add(transformed)
        }
    }
    return resource
}