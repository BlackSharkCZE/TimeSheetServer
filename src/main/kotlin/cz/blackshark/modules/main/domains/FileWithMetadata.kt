package cz.blackshark.modules.main.domains

data class FileWithMetadata<T>(
    val metadata: T,
    val fileUploadResult: FileUploadResult
)
