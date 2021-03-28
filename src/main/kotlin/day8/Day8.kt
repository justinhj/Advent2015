package day8

fun String.countMemoryBytes(): Int {

    // Strip the quotes off each end
    // \\ is a single slash and counts as 1
    // \" is a quote and counts as 1
    // \x00 is always a 2 digit hex value, counts as 1

    val stripped = this.substring(1,this.length-1)

    // Replace \\ with single char
    val replaceDoubleSlash = Regex("\\\\\\\\").replace(stripped, "S")

    // Replace \" with single char
    val replaceQuotes = Regex("\\\\\"").replace(replaceDoubleSlash, "Q")

    // Replace \x?? with single char
    val replaceHex = Regex("\\\\x[0-9a-f][0-9a-f]").replace(replaceQuotes, "H")

    val count = replaceHex.length
    return count

}

// part two requires an encoder too
// "" encodes to "\"\"",

fun String.encode(): String {

    // slash to double slash
    val out = Regex("\\\\").replace(this, "\\\\\\\\")

    //println("out $out")
    val out2 = Regex("\"").replace(out, "\\\\\\\"")
    //println("out2 $out2")

    return "\"" + out2 + "\""
}

fun main() {
    println("""""""".countMemoryBytes() == 0)
    println(""""1"""".countMemoryBytes() == 1)
    println(""""12"""".countMemoryBytes() == 2)
    println(""""\\"""".countMemoryBytes() == 1)
    println(""""\\\"""".countMemoryBytes() == 2)
    println(""""\\\\"""".countMemoryBytes() == 2)
    println(""""12\"12"""".countMemoryBytes() == 5)
    println(""""\"12\"12\""""".countMemoryBytes() == 7)
    println(""""\\\"12\"12\\\""""".countMemoryBytes() == 9)
    println(""""\xff"""".countMemoryBytes() == 1)

    // Try the sample input (and real input, just change the resource name)

    val sampleInput = {}::class.java.getResource("day8.txt").
        readText().
        split("\n")

    val s1 = sampleInput.map {
        it.countMemoryBytes()
    }.sum()
    val s2 = sampleInput.map {
        it.length
    }.sum()

    println(s2 - s1)


    println("""""""".encode().length)
    println(""""abc"""".encode().length)
    println(""""aaa\"aaa"""".encode().length)
    println(""""\x27"""".encode().length)

    val sampleInput2 = {}::class.java.getResource("day8.txt").
        readText().
        split("\n")

    val s3 = sampleInput2.map {
        it.encode().length
    }.sum()
    val s4 = sampleInput2.map {
        it.length
    }.sum()

    println("s3 $s3 s4 $s4 answer = ${s3 - s4}")
}