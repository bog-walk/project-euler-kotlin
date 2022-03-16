package batch7

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
    fun derivePasscode(logins: List<String>): String? {
        val passcode = mutableListOf<Char>()
        val patterns = mutableListOf<Regex>()
        for (login in logins) {
            patterns.add(login.toCharArray().joinToString(".*").toRegex())
            print("Login: $login ")
            var indices = login.map { passcode.indexOf(it) }
            print("$indices ")
            var alreadySeen = indices.filter { it != -1 }
            if (alreadySeen.isEmpty()) {
                println("All new values added")
                passcode.addAll(login.toList())
                println("Password: $passcode")
                continue
            }
            val expected = alreadySeen.sorted()
            var swapped = false
            if (expected != alreadySeen) {
                println("Something went wrong")
                val toSwap = mutableListOf<Int>()
                for (i in expected.indices) {
                    if (expected[i] != alreadySeen[i]) toSwap.add(expected[i])
                }
                println("Swap $toSwap")
                val (a, b) = toSwap
                passcode[a] = passcode[b].also {
                    passcode[b] = passcode[a]
                }
                indices = login.map { passcode.indexOf(it) }
                println("Swapped -> $indices")
                alreadySeen = indices.filter { it != -1 }
                swapped = true
            }
            if (alreadySeen.size == 3 && !swapped) {
                println("No need to check")
                continue
            }
            if (alreadySeen.size == 2) {
                print("\nOnly 1 new char create password ")
                when (indices.indexOf(-1)) {
                    0 -> passcode.add(indices[1], login[0])
                    1 -> passcode.add(indices[2], login[1])
                    else -> {
                        if (indices[1] >= passcode.lastIndex) {
                            passcode.add(login[2])
                        } else {
                            passcode.add(indices[1] + 1, login[2])
                        }
                    }
                }
                println("$passcode")
            }
            if (alreadySeen.size == 1) {
                print("\n2 new chars create password ")
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
                    else -> passcode.addAll(indices[2], login.take(2).toList())
                }
                println("$passcode")
            }
            for (pattern in patterns) {
                if (pattern.containsMatchIn(passcode.joinToString(""))) {
                    println("Attempts hold")
                } else {
                    println("No match for $pattern")
                    return null
                }
            }
        }
        return passcode.joinToString("")
    }
}

fun main() {
    val tool = PasscodeDerivation()
    val logins = listOf("@<!", "@!3", "<R3", "@!R", "<!3", "@R3")
    val passcode = tool.derivePasscode(logins)
    println(passcode)
}





