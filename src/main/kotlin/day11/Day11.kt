fun main() {

    fun containsPairs(s: String, n: Int): Boolean {
        val pairs: MutableList<Int> = mutableListOf()

        s.forEachIndexed { i,c ->
            if(i > 0 && s[i-1] == c) {
                pairs.add(i)
            }
        }

        // count pairs (must be non overlapping so index must be more than one apart)

        var count = 0

        pairs.forEachIndexed { i,pos ->
            if(i == 0) {
                count ++
            } else if(pos - pairs[i-1] > 1) {
                count ++
            }
        }

        return count >= n
    }

    fun containsConfusing(s: String): Boolean {
        return s.contains(Regex("[iol]"))
    }

    fun containsStraight(s: String, size: Int): Boolean {
        var mostIncs = 1

        s.forEachIndexed { i, c ->
            if(i > 0) {
                if(c - 1 == s[i - 1]) {
                    mostIncs ++
                    if(mostIncs == size) {
                        return true
                    }
                } else {
                    mostIncs = 1
                }
            }
        }
        return false
    }

    println(containsStraight("123", 3) == true)
    println(containsStraight("12 3", 3) == false)
    println(containsStraight(" 12 123 12", 3) == true)
    println(containsStraight(" 12 abc 12", 3) == true)

    println(containsConfusing("sol") == true)
    println(containsConfusing("sad") == false)

    println(containsPairs("aabb",2) == true)
    println(containsPairs("aa",2) == false)
    println(containsPairs("aaa",2) == false)

    fun isValid(s: String): Boolean {
        return containsPairs(s,2) && containsStraight(s,3) && !containsConfusing(s)
    }

    println(isValid("hijklmmn") == false)
    println(isValid("abbceffg") == false)
    println(isValid("abbcegjk") == false)

    // Incrementing

    fun inc(s: String): String {
        val b = StringBuilder(s)

        var i = b.length - 1

        while(i >= 0) {
            if(b.get(i) == 'z') {
                b.set(i,'a')
                i --
            } else {
                b.set(i, b.get(i) + 1)
                break
            }
        }

        return b.toString()
    }

    println(inc("aa") == "ab")
    println(inc("az") == "ba")
    println(inc("czz") == "daa")

    fun solve(s: String): String {
        var newPassword: String = s

        do {
            newPassword = inc(newPassword)
        } while(!isValid(newPassword))

        return newPassword
    }

    println(solve("abcdefgh") == "abcdffaa")
    println(solve("ghijklmn") == "ghjaabcc")
    println(solve("cqjxjnds"))
    println(solve("cqjxxyzz"))
}