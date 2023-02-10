package dev.bogwalk.util.tests

import kotlin.math.pow

internal fun fastFake(n: Int): Int {
    return n * n * 100
}

internal fun mediumFake(n: Int): Int {
    Thread.sleep(10L)
    return n * n * 100
}

internal fun slowFake(n: Int): Int {
    Thread.sleep(1000L)
    return n * n * 100
}

internal fun fakeA(n: Int): Int {
    return n * n * 100
}
internal fun fakeB(base: Int, exp: Int): Int {
    var ans = 1
    repeat(exp) {
        ans *= base
    }
    return ans * 100
}

internal fun fakeC(base: Int, exp: Int): Int {
    return (1.0 * base).pow(exp).toInt() * 100
}

internal fun sleepA() {
    Thread.sleep(1L)
}

internal fun sleepB() {
    Thread.sleep(10L)
}

internal fun sleepC() {
    Thread.sleep(100L)
}

internal fun sleepD() {
    Thread.sleep(1000L)
}