package cz.blackshark.config

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithDefault

@ConfigMapping(prefix = "application")
interface ApplicationConfig {

    @WithDefault("yyyy-MM-dd")
    fun jaxRsDateFormat(): String
    fun fileStoragePath(): String

}
