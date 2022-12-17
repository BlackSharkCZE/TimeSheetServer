package cz.blackshark.modules.main.domains


data class OperationResult<T>(
    val success: Boolean,
    val itemID: Long?,
    val list: List<ValidationError>,
    val data: T? = null
)
