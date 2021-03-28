
// This is defining an extension method on String
// work is a lambda function
fun String.asResource(work: (String) -> Unit) {
    val res = {}::class.java.getResource(this)
    val content = res.readText()
    work(content)
}

// Must have at least one repeating pair xxx12xxx12xxxx
// Must have at least one instance of 121

// Make a map where key is the pair and the value is the position of the pair
// the position must be two apart for it to be valid

fun repeatedPair(s: String): Boolean {

    val pairs = (0 .. s.length - 2).toList().map {
        it ->
            Pair(Pair(s[it],s[it+1]),it)
    }

    // We can walk the pairs making a map. If a pair is already in the map and it is not
    // immediately before this one (i.e overlapping) we can return true

    val pairMap = mutableMapOf<Pair<Char,Char>,Int>()
    pairs.forEach {
        it ->
            val thisPos = it.second
            val lastPos = pairMap.get(it.first)
            if(lastPos != null) {
                // repeated pair in legal position (we can ignore illegal ones)
                if(thisPos > lastPos + 1)
                    return true;
            } else {
                pairMap.put(it.first,it.second)
            }
    }

    return false
}

// Find the pattern aba where b can be anything

fun findaba(s: String): Boolean {
    s.forEachIndexed() {
        ndx,c ->
            if(ndx <= (s.length - 3)) {
                if(c == s[ndx + 2])
                    return true
            }
    }
    return false
}

fun String.isGood(): Boolean {

    val repeat = repeatedPair(this)
    val aba = findaba(this)
    return repeat and aba
}

fun main(args: Array<String>) {

    "day5/day5.txt".asResource {
        val lines = it.split("\n")
        println("found ${lines.size} lines")

        var goodCount = 0

        lines.forEach {
            if(it.isGood()) goodCount += 1
        }

        println("$goodCount were good")
    }

    // test some repeated pairs

//    println("qjhvhtzxzqqjkmpb is good? ${"qjhvhtzxzqqjkmpb".isGood()}")
//    println("uurcxstgmygtbstg is good? ${"uurcxstgmygtbstg".isGood()}")
//    println("ieodomkazucvgmuy is good? ${"ieodomkazucvgmuy".isGood()}")
//    println("abxxxab is good? ${"abxxxxab".isGood()}")
//    println("abxxxa is good? ${"abxxxa".isGood()}")
//    println("aaaqrqr is good? ${"aaaqrqr".isGood()}")
//    println("aaa is good? ${"aaa".isGood()}")
//    println("ijabcdijefghij is good? ${"ijabcdijefghij".isGood()}")
//    println("uxcplgxnkwbdwhrp is good? ${"uxcplgxnkwbdwhrp".isGood()}")
//    println("abaxaxa is good? ${"abaxaxa".isGood()}")
//    println("xaxaaba is good? ${"xaxaaba".isGood()}")
//
//    println(findaba("12345aba6789"))
//    println(findaba("aba123456789"))
//    println(findaba("123456789aba"))


}