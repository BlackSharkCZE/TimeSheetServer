package cz.blackshark.modules.commons.model

data class RestResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val code: Int? = null
)
