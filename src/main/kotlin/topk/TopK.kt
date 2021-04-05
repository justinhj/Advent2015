package topk

// Solve the top K problem in Kotlin
// Something that came up in a discussion, not an Advent problem AFAIK

import java.util.PriorityQueue

fun main() {

    val K: Int = 5

    val input = listOf<Int>(1,1,2,3,4,3,2,1,1,1,1,2,2,3,4,5,5,6,6,4,3,32,2,1,1,4,5,6,7,8,3,4,5,6,7,8,9,10,11,12)

    // Step 1 accumulate a map of all the counts

    val m: MutableMap<Int,Int> = input.fold(mutableMapOf(), {
        mm, s ->
            val c = mm.getOrDefault(s,0)
            mm.put(s, c + 1)
            mm
    })

    // Step 2 iterate over the map using a priority queue to keep track of the top K

    data class Freq(val item: Int, val count: Int): Comparable<Freq> {
        override fun compareTo(other: Freq): Int {
            return count.compareTo(other.count)
        }
    }

    val pq = PriorityQueue<Freq>(K)

    // keep track of smallest entry
    var smallest = Int.MAX_VALUE

    m.forEach { item, count ->

        // Initialize smallest
        if(smallest == Int.MAX_VALUE) smallest = count

        // Conditionally add each item

        if(pq.size == K) {
            // no room for new item, poll to make room if this one is bigger
            if(count > smallest) {
                pq.poll()
                pq.add(Freq(item, count))
            }
            // If this item is < or = to smallest
        } else {
            pq.add(Freq(item, count))
        }
    }

    println(pq)

    // Iteratively pull off the smallest items

    while(pq.size > 0) {
        val item = pq.poll()
        println("item ${item.item} count ${item.count}")
    }

// TOP 5
//    item 5 count 4
//    item 2 count 5
//    item 3 count 5
//    item 4 count 5
//    item 1 count 8
}
