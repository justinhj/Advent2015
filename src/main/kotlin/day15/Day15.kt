package day15

// fun findSeq(n:Int, ns: List<Int>):Int {
//     var acc = ns
//     var tail = acc
//     var i = 1

//     while(true) {
//         val find = acc.contains(n)
//         if(find) return i
//         tail = tail.drop(1)
//         acc = acc.zip(tail).map{
//             (a,b) -> a + b
//         }
//         i++
//    }    
// }

data class Ingredient(val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)

// Sample:
// Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8

fun parseInput(input: String): List<Ingredient> {

    val lines = input.split("\n")

    val parsed = lines.map {
            line ->
                val m = Regex("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)").find(line)
                    ?: throw Exception("parse error")
                val (name, capacity, durability, flavor, texture, calories) = m.destructured
                Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
    }
    return parsed
}

fun bestCookie(ingredients: List<Ingredient>, remainTeaspoons: Int, pairs: List<Pair<Ingredient,Int>>, best: Int): Int {

    //println("ingredients ${ingredients.size} remain ${remainTeaspoons} pairs ${pairs.size} best $best")

    if(ingredients.size == 1) {
        val final = Pair(ingredients.first(),remainTeaspoons)
        val newPairs = pairs.toMutableList()
        newPairs.add(final)
        return Math.max(best, scoreCookie(newPairs))
    } else {
        val remaining = ingredients.subList(1,ingredients.size)
        var newBest = best

        for(t in 0..remainTeaspoons) {
            val newPairs = pairs.toMutableList()
            newPairs.add(Pair(ingredients.first(), t))
            newBest = Math.max(
                bestCookie(remaining, remainTeaspoons - t, newPairs, newBest), newBest)
        }

        return newBest
    }
}

fun scoreCookie(ingredients: List<Pair<Ingredient, Int>>): Int {

    var capacity = 0
    var durability = 0
    var flavor = 0
    var texture = 0

    ingredients.forEach{
        (i,q) ->
            capacity = capacity + i.capacity * q
            durability = durability + i.durability * q
            flavor = flavor + i.flavor * q
            texture = texture + i.texture * q
    }

    capacity = Math.max(0,capacity) 
    durability = Math.max(0,durability) 
    flavor = Math.max(0,flavor) 
    texture = Math.max(0,texture) 

    val score = capacity * durability * flavor * texture
    //println("score $score")
    return score
}


fun main() {

    val urlSample = {}::class.java.getResource("day15sample.txt")
    val url = {}::class.java.getResource("day15.txt")

    if(url == null) throw Exception("Failed to access the data")
    val input = url.readText()

    if(urlSample == null) throw Exception("Failed to access the sample data")
    val inputSample = urlSample.readText()

    println("Parsed sample ${parseInput(inputSample)}")
    println("Parsed ${parseInput(input)}")

    var parsedSample: List<Ingredient> = parseInput(inputSample)
    var parsed: List<Ingredient> = parseInput(input)

    var inputs = parsedSample.map {
        if(it.name == "Butterscotch") Pair(it,44)
        else if(it.name == "Cinnamon") Pair(it,56)
        else Pair(it,0)
    }

    println("score ${scoreCookie(inputs)}")

    // Find the best cookies ...

    println("calculate sample score ${bestCookie(parsedSample, 100, listOf(), 0)}")
    println("calculate part 1 score ${bestCookie(parsed, 100, listOf(), 0)}")


}
