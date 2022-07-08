package dev.bogwalk.util.tests

import kotlin.math.pow

fun fastFake(n: Int): Int {
    return n * n * 100
}

fun mediumFake(n: Int): Int {
    Thread.sleep(10L)
    return n * n * 100
}

fun slowFake(n: Int): Int {
    Thread.sleep(1000L)
    return n * n * 100
}

fun fakeA(n: Int): Int {
    return n * n * 100
}
fun fakeB(base: Int, exp: Int): Int {
    var ans = 1
    repeat(exp) {
        ans *= base
    }
    return ans * 100
}

fun fakeC(base: Int, exp: Int): Int {
    return (1.0 * base).pow(exp).toInt() * 100
}

fun sleepA() {
    Thread.sleep(1L)
}

fun sleepB() {
    Thread.sleep(10L)
}

fun sleepC() {
    Thread.sleep(100L)
}

fun sleepD() {
    Thread.sleep(1000L)
}