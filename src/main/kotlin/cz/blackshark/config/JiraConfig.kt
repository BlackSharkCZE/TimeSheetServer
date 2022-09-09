package cz.blackshark.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "jira")
interface JiraConfig {

    fun username(): String
    fun token(): String
    fun accountId(): String
}
