package cz.blackshark.modules.main.http.filter

import org.apache.commons.io.IOUtils
import org.jboss.logging.Logger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.ws.rs.client.ClientRequestContext
import javax.ws.rs.client.ClientRequestFilter
import javax.ws.rs.client.ClientResponseContext
import javax.ws.rs.client.ClientResponseFilter
import javax.ws.rs.ext.Provider

@Provider
class ClientLoggingFilter : ClientRequestFilter, ClientResponseFilter {

    @Inject
    lateinit var logger: Logger

    override fun filter(requestContext: ClientRequestContext) {
        logger.infof("Client request: %s: %s", requestContext.method, requestContext.uri.toASCIIString())
    }

    override fun filter(requestContext: ClientRequestContext, responseContext: ClientResponseContext) {

        val iStream: InputStream? = responseContext.entityStream
        if (iStream != null) {
            val out = ByteArrayOutputStream()
            IOUtils.copy(iStream, out)
            val data = out.toByteArray()
            logger.tracef(
                "Client Response Status: %s. Content-Length: %d. Data: %s",
                responseContext.status,
                responseContext.length,
                String(data)
            )
            responseContext.entityStream = ByteArrayInputStream(data)
        } else {
            logger.debugf(
                "Client Response Status: %s. Content-Length: %d",
                responseContext.status,
                responseContext.length
            )
        }
    }
}
