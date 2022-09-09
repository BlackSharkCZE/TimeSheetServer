package cz.blackshark.modules.main.beans

import com.fasterxml.jackson.databind.ObjectMapper
import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.domains.FileUploadResult
import cz.blackshark.modules.main.domains.FileWithMetadata
import cz.blackshark.modules.main.dto.IncomingInvoiceMetadataVo
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import org.jboss.logging.Logger
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class FileSavingBean @Inject constructor(
    private val logger: Logger,
    private val objectMapper: ObjectMapper,
    private val appConfig: ApplicationConfig
) {

    companion object {
        val FILE_PART_NAME = "file"
        val METADATA_PART_NAME = "metadata"
    }

    /**
     * Save content of the incoming file and return full path
     * @param body Incoming MultipartFormData containing "file" part
     * @param storePath path where to save file on filesystem
     * @return absolute path of the file or null when save failed
     */
    fun saveIncomingFile(body: MultipartFormDataInput, storePath: String): FileUploadResult? {
        if (body.formDataMap.containsKey(FILE_PART_NAME)) {
            val file = body.formDataMap[FILE_PART_NAME]!![0]
            if (file is MultipartInputImpl.PartImpl) {
                val path = File(storePath)
                val dirOK = path.exists() || path.mkdirs()
                if (dirOK) {
                    val fileNameOfTheData = getFileNameOfTheData(body)
                    val iStr: InputStream = file.getBody(InputStream::class.java, null)
                    val fileName: String = fileNameOfTheData ?: Instant.now().epochSecond.toString()
                    val allFilePath = storePath + File.separator + fileName
                    try {
                        val filePath = Paths.get(allFilePath)
                        Files.write(filePath, iStr.readAllBytes())
                        return FileUploadResult(
                            fileNameOfTheData!!,
                            fileName,
                            Files.size(filePath),
                            allFilePath,
                            allFilePath.replace(appConfig.fileStoragePath(), "")
                        )
                    } catch (e: IOException) {
                        logger.error("Can not save file $allFilePath", e)
                    }
                }
            }
        } else {
            logger.error("Incoming data does not contains \"$FILE_PART_NAME\" part")
        }
        return null
    }

    /**
     * Process metadata on file upload
     * @param body MultipartFormData containing "metadata" part
     * @param expectedClass expected class in metadata
     */
    fun <T> getIncomingFileMetadata(body: MultipartFormDataInput, expectedClass: Class<*>): T {
        if (body.formDataMap.containsKey(METADATA_PART_NAME)) {
            val content = body.formDataMap[METADATA_PART_NAME]!![0]
            if (content is MultipartInputImpl.PartImpl) {
                val iStr = content.getBody(InputStream::class.java, null)
                val ba = iStr.readAllBytes()
                try {
                    val data = objectMapper.readValue(ba, expectedClass) as T
                    return data
                } catch (e: Exception) {
                    logger.error("Can not read JSON from ${String(ba)}")
                }
            }
        }
        throw NoSuchFieldError("Metadata not pStringresent in multipart data input")
    }

    fun processUploadInvoice(
        body: MultipartFormDataInput,
        storePath: String,
        metadata: IncomingInvoiceMetadataVo,
        issuer: CompanyEntity
    ): FileWithMetadata<IncomingInvoiceMetadataVo>? {
        val path = File(storePath)
        val dirOK = path.exists() || path.mkdirs()
        if (dirOK) {
            val file = saveIncomingFile(
                body,
                storePath
                        + "/"
                        + issuer.ic
                        + "/"
                        + metadata.issueDate.format(DateTimeFormatter.ofPattern("YYYY"))
                        + "/"
                        + metadata.issueDate.format(DateTimeFormatter.ofPattern("MM"))
            )
            if (file != null) {
                return FileWithMetadata(metadata, file)
            }
            logger.error("Can not process upload file with metadata. Metadata or file is null!")
        } else {
            logger.error("Store directory $storePath does not exists and can not create one!")
        }
        return null
    }


    private fun getValueOfHeader(strHeader: String, key: String): String? {
        return strHeader.split(";")
            .map { it.trim() }
            .filter { st ->
                val d = st.split("=")
                d.size == 2 && d[0].equals(key, true)
            }.map {
                val d = it.split("=")
                d[1]
            }.map { it.replace("\"", "") }
            .firstOrNull()
    }

    private fun getFileNameOfTheData(body: MultipartFormDataInput): String? {
        return body.parts.filter { part ->
            part.headers.containsKey("Content-Disposition")
        }.map {
            getValueOfHeader(it.headers["Content-Disposition"]!![0], "filename")
        }.firstOrNull()
    }


}
