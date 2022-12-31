package cz.blackshark.modules.main.http.mappers


import cz.blackshark.modules.commons.model.RestResponse
import org.jboss.logging.Logger
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ExceptionMappers : ExceptionMapper<Exception> {

    @Inject
    private lateinit var logger: Logger

    override fun toResponse(exception: Exception): Response {
        if (exception is NullPointerException) {
            logger.error("Mapping NPE", exception.cause)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(RestResponse<Any>(false, "NullPointer Exception", null, 500))
                .build()
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(RestResponse<Any>(false, exception.message, null, 500))
                .build()
        }

    }
}
