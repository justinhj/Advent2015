package day12

// Find all the numbers in a string and return them as a list
// Part one is possible with a regex

fun getNumbers(s: String): List<Int> {
    val numRegex = Regex("-?\\d+")
    val result = numRegex.findAll(s)
    val values = result.map{
        mr ->
            mr.value.toInt()
    }
    return values.toList()
}

fun sum(s: String): Int {
    return getNumbers(s).sum()
}

// Part two requires structural parsing because we need to
// know if a particular object has a property containing the string "red"

sealed class JSONThing
data class JSONString(val value: String) : JSONThing()
data class JSONNumber(val value: String) : JSONThing()
data class JSONArray(val elements: List<JSONThing>) : JSONThing()
data class JSONObject(val elements: Map<JSONString,JSONThing>) : JSONThing()

// Parse the thing you're looking at and return an index to the string
// after the thing you parsed
fun parse(lookingAt: Int, input: String): Pair<Int,JSONThing> {

    val lookat = input[lookingAt]

    when(lookat) {
        ' ' -> return parse(lookingAt + 1,input)
        '\t' -> return parse(lookingAt + 1,input)
        '\"' -> return parseString(lookingAt, input)
        '[' -> return parseArray(lookingAt, input)
        else -> throw Exception("parse error")
    }

}

fun parseString(lookingAt: Int, input: String): Pair<Int,JSONString> {
    // find the next " and return it
    var i = lookingAt + 1
    while(input[i] != '\"') {
        i++
    }
    return Pair(i + 1, JSONString(input.substring(lookingAt + 1, i)))
}

fun parseArray(lookingAt: Int, input: String): Pair<Int,JSONArray> {
    // parse the things separated by ,
    var i = lookingAt
    var l = mutableListOf<JSONThing>()
    i++ // skip first [
    while(true) {
        if(input[i] == ']') {
            break
        } else if(input[i] == ',') {
            i++
        }

        val next = parse(i, input)
        l.add(next.second)
        i = next.first
    }

    return Pair(i + 1, JSONArray(l))
}

fun main() {
    println(getNumbers("12"))
    println(getNumbers("-2,12,24"))

    println(sum("[1,2,3]") == 6 && sum("""{"a":2,"b":4}""") == 6)
    println(sum("""[[[3]]]""") == 3 && sum("""{"a":{"b":4},"c":-1}""") == 3)
    println(sum("""[]""") == 0 && sum("""{}""") == 0)

    // Part one

    val input = {}::class.java.getResource("day12.txt").readText()

    println(sum(input))

    // Part two

    println(parse(0, "\"test\""))
    println(parse(0, """["Nero", "Justin", "Jamie"]"""))
    println(parse(0, """[["Lisa", "Lara"], "Nero", "Justin", "Jamie"]"""))

}