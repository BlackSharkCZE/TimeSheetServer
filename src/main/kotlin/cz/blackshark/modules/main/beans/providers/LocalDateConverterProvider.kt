package cz.blackshark.modules.main.beans.providers

import cz.blackshark.modules.main.converter.LocalDateConverter
import cz.blackshark.modules.main.converter.LocalDateTimeConverter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import javax.ws.rs.ext.ParamConverter
import javax.ws.rs.ext.ParamConverterProvider
import javax.ws.rs.ext.Provider

@Provider
class LocalDateConverterProvider : ParamConverterProvider {
    private var jaxRsDateFormat: String = "yyyy-MM-dd"
    override fun <T : Any?> getConverter(
        rawType: Class<T>,
        genericType: Type?,
        annotations: Array<out Annotation>?
    ): ParamConverter<T>? {

        return when (rawType.name) {
            LocalDate::class.java.name -> LocalDateConverter(jaxRsDateFormat)
            LocalDateTime::class.java.name -> LocalDateTimeConverter()
            else -> null
        }

    }
}
