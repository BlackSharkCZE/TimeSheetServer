package cz.blackshark.modules.main.http.filter

import io.vertx.core.http.HttpServerRequest
import org.apache.commons.io.IOUtils
import org.jboss.logging.Logger
import org.slf4j.MDC
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.Context
import javax.ws.rs.core.UriInfo
import javax.ws.rs.ext.Provider
import javax.ws.rs.ext.WriterInterceptor
import javax.ws.rs.ext.WriterInterceptorContext

@Provider
class LoggingFilter : ContainerRequestFilter, ContainerResponseFilter, WriterInterceptor {

    companion object {
        const val CORRELATION_ID = "X-Correlation-Id"
    }

    @Inject
    lateinit var logger: Logger

    @Context
    private lateinit var info: UriInfo

    @Context
    private lateinit var request: HttpServerRequest

    override fun aroundWriteTo(context: WriterInterceptorContext) {
        val orginalStream = context.outputStream
        val baos = ByteArrayOutputStream()
        context.outputStream = baos
        try {
            context.proceed()
        } finally {
            logger.tracef("Response Data: %s", baos.toString("UTF-8"))
            baos.writeTo(orginalStream)
            baos.close()
            context.outputStream = orginalStream
        }
    }

    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {

        if (logger.isTraceEnabled) {
            logger.tracef(
                "Response Status: %s, Content-length: %s, Content-Type: %s.",
                responseContext.statusInfo,
                responseContext.length,
                responseContext.mediaType
            )
        } else {
            logger.debugf(
                "Response Status: %s, Content-length: %s, Content-Type: %s",
                responseContext.statusInfo,
                responseContext.length,
                responseContext.mediaType
            )
        }
    }

    override fun filter(requestContext: ContainerRequestContext) {
        MDC.clear()
        MDC.put(CORRELATION_ID, UUID.randomUUID().toString())
        requestContext.setProperty(CORRELATION_ID, MDC.get(CORRELATION_ID))

        if (logger.isTraceEnabled) {
            val iStream: InputStream? = requestContext.entityStream
            if (iStream != null) {
                val out = ByteArrayOutputStream()
                IOUtils.copy(iStream, out)
                val data = out.toByteArray()
                logger.tracef(
                    "Request %s %s from IP %s. Data: %s",
                    requestContext.method,
                    info.path,
                    request.remoteAddress().toString(),
                    String(data)
                )
                requestContext.entityStream = ByteArrayInputStream(data)
            } else {
                logger.debugf(
                    "Request %s %s from IP %s",
                    requestContext.method,
                    info.path,
                    request.remoteAddress().toString()
                )
            }
        }
    }
}
