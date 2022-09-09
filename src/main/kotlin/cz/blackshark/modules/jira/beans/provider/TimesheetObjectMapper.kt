package cz.blackshark.modules.jira.beans.provider

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import cz.blackshark.modules.jira.customizations.ZonedDateTimeSerializer
import cz.blackshark.modules.jira.Jira
import io.quarkus.jackson.ObjectMapperCustomizer
import java.time.ZonedDateTime
import javax.enterprise.inject.Default
import javax.enterprise.inject.Instance
import javax.inject.Singleton

class TimesheetObjectMapper {

    @Singleton
    @Default
    fun timesheetObjectMapper(customizer: Instance<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        customizer.forEach { it.customize(mapper) }
        mapper.registerModule(KotlinModule())
        mapper.registerModule(JavaTimeModule())
        return mapper
    }

    @Singleton
    @Jira
    fun jiraObjectMapper(customizer: Instance<ObjectMapperCustomizer>): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        customizer.forEach { it.customize(mapper) }
        val simple = SimpleModule()
        simple.addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())
        mapper.registerModule(simple)
        return mapper
    }


}
