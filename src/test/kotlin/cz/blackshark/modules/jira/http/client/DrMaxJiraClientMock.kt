package cz.blackshark.modules.jira.http.client

import cz.blackshark.config.JiraConfig
import cz.blackshark.modules.jira.dto.*
import io.quarkus.test.Mock
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.ZonedDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@Mock
@RestClient
@ApplicationScoped
class DrMaxJiraClientMock @Inject constructor(private val jiraConfig: JiraConfig) : DrMaxJiraClient {

    val me = UserDetails(jiraConfig.accountId(), "hello@xy.cz", "Hello")
    val randomUser = UserDetails("randomaccountid", "greeting@xy.cz", "Greeting")

    val issues = listOf(
        JiraIssue("IPF-1122", "CPOJ"),
        JiraIssue("IPF-1133", "RPOJ"),
    )
    val worklogs = listOf(
        Worklog(
            "http://",
            ZonedDateTime.now().withDayOfMonth(1),
            ZonedDateTime.now(),
            ZonedDateTime.now().minusHours(4),
            "4h",
            14400,
            "IPF-1133",
            me
        ),
        Worklog(
            "http://",
            ZonedDateTime.now().withDayOfMonth(2),
            ZonedDateTime.now(),
            ZonedDateTime.now().minusHours(4),
            "2h",
            7200,
            "IPF-1133",
            me
        ),
        Worklog(
            "http://",
            ZonedDateTime.now().withDayOfMonth(2),
            ZonedDateTime.now(),
            ZonedDateTime.now().minusHours(4),
            "2h",
            7200,
            "IPF-1133",
            randomUser
        ),

    )

    override fun workLog(issue: String, worklog: JiraWorklog): Response {
        TODO("Not yet implemented")
    }

    override fun search(jql: String): SearchIssueResponse {
        return SearchIssueResponse(1000, issues.size, issues)
    }

    override fun getWorkLog(issue: String): PageOfWorklogs {
        val wl = worklogs.filter { it.id == issue }
        return PageOfWorklogs(0,1000, wl.size, wl)
    }
}
