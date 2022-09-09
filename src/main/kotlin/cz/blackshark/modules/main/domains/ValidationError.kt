package cz.blackshark.modules.main.domains

data class ValidationError(val clazz: Class<*>, val field: String, val message: String)
