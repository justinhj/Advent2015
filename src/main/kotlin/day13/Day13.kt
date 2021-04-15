package day13

data class SeatBoost(val sitter: String, val sittee: String, val boost: Int)

fun parseInput(input: String): List<SeatBoost> {

    val lines = input.split("\n")

    val parsed = lines.map {
        line ->
            val m = Regex("(\\w+) would (\\w+) (\\w+) happiness units by sitting next to (\\w+).").find(line)
                ?: throw Exception("parse error")
            val (sitter, isGain, boostMagnitude, sittee) = m.destructured
            val boost = if(isGain == "gain") boostMagnitude.toInt() else -boostMagnitude.toInt()
            SeatBoost(sitter, sittee, boost)
    }

    return parsed
}

/*
Algorithm

We simply need to brute force the different seating arrangements
To score each arrangement you must look at the name of the sitter and
adjust their happiness from zero based on their two neighbours

This implies a lookup of the sitter and neighbour pair that returns a boost which
can easily be calculated

 */

fun makeLookup(boosts: List<SeatBoost>): Map<Pair<String,String>,Int> {
    val m = mutableMapOf<Pair<String,String>,Int>()
    boosts.forEach {
        boost ->
            m[Pair(boost.sitter,boost.sittee)] = boost.boost
    }
    return m
}

// given a set return all the combinations as a list of lists
fun <A>combinations(input: Set<A>, acc: List<A> = listOf(), accs: List<List<A>> = listOf()): List<List<A>> {

    return if(input.size == 1) {
        val mAcc = acc.toMutableList()
        mAcc.add(input.elementAt(0))
        val mAccs = accs.toMutableList()
        mAccs.add(mAcc)
        return mAccs
    } else if(input.isEmpty()) {
        return listOf()
    }
    else {
        input.flatMap {
            a ->
                val mAcc = acc.toMutableList()
                mAcc.add(a)
                combinations(input.minus(a), mAcc, accs)
        }
    }
}

fun scoreSeating(seating: List<String>,boosts: Map<Pair<String,String>,Int>): Int {
    val lastSeat = seating.size-1
    val scores = seating.mapIndexed {
        i,sitter ->
            val left = if(i==0) seating[lastSeat] else seating[i-1]
            val right = if(i==lastSeat) seating[0] else seating[i+1]
            val leftBoost = boosts.getOrDefault(Pair(sitter,left),0)
            val rightBoost = boosts.getOrDefault(Pair(sitter,right),0)
            leftBoost + rightBoost
    }

    return scores.sum()
}

fun happiestSeating(seatings: List<List<String>>, boosts: Map<Pair<String,String>,Int>): Int {

    var happiest = Int.MIN_VALUE
    seatings.forEach {
        seating ->
            val score = scoreSeating(seating,boosts)
            happiest = Math.max(happiest, score)
    }

    return happiest
}

fun happinessChange(input: String): Int {
    val lookup = makeLookup(parseInput(input))

    // we would like the set of unique guests...
    val guests = lookup.mapKeys {
            (k,_) -> k.first
        }.keys

    // generate combinations
    val combs = combinations(guests)

    // score the combinations and return the maximum happiness boost
    return happiestSeating(combs,lookup)
}

// Step 2 you add yourself and you are non-plussed about boosts
fun happinessChangeWithMe(input: String): Int {
    val lookup = makeLookup(parseInput(input))

    // we would like the set of unique guests...
    val guests = lookup.mapKeys {
            (k,_) -> k.first
    }.keys

    val guestsAndMe = guests.toMutableSet()
    guestsAndMe.add("me")

    // generate combinations
    val combs = combinations(guestsAndMe)

    // score the combinations and return the maximum happiness boost
    return happiestSeating(combs,lookup)
}

fun main() {

    val urlSample = {}::class.java.getResource("day13sample.txt")
    val url = {}::class.java.getResource("day13.txt")

    if(url == null) throw Exception("Failed to access the data")
    val input = url.readText()

    if(urlSample == null) throw Exception("Failed to access the sample data")
    val inputSample = urlSample.readText()

//    val lookup = makeLookup(parseInput(inputSample))
//    println(lookup)

//    val s1 = setOf(1,2,3,4)
//    println("combinations ${combinations(s1)}")

    println("sample ${happinessChange(inputSample)}")
    println("step 1 ${happinessChange(input)}")

    println("step 2 ${happinessChangeWithMe(input)}")

}