package cz.blackshark.modules.gemts.http.client

import cz.blackshark.config.GemTimesheetConfig
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.MediaType

@ApplicationScoped
class GemTimesheetLoginClient(
    private val gmConfig: GemTimesheetConfig
) {

    private val client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .followRedirects(HttpClient.Redirect.NEVER)
        .build()

    fun login(username: String, password: String): HttpResponse<Void> {

        val request = HttpRequest
            .newBuilder()
            .uri(URI(gmConfig.url() + "/j_security_check"))
            .POST(HttpRequest.BodyPublishers.ofString(buildData(username, password)))
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
            .build()

        return client.send(request, HttpResponse.BodyHandlers.discarding())
    }

    private fun buildData(username: String, password: String): String {
        val data = mapOf(
            "j_username" to username,
            "j_password" to password
        )
        return data.map { "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}" }
            .joinToString("&")
    }
}
