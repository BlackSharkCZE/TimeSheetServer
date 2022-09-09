package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraIssue(
    val id: String,
    val key: String
)
