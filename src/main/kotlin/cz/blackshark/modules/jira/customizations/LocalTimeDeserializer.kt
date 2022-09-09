package cz.blackshark.modules.jira.customizations

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalTime

class LocalTimeDeserializer : JsonDeserializer<LocalTime?>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalTime? {
        return if (p != null) {
            LocalTime.parse(p.valueAsString)
        } else {
            null
        }
    }
}
