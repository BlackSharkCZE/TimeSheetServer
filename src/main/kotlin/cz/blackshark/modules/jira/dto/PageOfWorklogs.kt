package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PageOfWorklogs(
    val startAt: Int,
    val maxResults: Int,
    val total: Int,
    val worklogs: List<Worklog>
)
