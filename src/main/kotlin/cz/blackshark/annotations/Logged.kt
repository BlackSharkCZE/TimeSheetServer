package cz.blackshark.annotations

import javax.interceptor.InterceptorBinding

@InterceptorBinding
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logged()
