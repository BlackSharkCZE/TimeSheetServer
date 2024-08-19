package cz.blackshark

import io.quarkus.elytron.security.common.BcryptUtil
import org.junit.jupiter.api.Test

class PasswordHashGenerator {

    @Test
    fun generatePassword() {
        val password = "1234567890"
        val result = BcryptUtil.bcryptHash(password)
        println(result)
    }

}
