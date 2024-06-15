package cz.blackshark.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "gemts")
interface GemTimesheetConfig {
    fun url(): String
    fun username(): String
    fun password(): String
    fun drMaxProjectName(): String
}
