package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class JiraWorklog(
    @field:JsonIgnore
    val ticket: String,
    @field:JsonProperty("timeSpent")
    val timeSpent: String,
    val started: ZonedDateTime,
    @field:JsonProperty("comment")
    val comment: JiraComment
)

data class JiraComment(
    @field:JsonProperty("type")
    val type: String,
    @field:JsonProperty("version")
    val version: Int,
    @field:JsonProperty("content")
    val content: List<JiraContent>?
)

data class JiraContent(
    @field:JsonProperty("type")
    val type: String,
    @field:JsonProperty("content")
    val content: List<JiraContent>?,
    @field:JsonProperty("text")
    val text: String?
)
