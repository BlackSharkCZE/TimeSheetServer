package cz.blackshark.modules.gemts.converter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalTime

class HoursToJsonDeserializer : JsonDeserializer<LocalTime>() {
    override fun deserialize(parser: JsonParser?, p1: DeserializationContext): LocalTime? {
        val str = parser?.valueAsString
        if (str != null) {
            if (str.startsWith("24:")) {
                return LocalTime.MIDNIGHT
            } else {
                return LocalTime.parse(str)
            }
        } else {
            return null
        }
    }
}
