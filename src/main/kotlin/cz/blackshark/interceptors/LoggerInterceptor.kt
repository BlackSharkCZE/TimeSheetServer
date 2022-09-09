package cz.blackshark.interceptors

import cz.blackshark.annotations.Logged
import org.jboss.logging.Logger
import java.util.*
import javax.annotation.Priority
import javax.inject.Inject
import javax.interceptor.AroundInvoke
import javax.interceptor.Interceptor
import javax.interceptor.InvocationContext

@Interceptor
@Logged
@Priority(2020)
open class LoggerInterceptor @Inject constructor(private val logger: Logger) {

    @AroundInvoke
    fun processLogger(invocationContext: InvocationContext): Any? {
        logger.infof(
            "Invocation on context: %s::%s. Params: %s", invocationContext.method.declaringClass.name,
            invocationContext.method.name, invocationContext.parameters?.joinToString(",", transform = Objects::toString)
        )
        return invocationContext.proceed()
    }

}
