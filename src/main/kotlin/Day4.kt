
// Kotlin has packages just like Java but we just put this in the default one...

// Imports are like Java but you don't need ;
import java.security.MessageDigest

object Util {
    val md = MessageDigest.getInstance("MD5")

    // We don't need a string though...

    fun getmd5BytesFromPrefixAndInt(pre: String, n: Int): ByteArray {
        return md.digest((pre + n.toString()).toByteArray())
    }

}

fun main(args: Array<String>) {

    fun check5(n: ByteArray): Boolean {
        assert(n.size > 5)
        return (n[0] == 0.toByte()) &&
                (n[1] == 0.toByte()) &&
                (n[2].toInt() and 0xf0.toInt() == 0)
    }

    fun check6(n: ByteArray): Boolean {
        assert(n.size > 5)
        return (n[0] == 0.toByte()) &&
                (n[1] == 0.toByte()) &&
                (n[2] == 0.toByte())
    }

    var n = 0
    //val prefix = "abcdef"
    val prefix = "yzbqklnj"
    do {
        n ++
        val check = check6(Util.getmd5BytesFromPrefixAndInt(prefix, n))
    } while (check == false)

    print(n) // test output 609043
}