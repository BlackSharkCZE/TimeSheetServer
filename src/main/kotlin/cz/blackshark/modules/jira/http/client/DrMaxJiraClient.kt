package cz.blackshark.modules.jira.http.client

import cz.blackshark.config.JiraConfig
import cz.blackshark.modules.jira.customizations.JiraClientObjectMapper
import cz.blackshark.modules.jira.dto.JiraContent
import cz.blackshark.modules.jira.dto.JiraWorklog
import cz.blackshark.modules.jira.dto.PageOfWorklogs
import cz.blackshark.modules.jira.dto.SearchIssueResponse
import io.smallrye.config.SmallRyeConfig
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.Response

@ClientHeaderParam(name = "Authorization", value = ["{cz.blackshark.modules.jira.http.client.DrMaxJiraClient.auth}"])
@RegisterRestClient(configKey = "drmax-jira")
@Path("/rest/api/3")
@RegisterProvider(JiraClientObjectMapper::class)
interface DrMaxJiraClient {

	fun auth(): String {
		val config = ConfigProvider.getConfig().unwrap(SmallRyeConfig::class.java)
		val jira = config.getConfigMapping(JiraConfig::class.java)
		val username = jira.username()
		val token = jira.token()
		return "Basic " + Base64.getEncoder().encodeToString("$username:$token".toByteArray())
	}

	@POST
	@Path("issue/{issue}/worklog")
	fun workLog(@PathParam("issue") issue: String, worklog: JiraWorklog): Response

	@GET
	@Path("search")
	fun search(@QueryParam("jql") jql: String) : SearchIssueResponse

	@GET
	@Path("issue/{issue}/worklog")
	fun getWorkLog(@PathParam("issue") issue: String) : PageOfWorklogs

}
