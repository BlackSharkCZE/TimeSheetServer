package cz.blackshark.modules.main.http.mappers


import cz.blackshark.modules.commons.model.RestResponse
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ExceptionMappers : ExceptionMapper<Exception> {

    override fun toResponse(exception: Exception): Response {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(RestResponse<Any>(false, exception.message, null, 500))
            .build()
    }
}
