package cz.blackshark.modules.jira.customizations

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime>() {


    override fun deserialize(value: JsonParser?, p1: DeserializationContext): ZonedDateTime? {
        if (value != null) {
            return ZonedDateTime.parse(value.valueAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
        } else {
            return null
        }
    }
}
