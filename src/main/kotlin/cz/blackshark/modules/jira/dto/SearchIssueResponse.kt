package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchIssueResponse(
    val maxResult: Int,
    val total: Int,
    val issues: List<JiraIssue>
)
