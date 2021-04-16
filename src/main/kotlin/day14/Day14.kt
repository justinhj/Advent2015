package day14

// Vixen can fly 8 km/s for 8 seconds, but then must rest for 53 seconds.
data class Reindeer(val name: String, val speed: Int, val active: Int, val rest: Int)

fun parseInput(input: String): List<Reindeer> {

    val lines = input.split("\n")

    val parsed = lines.map {
            line ->
        val m = Regex("(\\w+) can fly (\\w+) km/s for (\\w+) seconds, but then must rest for (\\w+) seconds.").find(line)
            ?: throw Exception("parse error")
        val (name, speed, active, rest) = m.destructured
        Reindeer(name, speed.toInt(), active.toInt(), rest.toInt())
    }
    return parsed
}

/*
Algorithm

You can calculate the position over time as follows ...
Given time t the reindeer has been running for n complete cycles
and maybe some partial cycles
A cycle is the active time plus the rest time. In this period the
distance travelled is simple speed * active * n
For the partial period you can simply calculate the proportion of the
active period * speed

Example

Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
At t = 1000
complete cycles = 1000 / (10 + 127) = 7
  7 * speed * active = 7 * 14 * 10 = 980
partial cycle = 1000 % (10 + 127) == 41
  min(41,10) = 10
  10 * speed = 10 * 14 = 140
total distance = 1120

 */

fun calcDistance(t: Int, reindeer: Reindeer): Int {
    val cycles = t / (reindeer.active + reindeer.rest)
    val fullCycleDistance = cycles * reindeer.speed * reindeer.active
    val partialCycleTime = t % (reindeer.active + reindeer.rest)
    val partialCycleDistance = Math.min(partialCycleTime,reindeer.active) * reindeer.speed
    return fullCycleDistance + partialCycleDistance
}

fun step2Race(endTime: Int, reindeers: List<Reindeer>): Int {
    var t = 0
    val scores = mutableMapOf<String,Int>()
    while(t < endTime) {
        t++
        val positions = reindeers.map{r -> Pair(r.name, calcDistance(t, r))}
        val winner = positions.maxByOrNull { (name,position) -> position }
        val winnerPoints = winner?.second ?: 0
        val winners = positions.filter{(name,position) -> position == winnerPoints}
        winners.forEach {
            (name,position) ->
                val score = scores.getOrDefault(name, 0)
                scores.put(name, score + 1)
        }
    }

    val mostPoints =  scores.values.maxOrNull()

    return mostPoints ?: 0
}

fun main() {

    val urlSample = {}::class.java.getResource("day14sample.txt")
    val url = {}::class.java.getResource("day14.txt")

    if(url == null) throw Exception("Failed to access the data")
    val input = url.readText()

    if(urlSample == null) throw Exception("Failed to access the sample data")
    val inputSample = urlSample.readText()

    val sampleReindeers = parseInput(inputSample)
    val winrar = sampleReindeers.map { r ->
        calcDistance(1000, r)
    }.maxOrNull()

    println("Sample Reindeer wins at ${winrar}")

    val reindeers = parseInput(input)
    val winrar2 = reindeers.map { r ->
        calcDistance(2503, r)
    }.maxOrNull()

    println("Step 1 Reindeer wins at ${winrar2}")

    // LOL part two requires an iterative solution anyway

    // Just calculate the position every second awarding a point for the winner
    // winner is the one with the most points

    println("Step 2 sample ${step2Race(1000, sampleReindeers)}")
    println("Step 2 for real ${step2Race(2503, reindeers)}")
}