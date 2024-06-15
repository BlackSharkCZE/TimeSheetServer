package cz.blackshark.modules.gemts.http.client

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.service.GemTimesheetLogin
import io.quarkus.logging.Log
import io.quarkus.test.junit.QuarkusTest
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class GemLoginClientTest {

    @RestClient
    private lateinit var gemTimesheetLoginClient: GemTimesheetLoginClient

    @Inject
    private lateinit var config: GemTimesheetConfig

    @Test
    fun `login should pass with valid username and password`() {
        Log.info("Try login with user ${config.username()}")
        try {
            val response = gemTimesheetLoginClient.login(config.username(), config.password())
            Assertions.assertNotNull(response)
            Assertions.assertEquals(302, response.statusCode())
//            Assertions.assertTrue(response.cookies.contains(GemTimesheetLogin.QUAKRUS_COOKIE_LOGIN_NAME))
        } catch (e: Exception ) {
            Log.error("Error during login", e)
            Assertions.fail("Error during login",e)
        }
    }

}
