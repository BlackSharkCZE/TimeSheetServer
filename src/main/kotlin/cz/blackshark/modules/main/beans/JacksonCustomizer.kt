package cz.blackshark.modules.main.beans

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.quarkus.jackson.ObjectMapperCustomizer
import javax.inject.Singleton

@Singleton
class JacksonCustomizer : ObjectMapperCustomizer {

    override fun customize(mapper: ObjectMapper) {
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

    }
}
