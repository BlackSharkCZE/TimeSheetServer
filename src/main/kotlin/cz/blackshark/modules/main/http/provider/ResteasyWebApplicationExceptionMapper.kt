package cz.blackshark.modules.main.http.provider

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper
import org.jboss.logging.Logger
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException
import javax.enterprise.inject.spi.CDI
import javax.inject.Inject
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider


//@Provider ... it will be used for every response
class ResteasyWebApplicationExceptionMapper : ResponseExceptionMapper<ResteasyWebApplicationException> {

    @Inject
    lateinit var logger: Logger

    override fun toThrowable(response: Response): ResteasyWebApplicationException {
        // CDI.current().select(Logger::class.java).get()

        logger.errorf("Response error. Http status %d. Url: %s. Data: %s", response.status, response.location?.toASCIIString(),response.readEntity(String::class.java))
        return ResteasyWebApplicationException(WebApplicationException("Response status code ${response.status}"))
    }
}
