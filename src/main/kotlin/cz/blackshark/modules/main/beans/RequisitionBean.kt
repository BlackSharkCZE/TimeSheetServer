package cz.blackshark.modules.main.beans

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.dto.RequisitionRequest
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import cz.blackshark.tools.MD5
import org.jboss.logging.Logger
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
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
import javax.ws.rs.InternalServerErrorException

@ApplicationScoped
class RequisitionBean @Inject constructor(
    private val logger: Logger,
    private val appConf: ApplicationConfig,
    private val requisitionRepository: RequisitionRepository,
    private val companyRepository: CompanyRepository
) {

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

    fun save(requisitionRequest: RequisitionRequest): Long {
        return try {
            val (path, fsName) = FileInputStream(requisitionRequest.file).use {
                saveFileToStore(requisitionRequest.fileName, it)
            }
            val requisitionEntity = RequisitionEntity().apply {
                this.note = requisitionRequest.note
                this.path = "$path/$fsName"
                this.company = companyRepository.findById(requisitionRequest.companyId)
                this.customerNumber = requisitionRequest.orderNumber
                this.startDate =
                    LocalDate.parse(requisitionRequest.startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                this.endDate = LocalDate.parse(requisitionRequest.endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                this.origFileName = requisitionRequest.fileName
            }
            requisitionRepository.persistAndFlush(requisitionEntity)
            requisitionEntity.id!!
        } catch (e: Exception) {
            logger.errorf(e, "Can not save incoming file! ${e.message}")
            throw InternalServerErrorException(e)
        }
    }

    private fun saveFileToStore(fileName: String, iStr: InputStream): Pair<String, String> {
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
        return Pair(path, fsName)
    }
}
