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
data class JSONNumber(val value: Int) : JSONThing()
data class JSONArray(val elements: List<JSONThing>) : JSONThing()
data class JSONObject(val elements: Map<JSONString,JSONThing>) : JSONThing()

// Parse the thing you're looking at and return an index to the string
// after the thing you parsed
fun parse(lookingAt: Int, input: String): Pair<Int,JSONThing> {

    val lookAt = input[lookingAt]

    return if(lookAt.isDigit() || lookAt == '-') {
        parseNumber(lookingAt, input)
    } else {
        when(lookAt) {
            ' ' -> parse(lookingAt + 1,input)
            '\t' -> parse(lookingAt + 1,input)
            '\"' -> parseString(lookingAt, input)
            '[' -> parseArray(lookingAt, input)
            '{' -> parseObject(lookingAt, input)
            else -> throw Exception("parse error")
        }
    }
}

fun parseNumber(lookingAt: Int, input: String): Pair<Int,JSONNumber> {

    var i = lookingAt
    if(input[i] == '-') i++

    while(i < input.length && input[i].isDigit()) i++

    val value = input.subSequence(lookingAt,i).toString().toInt()

    return Pair(i, JSONNumber(value))
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
    val l = mutableListOf<JSONThing>()
    i++ // skip [
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

fun parseObject(lookingAt: Int, input: String): Pair<Int,JSONObject> {
    // parse each object which is a string followed by an object
    // with commas in between
    var i = lookingAt
    val l = mutableMapOf<JSONString,JSONThing>()
    i++ // skip {
    while(true) {
        if(input[i] == '}') {
            break
        } else if(input[i] == ',') {
            i++
        }
        val key = parse(i, input)
        // skip whitespace and expect a :
        i = key.first
        if(key.second !is JSONString) throw Exception("invalid object key")
        while(input[i] == ' ') i++
        if(input[i] != ':') throw Exception("no colon in object key value")
        i++
        while(input[i] == ' ') i++
        val value = parse(i, input)
        l[key.second as JSONString] = value.second
        i = value.first
    }

    return Pair(i + 1, JSONObject(l))
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
    println(parse(0, """{}"""))
    println(parse(0, """12"""))
    println(parse(0, """[12,13,14]"""))

    println(parse(0, """{"key1":["value1", "value2"]}"""))
    println(parse(0, """["Nero", "Justin", "Jamie"]"""))
    println(parse(0, """[["Lisa", "Lara"], "Nero", "Justin", "Jamie"]"""))
    println(parse(0, """{"a":{"b":4},"c":-1}"""))

    val parsed = parse(0,input)

    fun calcSum(json: JSONThing): Int {

        return when (json) {
            is JSONNumber -> json.value
            is JSONString -> 0
            is JSONArray -> json.elements.sumBy { thing -> calcSum(thing) }
            is JSONObject -> if(json.elements.containsValue(JSONString("red"))) 0
            else {
                json.elements.values.sumBy { thing -> calcSum(thing) }
            }
        }

    }

    println("Input sum is ${calcSum(parsed.second)}")

}