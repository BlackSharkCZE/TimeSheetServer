package cz.blackshark.modules.main.domains

/**
 * Describe success result of the file upload
 */
data class FileUploadResult(
    val origFileName: String,
    val systemFileName: String,
    val fileSize: Long,
    val filePath: String,
    val storePath: String
)
