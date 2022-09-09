package cz.blackshark.modules.jira.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDetails(
    val accountId: String,
    val emailAddress: String?,
    val displayName: String,
)
