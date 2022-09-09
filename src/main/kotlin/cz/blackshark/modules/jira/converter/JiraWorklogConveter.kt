package cz.blackshark.modules.jira.converter

import cz.blackshark.modules.jira.dto.JiraComment
import cz.blackshark.modules.jira.dto.JiraContent
import cz.blackshark.modules.jira.dto.JiraWorklog
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import org.jboss.logging.Logger
import java.time.Duration

object JiraWorklogConveter {

    private val logger = Logger.getLogger(JiraWorklogConveter::class.java)

    private val pattern = Regex("^(IPF-\\d{2,4})\\s-\\s+(.*)")

    fun convert(timelineEntity: TimelineEntity): JiraWorklog? {

        val matcher = pattern.matchEntire(timelineEntity.note)
        return if (matcher != null && matcher.groups.size == 3) {
            val ticket = matcher.groups[1]!!.value
            val note = matcher.groups[2]?.value
            val duration = Duration.between(timelineEntity.fromTime, timelineEntity.toTime)
                .minusMinutes(timelineEntity.pause.toLong())
            val timespent = "${duration.toMinutes() / 60.0}h"
            JiraWorklog(
                ticket,
                timespent, timelineEntity.fromTime,
                JiraComment(
                    "doc", 1, listOf(
                        JiraContent(
                            "paragraph", listOf(
                                JiraContent("text", null, note)
                            ), null
                        )
                    )
                )
            )
        } else {
            logger.warn("Note does match pattern to parse note as JIRA ticket. Note: \"${timelineEntity.note}\" for Timeline ID: ${timelineEntity.id}")
            null
        }
    }
}
