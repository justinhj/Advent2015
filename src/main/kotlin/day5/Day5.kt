package day5


// This is defining an extension method on String
// work is a lambda function
fun String.asResource(work: (String) -> Unit) {
    val res = {}::class.java.getResource(this)
    val content = res.readText()
    work(content)
}

fun checkString(s: String, prev: Char, vowelCount: Int, doubles: Int): Boolean {

    // handle end of string
    if (s.length == 0) {
        if (vowelCount < 3)
            return false
        else if (doubles < 1)
            return false
        else
            return true
    } else {
        // Check for bad words ab, cd, pq, or xy,
        // possibly when can be used here
        if (s.first() == 'b' && prev == 'a') return false
        else if (s.first() == 'd' && prev == 'c') return false
        else if (s.first() == 'q' && prev == 'p') return false
        else if (s.first() == 'y' && prev == 'x') return false

        var vowelInc = 0
        var doubleInc = 0

        if (s.first() == prev) doubleInc = 1
        if ("aeiou".contains(s.first())) vowelInc = 1

        return checkString(s.substring(1), s.first(), vowelCount + vowelInc, doubles + doubleInc)
    }
}

fun String.isGood(): Boolean {

    return checkString(this, ' ', 0, 0)
}

fun main(args: Array<String>) {

    println()

    "day5/day5.txt".asResource {
        val lines = it.split("\n")
        println("found ${lines.size} lines")

        var goodCount = 0

        lines.forEach {
            if (it.isGood()) goodCount ++
        }
        println("$goodCount were good")
    }

    println("ugknbfddgicrmopn is good? ${"ugknbfddgicrmopn".isGood()}")

    println("jchzalrnumimnmhp is good? ${"jchzalrnumimnmhp".isGood()}")

}
