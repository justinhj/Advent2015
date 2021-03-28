package day9

// strategy parse the input into a map of maps
// so you can get the distance between any two
// cities
// Map<String, Map<String,Int>>
// Dublin to Belfast = 141

// TODO this is kind of a mess it must be easier to work with immutable collections than this?

fun distanceMap(input: List<String>): Map<String, Map<String,Int>> {

    val dm: MutableMap<String, MutableMap<String,Int>> = mutableMapOf()

    input.forEach{
        val m =  Regex("(\\w+) to (\\w+) = (\\d+)").find(it)
        if(m != null) {
            val (source, dest, distance) = m.destructured
            val sourceMap = dm.getOrDefault(source, mutableMapOf())
            sourceMap.put(dest,distance.toInt())
            dm.put(source, sourceMap)
            val sourceMap2 = dm.getOrDefault(dest, mutableMapOf())
            sourceMap2.put(source,distance.toInt())
            dm.put(dest, sourceMap2)
        }
    }

    return dm
}

fun <T> Set<T>.permutations(): List<List<T>> {
    if(this.size <= 1) return listOf(this.toList())
    else {
        return this.flatMap {
            val r = this.toMutableSet()
            r.remove(it)
            val perms = r.permutations()
            perms.map { a ->
                val nl = a.toMutableList() // Bleurgh...
                nl.add(it)
                nl.toList()
            }
        }
    }
}

fun cost(cities: List<String>, dm: Map<String, Map<String,Int>>): Pair<List<String>,Int> {
    val trips = cities.zip(cities.drop(1))
    val c = trips.map {
        val c = dm.get(it.first)?.get(it.second)
        if(c != null) c else 0
    }
    return Pair(cities, c.sum())
}

fun main() {

    val input = {}::class.java.getResource("day9.txt").
        readText().
        split("\n")

    val dm = distanceMap(input)
    val cities = dm.keys

    // combination or permutation
    // order matters so it is permutation

    // generate the permutations...
    // ABC
    // ACB
    // BAC
    // BCA
    // CAB
    // CBA

    // for each item make a list with that item and the permutations of the rest of the items
    // permutations of ABC is A and perm B,C
    //   perm of B,C
    //      B and perms of C
    //      C and perms of A
    // of B and perm A,C
    // of C and perm A,B

    //println(cities.permutations())
    //println(cities)

    val tripCosts = cities.permutations().map {
      cost(it,dm)
    }

    // For part 1 it's sortedByDescending
    println(tripCosts.sortedBy{
        it.second
    })

}


