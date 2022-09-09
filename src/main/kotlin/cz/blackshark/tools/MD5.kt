package cz.blackshark.tools

import java.math.BigInteger
import java.security.MessageDigest

object MD5 {

    /**
     * Create MD5 from the string
     */
    fun asHex(input: String): String = asHex(input.toByteArray())


    /**
     * Create MD5 from the byte array
     */
    fun asHex(input: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input)).toString(16).padStart(32, '0')
    }

}
