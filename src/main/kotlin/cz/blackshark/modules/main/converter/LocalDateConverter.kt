package cz.blackshark.modules.main.converter

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.ws.rs.ext.ParamConverter

class LocalDateConverter<T>(private val pattern: String) : ParamConverter<T> {

    override fun toString(value: T?): String? {
        if (value != null) {
            return (value as LocalDate).format(DateTimeFormatter.ofPattern(pattern))
        } else {
            return null
        }
    }

    @SuppressWarnings("Unchecked")
    override fun fromString(value: String?): T? {
        return if (value != null) {
            LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern)) as T
        } else {
            null
        }

    }
}
