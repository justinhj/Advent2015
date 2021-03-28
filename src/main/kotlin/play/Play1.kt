// Various playing
package play

// Pattern matching on type

abstract class Element
data class And(val left: String,val right: String): Element()
data class Or(val left: String,val right: String): Element()
data class Value(val value: Int): Element()

// now match with when

// Note you can match on type but there is no destructuring of variables

fun dispatch(e: Element) : Unit {
    when (e) {
        is Value -> println("Value ${e.value}")
        is And -> println("Value ${e.left} AND ${e.right}")
        is Or -> println("Value ${e.left} OR ${e.right}")

    }
}

// Using nullables in expressions

fun testNulls(left: Value?, right: Value?): Unit {

    // you can deference nullables as much as you like
    val lv = left?.value
    val rv = right?.value

    // but to use them you must strip their nullness by checking them
    if(lv != null && rv != null) {
        println("Yaas! $lv $rv")
    }

}