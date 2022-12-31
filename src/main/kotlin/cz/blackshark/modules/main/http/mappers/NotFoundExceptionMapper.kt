package cz.blackshark.modules.main.http.mappers

import io.quarkus.logging.Log
import java.io.InputStream
import java.util.Scanner
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class NotFoundExceptionMapper : ExceptionMapper<NotFoundException> {

    override fun toResponse(exception: NotFoundException): Response {
        Log.warnf("Can not find resource: %s", exception.message)
        val text: String = Scanner(
            this.javaClass.getResourceAsStream("/META-INF/resources/index.html") as InputStream,
            "UTF-8"
        ).useDelimiter("\\A").next()
        return Response
            .status(200)
            .entity(text)
            .type(MediaType.TEXT_HTML)
            .build()
    }
}
