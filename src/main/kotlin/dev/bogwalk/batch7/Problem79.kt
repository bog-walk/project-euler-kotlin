package dev.bogwalk.batch7

/**
 * Problem 79: Passcode Derivation
 *
 * https://projecteuler.net/problem=79
 *
 * Goal: Based on a collection of successful login attempts using 3 characters (ASCII codes [33,
 * 126]) in order (but not necessarily consecutive), determine the shortest (& lexicographically
 * smallest) original password of unknown length with only unique characters. If a password is
 * not possible, return a null string.
 *
 * Constraints: 1 <= login attempts <= 3000
 *
 * e.g.:
 *      attempts = {an0, n/., .#a}
 *      password = null
 *      1st attempt shows that 'n' must follow 'a'. 2nd attempt shows '.' must follow 'n'. But
 *      the last attempt has 'a' after '.' which should not be possible.
 */

class PasscodeDerivation {
    /**
     * Solution stores all connections between each login character & characters that potentially
     * precede it in the passcode, using a pseudo-graph.
     *
     * A stored character will be next in the passcode if it has degree 0 (i.e. no connecting
     * edges), with multiple choices being judged based on their lexicographic order (as the
     * cache is an array, the smallest will be the first element found that matches the predicate).
     *
     * Once a character is added to the passcode, its presence in the array is nullified & it is
     * removed from all characters that referenced it as an edge.
     *
     * A passcode is considered invalid/unattainable if at any point there are no degree 0
     * characters & a null value is returned.
     *
     * N.B. A dictionary could be used to cache all character nodes & references & would require
     * dictionary keys to be sorted to find the smallest insert for every iteration.
     *
     * SPEED (BETTER) 3.80ms for 50-login attempts
     */
    fun derivePasscodeImproved(logins: List<String>): String? {
        // reduce cache size to ASCII characters between 33 and 126 inclusive
        val offset = 33
        val connections = Array<Set<Int>?>(127 - offset) { null }
        for (login in logins) {
            // normalise login character ASCII to fit in cache
            val codes = login.map { it.code - offset }
            for (i in 0..2) {
                connections[codes[i]] = if (i == 0) {
                    // create a new set if a new character, otherwise preceding characters unknown
                    connections[codes[i]] ?: mutableSetOf()
                } else {
                    connections[codes[i]]?.plus(codes[i-1]) ?: mutableSetOf(codes[i-1])
                }
            }
        }
        var passcode = ""
        while (connections.any { it != null }) { // break loop if no more edges exist
            val smallest = connections.indexOfFirst { it?.isEmpty() == true }
            // break loop if no isolated characters exist
            if (smallest == -1) return null
            passcode += (smallest + offset).toChar()
            // remove all existence of the character just added
            connections[smallest] = null
            for (i in 0..93) {
                if (connections[i]?.contains(smallest) == true) {
                    connections[i] = connections[i]?.minus(smallest)
                }
            }
        }
        return passcode
    }

    /**
     * Solution assesses each login string & places each character into a list with minimal
     * movement or swapping based on the indices or pre-existing characters, if any.
     *
     * If characters are swapped or newly placed, the new passcode is matched with all login
     * Regex objects encountered so far, until either a match is not found (null value returned) or
     * all login attempts are processed (string value returned).
     *
     * e.g.
     *          "SMH" = [-1, -1, -1] -> [S, M, H]
     *          "TON" = [-1, -1, -1] -> [S, M, H, T, O, N]
     *          "RNG" = [-1, 5, -1] -> [S, M, H, T, O, R, N, G]
     *          "WRO" = [-1, 5, 4] !! swap required -> [S, M, H, T, R, O, N, G]
     *                  [-1, 4, 5] -> [S, M, H, T, W, R, O, N, G]
     *          "THG" = [3, 2, 8] !! swap required -> [S, M, T, H, W, R, O, N, G]
     *          passcode = SMTHWRONG
     *
     * N.B. This solution uses Regex & solves the Project Euler problem easily along with more
     * restricted test samples. However, this solution is not ideal for the more robust HackerRank
     * problems, particularly due to the span of characters allowed, which may cause either
     * dangling metacharacters or escape characters. It also does not allow for backtracking in
     * the event a swap is not enough to satisfy a new login Regex, leading to occasional false
     * null results.
     *
     * SPEED (WORSE) 13.06ms for 50-login attempts
     */
    fun derivePasscode(logins: List<String>): String? {
        val passcode = mutableListOf<Char>()
        val patterns = mutableListOf<Regex>()
        for (login in logins) {
            patterns.add(createRegex(login))
            var indices = login.map { passcode.indexOf(it) }
            var alreadySeen = indices.filter { it != -1 }
            if (alreadySeen.isEmpty()) { // all new characters added to end of list
                passcode.addAll(login.toList())
                continue
            }
            val expected = alreadySeen.sorted()
            var swapped = false
            if (expected != alreadySeen) { // order does not match new login attempt
                if (expected.size == 2) {
                    val toSwap = mutableListOf<Int>()
                    for (i in expected.indices) {
                        if (expected[i] != alreadySeen[i]) toSwap.add(expected[i])
                    }
                    val (a, b) = toSwap
                    passcode[a] = passcode[b].also {
                        passcode[b] = passcode[a]
                    }
                } else {
                    when (indices.indexOf(expected.first())) {
                        0 -> {
                            passcode.remove(login[1])
                            passcode.add(indices[2], login[1])
                        }
                        1 -> {
                            passcode.remove(login[0])
                            passcode.add(indices[1], login[0])
                        }
                        2 -> {
                            passcode.removeAll(login.take(2).toList())
                            val toAdd = expected.takeLast(2).map { login[it] }
                            passcode.addAll(indices[2], toAdd)
                        }
                    }
                }
                indices = login.map { passcode.indexOf(it) }
                alreadySeen = indices.filter { it != -1 }
                swapped = true
            }
            // login attempt fits well with current passcode
            if (alreadySeen.size == 3 && !swapped) continue
            if (alreadySeen.size == 2) {
                when (indices.indexOf(-1)) {
                    0 -> passcode.add(indices[1], login[0])
                    1 -> passcode.add(indices[0] + 1, login[1])
                    2 -> {
                        if (indices[1] >= passcode.lastIndex) {
                            passcode.add(login[2])
                        } else {
                            passcode.add(indices[1] + 1, login[2])
                        }
                    }
                }
            }
            if (alreadySeen.size == 1) {
                when (indices.indexOfFirst { it != -1 }) {
                    0 -> {
                        if (indices[0] >= passcode.lastIndex) {
                            passcode.addAll(login.takeLast(2).toList())
                        } else {
                            passcode.addAll(indices[0] + 1, login.takeLast(2).toList())
                        }
                    }
                    1 -> {
                        if (indices[1] >= passcode.lastIndex) {
                            passcode.add(login[2])
                        } else {
                            passcode.add(indices[1] + 1, login[2])
                        }
                        passcode.add(indices[1], login[0])
                    }
                    2 -> passcode.addAll(indices[2], login.take(2).toList())
                }
            }
            for (pattern in patterns) {
                if (!pattern.containsMatchIn(passcode.joinToString(""))) return null
            }
        }
        return passcode.joinToString("")
    }

    /**
     * Returns a Regex object based on the [login] attempt that ensures metacharacters are
     * escaped to allow no dangling during match checks.
     */
    private fun createRegex(login: String): Regex {
        var pattern = ""
        for ((i, ch) in login.withIndex()) {
            val next = if (ch in listOf('$', '*', '+', '.', '?', '\\')) """\$ch""" else "$ch"
            pattern += if (i < 2) "$next.*" else next
        }
        return pattern.toRegex()
    }
}