package cz.blackshark.modules.main.domains


data class OperationResult(
    val success: Boolean,
    val itemID: Long?,
    val list: List<ValidationError>
)
