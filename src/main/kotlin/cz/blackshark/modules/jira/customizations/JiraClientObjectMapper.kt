package cz.blackshark.modules.jira.customizations

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import javax.ws.rs.ext.ContextResolver

class JiraClientObjectMapper : ContextResolver<ObjectMapper> {

    override fun getContext(type: Class<*>?): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.findAndRegisterModules()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        val simple = SimpleModule()
        simple.addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())
        simple.addDeserializer(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
        mapper.registerModule(simple)
        mapper.registerKotlinModule()


        return mapper
    }
}
