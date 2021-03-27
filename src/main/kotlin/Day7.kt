
// Day 7 solution

val sample = """
    123 -> xx
    456 -> yy
    xx AND yy -> dd
    xx OR yy -> ee
    xx LSHIFT 2 -> ff
    yy RSHIFT 2 -> gg
    NOT xx -> hh
    NOT yy -> ii
""".trimIndent()

// Method
// How to encode the input into data
// Something like
//   Element
//      Input(value, name)
//      And(wire1, wire2)
//      Or(wire1, wire2)
//     ...

// once encoded need to make map of wire to value
// iterate the rules
//   if signal, see if the wire has a value. if it doesn't set it
//   if it is an operator like and, check if the wires have values, if not then ignore
//   otherwise propagate to the wires value
// repeat until nothing changes

// Note this is a simple solution and works for the puzzle but there's a more efficient way
// Add an element to represent a known value. e.g, Value(value: Int): Element...
// Now you can make a mutable map that is keyed by unknown values...
// Map<String,Element>
// You can loop through the elements with this algorithm
// foreach element
//    what are the unknowns? eg with "a and b -> c" unknowns are a and b
//    add a map entry for both a and b linking to this element
//    go to the next element in the list... "123 -> a" for example
//    in this case there are no unknowns. we can add <"a", Value(123)> to the map
//    now when you go to add that to the map you see that a is the element
//    <a,"a and b -> c">
//    so you can set a to value(123) but also update the b dependency
//    remember b was <"b", And(a,b,c)>
//    now it will become And(123,b,c)
//    later when we know b we will come here and we can resolve the And completely
//    and set a new entry for c to be the actual value
// there's a question here as to whether it would help any to add the outputs
// to the map, which i'd have to work through

// it was also suggested to do this with actors and coroutines? may be a fun exercise


// Each element contains the parsed parameters and an execute function that given
// the known wires will generate a new known wire and its value, otherwise null
abstract class Element(out: String) {
    abstract fun execute(wires: Map<String,Int>): Pair<String,Int>?
}

// Handle any string as a numeric value or a wire name and get the value or null
fun value(input: String, wires: Map<String, Int>): Int? {
    return try {
        input.toInt()
    } catch (exception: NumberFormatException) {
        val w = wires.get(input)
        if (w != null)
            w
        else
            null
    }
}

data class And(val left: String,val right: String, val out: String): Element(out) {
    override fun execute(wires: Map<String,Int>): Pair<String,Int>? {
        val w1 = value(left,wires)
        val w2 = value(right,wires)
        if(w1 != null && w2 != null) {
            return Pair(out, w1 and w2)
        } else {
            return null
        }
    }
}

data class Or(val left: String,val right: String, val out: String): Element(out) {
    override fun execute(wires: Map<String,Int>): Pair<String,Int>? {
        val w1 = value(left,wires)
        val w2 = value(right,wires)
        if(w1 != null && w2 != null) {
            return Pair(out, w1 or w2)
        } else {
            return null
        }
    }
}

data class LShift(val wire: String,val amount: Int, val out: String): Element(out) {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = value(wire,wires)
        if (w1 != null) {
            return Pair(out, w1 shl amount)
        } else {
            return null
        }
    }
}

data class RShift(val wire: String,val amount: Int, val out: String): Element(out) {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = value(wire,wires)
        if (w1 != null) {
            return Pair(out, w1 shr amount)
        } else {
            return null
        }
    }
}

data class Not(val wire: String, val out: String): Element(out)  {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = value(wire,wires)
        if (w1 != null) {
            return Pair(out, w1.inv())
        } else {
            return null
        }
    }
}

data class Signal(val wire: String, val out: String): Element(out)  {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = value(wire,wires)
        if (w1 != null) {
            return Pair(out, w1)
        } else {
            return null
        }
    }
}

fun stringToElement(input: String): Element? {

    // 123 -> x
    val m1 =  Regex("^(\\w+) -> (\\w+)").find(input)
    if(m1 != null) {
        val (value, out) = m1.destructured
        return Signal(value,out)
    }
    // x AND y -> d
    val m2 =  Regex("(\\w+) AND (\\w+) -> (\\w+)").find(input)
    if(m2 != null) {
        val (w1, w2, out) = m2.destructured
        return And(w1,w2,out)
    }
    // x OR y -> d
    val m3 =  Regex("(\\w+) OR (\\w+) -> (\\w+)").find(input)
    if(m3 != null) {
        val (w1, w2, out) = m3.destructured
        return Or(w1,w2,out)
    }
    // x LSHIFT 2 -> f
    val m4 =  Regex("(\\w+) LSHIFT (\\d+) -> (\\w+)").find(input)
    if(m4 != null) {
        val (w1, value, out) = m4.destructured
        return LShift(w1,value.toInt(),out)
    }
    // x RSHIFT 2 -> f
    val m5 =  Regex("(\\w+) RSHIFT (\\d+) -> (\\w+)").find(input)
    if(m5 != null) {
        val (w1, value, out) = m5.destructured
        return RShift(w1,value.toInt(),out)
    }
    // NOT x -> h
    val m6 =  Regex("NOT (\\w+) -> (\\w+)").find(input)
    if(m6 != null) {
        val (w1, out) = m6.destructured
        return Not(w1,out)
    }

    return null
}

fun inputToElements(input: String): List<Element> {
    val lines = input.split("\n")

    return lines.map{
        stringToElement(it)!! // ok to blow up here
    }
}

data class Solver(val elements: List<Element>,
                  val done: Boolean = false,
                  val wires: Map<String,Int> = mapOf())

// Given a list of elements and a map of known wires solve what you can
// Solve elements are removed and new wires are added
// Call this repeatedly until done is true or the heat death of the universe
fun solve(input: Solver): Solver {
    var newWires: MutableMap<String,Int> = input.wires.toMutableMap()

    var added = false

    val remainingElements = input.elements.filter {
        val r = it.execute(newWires)
        if(r == null)
            true // leave the element
        else {
            println("Adding wire ${r.first} / ${r.second}")
            newWires.putIfAbsent(r.first,r.second)
            added = true
            false // remove the element
        }
    }

    println("remaining elements ${remainingElements.size}")
    println("known wires ${newWires.size}")

    return Solver(remainingElements, !added, newWires)
}


// Note that main doesn't appear in a class and it doesn't need arguments
fun main() {

//    println(stringToElement("123 -> x"))
//    println(inputToElements(sample))

    val sampleElements = inputToElements(sample)
    val sampleSolver = Solver(sampleElements)

    val s = solve(sampleSolver)

    for (wire in s.wires) {
        var v = wire.value
        if(v < 0) v += 65536
        println("wire ${wire.key} -> $v")
    }

    //    d: 72
//    e: 507
//    f: 492
//    g: 114
//    h: 65412
//    i: 65079
//    x: 123
//    y: 456

    println("\n\nlololol\n\n")

    val input = {}::class.java.getResource("day7.txt").readText()
    val inputElements = inputToElements(input)
    var inputSolver = Solver(inputElements)

    do {
        inputSolver = solve(inputSolver)
        println("done? ${inputSolver.done}")
    } while(!inputSolver.done)

    for (wire in inputSolver.wires) {
        var v = wire.value
        if(v < 0) v += 65536
        println("wire ${wire.key} -> $v")
    }

    // Part 2 feed 16076 as b ...

    var inputSolver2 = Solver(inputElements,false,mapOf("b" to 16076))

    do {
        inputSolver2 = solve(inputSolver2)
        println("done? ${inputSolver2.done}")
    } while(!inputSolver2.done)

    for (wire in inputSolver2.wires) {
        var v = wire.value
        if(v < 0) v += 65536
        println("wire ${wire.key} -> $v")
    }



}

