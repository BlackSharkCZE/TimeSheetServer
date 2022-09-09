package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime


@JsonIgnoreProperties(ignoreUnknown = true)
data class Worklog(
    val self: String, // The url of the worklog item
    val created: ZonedDateTime,
    val updated: ZonedDateTime,
    val started: ZonedDateTime,
    val timeSpent: String,
    val timeSpentSeconds: Int,
    val id: String,
    val author: UserDetails
)
