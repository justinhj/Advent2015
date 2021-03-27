
// Day 7 solution

val sample = """
    123 -> x
    456 -> y
    x AND y -> d
    x OR y -> e
    x LSHIFT 2 -> f
    y RSHIFT 2 -> g
    NOT x -> h
    NOT y -> i
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

// Each element contains the parsed parameters and an execute function that given
// the known wires will generate a new known wire and its value, otherwise null
abstract class Element(out: String) {
    abstract fun execute(wires: Map<String,Int>): Pair<String,Int>?
}

data class And(val wire1: String,val wire2: String, val out: String): Element(out) {
    override fun execute(wires: Map<String,Int>): Pair<String,Int>? {
        val w1 = wires.get(wire1)
        val w2 = wires.get(wire2)
        if(w1 != null && w2 != null) {
            return Pair(out, w1 and w2)
        } else {
            return null
        }
    }
}

data class Or(val wire1: String,val wire2: String, val out: String): Element(out) {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = wires.get(wire1)
        val w2 = wires.get(wire2)
        if (w1 != null && w2 != null) {
            return Pair(out, w1 or w2)
        } else {
            return null
        }
    }
}

data class LShift(val wire1: String,val amount: Int, val out: String): Element(out) {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = wires.get(wire1)
        if (w1 != null) {
            return Pair(out, w1 shl amount)
        } else {
            return null
        }
    }
}

data class RShift(val wire1: String,val amount: Int, val out: String): Element(out) {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = wires.get(wire1)
        if (w1 != null) {
            return Pair(out, w1 shr amount)
        } else {
            return null
        }
    }
}

data class Not(val wire1: String, val out: String): Element(out)  {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        val w1 = wires.get(wire1)
        if (w1 != null) {
            return Pair(out, w1.inv())
        } else {
            return null
        }
    }
}

data class Signal(val value: Int, val out: String): Element(out)  {
    override fun execute(wires: Map<String, Int>): Pair<String, Int>? {
        return Pair(out, value)
    }
}

fun stringToElement(input: String): Element? {

    // 123 -> x
    val m1 =  Regex("(\\d+) -> (\\w+)").find(input)
    if(m1 != null) {
        val (value, out) = m1.destructured
        return Signal(value.toInt(),out)
    }
    // x AND y -> d
    val m2 =  Regex("(\\w+) AND (\\w+) -> (\\w+)").find(input)
    if(m2 != null) {
        val (w1, w2, out) = m2.destructured
        return And(w1,w2,out)
    }
    // x AND y -> d
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
        return LShift(w1,value.toInt(),out)
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
fun solve(input: Solver): Solver? {
    var newWires: MutableMap<String,Int> = input.wires.toMutableMap()

    val remainingElements = input.elements.filter {
        val r = it.execute(newWires)
        if(r == null)
            true // leave the element
        else {
            println("Adding wire ${r.first} / ${r.second}")
            newWires.put(r.first,r.second)
            false // remove the element
        }
    }

    return Solver(remainingElements, false, newWires)
}


// Note that main doesn't appear in a class and it doesn't need arguments
fun main() {

//    println(stringToElement("123 -> x"))
//    println(inputToElements(sample))

    val sampleElements = inputToElements(sample)
    val sampleSolver = Solver(sampleElements)

    solve(sampleSolver)

}

