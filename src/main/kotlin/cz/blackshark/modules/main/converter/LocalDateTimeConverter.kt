package cz.blackshark.modules.main.converter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.ws.rs.ext.ParamConverter

class LocalDateTimeConverter<T> : ParamConverter<T> {

    override fun toString(value: T?): String? =
        (value as? LocalDateTime)?.format(DateTimeFormatter.ISO_DATE_TIME)


    override fun fromString(value: String?): T? = LocalDateTime.parse(value) as T
}
