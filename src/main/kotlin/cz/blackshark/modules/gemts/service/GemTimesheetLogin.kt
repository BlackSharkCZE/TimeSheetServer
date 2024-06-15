package cz.blackshark.modules.gemts.service

import cz.blackshark.config.GemTimesheetConfig
import cz.blackshark.modules.gemts.exceptions.TimesheetLoginException
import cz.blackshark.modules.gemts.http.client.GemTimesheetLoginClient
import io.quarkus.logging.Log
import java.net.HttpCookie
import java.net.http.HttpResponse
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GemTimesheetLogin(
    private val tsConfig: GemTimesheetConfig,
    private val client: GemTimesheetLoginClient,
) {

    companion object {
        const val QUAKRUS_COOKIE_LOGIN_NAME: String = "quarkus-credential"
    }

    private var cookieValue: String? = null
    private var lastLogin: LocalDateTime? = null

    @Throws(TimesheetLoginException::class)
    fun login(): String? {
        Log.info("Process login into GEM Timesheet!")
        val response = client.login(tsConfig.username(), tsConfig.password())
        cookieValue = when (response.statusCode()) {
            302 -> {
                processProbablyValidResponse(response).also { lastLogin = LocalDateTime.now() }
            }
            else -> processUnexpectedResponse(response)
        }
        return cookieValue
    }

    private fun processUnexpectedResponse(response: HttpResponse<Void>): String? {
        throw TimesheetLoginException("Unexpected response from login endpoint. Expected HTTP status 302 but ${response.statusCode()} received.")
    }

    private fun processProbablyValidResponse(response: HttpResponse<Void>): String {
        val location = response.headers().firstValue("Location")

        if (location.isPresent) {
            val strLoc = location.get()
            if (strLoc.contains("error")) {
                throw TimesheetLoginException("Login failed. Redirect location is error page!")
            }

            val cookies: MutableList<HttpCookie> = mutableListOf()
            response.headers().map().entries.filter { it.key.equals("Set-Cookie", true) }
                .forEach { mapEntry ->
                    mapEntry.value.forEach {
                        cookieStr ->
                        cookies.addAll(HttpCookie.parse(cookieStr))
                    }
                }
            return cookies.firstOrNull { it.name == QUAKRUS_COOKIE_LOGIN_NAME }?.value ?: ""

        } else {
            throw TimesheetLoginException("Header location not present in login response")
        }

    }

    fun getCookie(): String? {
        if (lastLogin == null || lastLogin!!.isBefore(LocalDateTime.now().minusMinutes(25)) || lastLogin == null) {
            Log.info("Expired login. Try re-login!")
            try {
                login()
            } catch (e: Exception) {
                Log.error("Login to GEM Timesheet failed: ${e.message}")
            }
        }
        Log.info("Login cookie: $cookieValue")
        return cookieValue
    }
}
