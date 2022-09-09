package cz.blackshark.modules.jira.customizations

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class  ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime>() {

	companion object {
		// 2021-03-31T10:00:00.000+0200
		val FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	}

	override fun serialize(value: ZonedDateTime?, gen: JsonGenerator, serializers: SerializerProvider) {
		if (value != null) {
			gen.writeString(value.format(FORMAT))
		} else {
			gen.writeNull()
		}


	}
}
