package cz.blackshark.modules.main.beans

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.domains.ValidationError
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import cz.blackshark.tools.MD5
import org.jboss.resteasy.plugins.providers.multipart.InputPart
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.FileSystemException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.GenericType

@ApplicationScoped
class RequisitionBean @Inject constructor(
    private val appConf: ApplicationConfig,
    private val requisitionRepository: RequisitionRepository,
    private val companyRepository: CompanyRepository
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RequisitionBean::class.java)
    }

    @PostConstruct
    fun initialize() {

        val path = Paths.get(appConf.fileStoragePath()).toFile();
        if (!path.exists()) {
            logger.info("File Storage ${appConf.fileStoragePath()} does not exists. Try create storage!")
            if (!path.mkdirs()) {
                logger.error("Can not create file storage ${appConf.fileStoragePath()}")
                throw FileNotFoundException("Can not create file storage ${appConf.fileStoragePath()}")
            }
        }
    }

    fun validateIncomingMultipart(input: MultipartFormDataInput): Pair<ValidationError?, String?> {
        if (input.formDataMap["file"]?.get(0) == null) {
            return Pair(
                ValidationError(MultipartFormDataInput::class.java, "file", "File is not present in request!"),
                null
            )
        }

        val filePart: InputPart = input.formDataMap["file"]!!.first()

        if (filePart.headers["Content-Disposition"]?.firstOrNull() == null) {
            return Pair(
                ValidationError(
                    MultipartFormDataInput::class.java,
                    "file",
                    "Missing required header Content-Disposition on file part"
                ), null
            )
        }

        val contentDispostion: String = filePart.headers["Content-Disposition"]!!.first()
        val fileNameContent: String = contentDispostion.split(";").find { it.trim().startsWith("filename") }
            ?: return Pair(
                ValidationError(
                    MultipartFormDataInput::class.java,
                    "file",
                    "Content-Disposition does not contain filename"
                ), null
            )

        val fileName: String = fileNameContent.split("=")[1].replace("\"", "")

        return Pair(null, fileName)

    }


    /**
     * Save metadata attachment into database and file to FS store
     *
     * @return new Database row PK
     */
    fun processValidIncomingMultipart(input: MultipartFormDataInput, fileName: String): Long? {

        var res: Long? = null

        val file = input.formDataMap["file"]!![0]
        if (file is MultipartInputImpl.PartImpl) {
            logger.info("Save file $fileName")
            val iStr: InputStream = file.getBody(InputStream::class.java, null)

            val fsExt = fileName.split(".").reversed()[0]
            val fsName = MD5.asHex(
                fileName + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"))
            ) + "." + fsExt
            val path = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"))
            val fullPath = appConf.fileStoragePath() + "/" + path

            if (!Paths.get(fullPath).toFile().exists()) {
                if (!Paths.get(fullPath).toFile().mkdirs()) {
                    throw FileSystemException("Can not create directory to store incoming file. Path: $fullPath")
                }
            }
            logger.info("Write file $fullPath/$fsName")
            Files.write(Paths.get("$fullPath/$fsName"), iStr.readBytes(), StandardOpenOption.CREATE_NEW)

            try {

                val requisitionEntity = RequisitionEntity()
                input.formDataMap.keys.forEach { key ->
                    val data = input.formDataMap[key]
                    when (key) {
                        "note" -> requisitionEntity.note =
                            String(data!!.first().getBody(GenericType(ByteArray::class.java)), Charset.forName("UTF-8"))
                        "companyId" -> requisitionEntity.company =
                            companyRepository.findById(data!!.first().bodyAsString.toLong())
                        "orderNumber" -> requisitionEntity.customerNumber = data!!.first().bodyAsString
                        "startDate" -> requisitionEntity.startDate =
                            LocalDate.parse(data!!.first().bodyAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        "endDate" -> requisitionEntity.endDate =
                            LocalDate.parse(data!!.first().bodyAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }
                }

                requisitionEntity.path = "$path/$fsName"
                requisitionEntity.origFileName = fileName
                requisitionRepository.persistAndFlush(requisitionEntity)
                if (requisitionEntity.id != null) {
                    logger.info("File requisition saved with id ${requisitionEntity.id} and file $fullPath/$fsName")
                    res = requisitionEntity.id
                }
            } catch (e: Exception) {
                logger.error("Can not save requisition into to the database", e)
                logger.error("Can not save requisition to database! Delete stored file from the FS")
                Paths.get("$fullPath/$fsName").toFile().delete()
            }

        }

        return res

    }

}
