package cz.blackshark.modules.jira.beans

import cz.blackshark.config.JiraConfig
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.jira.converter.JaxRsResponseMapper
import cz.blackshark.modules.jira.converter.JiraWorklogConveter
import cz.blackshark.modules.jira.dto.Worklog
import cz.blackshark.modules.jira.http.client.DrMaxJiraClient
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import java.text.MessageFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.NotFoundException

@ApplicationScoped
class JiraBean @Inject constructor(
    @RestClient private val jiraClient: DrMaxJiraClient,
    private val timelineRepository: TimelineRepository,
    private val jiraConfig: JiraConfig,
    private val log: Logger
) {

    /**
     * Try write log into Dr.Max JIRA
     */
    fun logIntoJira(timeline: TimelineEntity,settings: RemoteWriteSettingsEntity): RestResponse<Unit> {

        val jira = JiraWorklogConveter.convert(timeline)
        return if (jira != null) {
            try {
                JaxRsResponseMapper.convert(jiraClient.workLog(jira.ticket, jira))
            } catch (e: Exception) {
                log.error("Can not write to Dr.Max JIRA")
                throw e
            }
        } else {
            RestResponse<Unit>(false, "Create JIRA record failed!. Can not create Jira data object!", null, null)
        }
    }


    /**
     *
     */
    fun findWorkLogForCurrentUser(fromDate: LocalDate, toDate: LocalDate): List<Worklog> {
        val jql = MessageFormat.format(
            "worklogAuthor=currentUser() and worklogDate>={0} and worklogDate<={1}",
            formatDate(fromDate), formatDate(toDate)
        )
        val data = jiraClient.search(jql)

        val fromDateTime = ZonedDateTime.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()))
        val toDateTime = ZonedDateTime.from(toDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()))

        return data.issues.map { issue ->
            jiraClient.getWorkLog(issue.id).worklogs.filter { worklog ->
                worklog.author.accountId == jiraConfig.accountId() &&
                        worklog.created.isAfter(fromDateTime) && worklog.created.isBefore(
                    toDateTime
                )
            }
        }.flatten()

    }



    private fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ISO_DATE)
    }
}
