package cz.blackshark.modules.main.beans

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.converter.InvoiceMapper
import cz.blackshark.modules.main.domains.BeanProcessingResult
import cz.blackshark.modules.main.domains.FileWithMetadata
import cz.blackshark.modules.main.dto.IncomingInvoiceMetadataVo
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import org.jboss.logging.Logger
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import java.nio.file.Files
import java.nio.file.Paths
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.BadRequestException

@ApplicationScoped
class InvoiceUploadBean @Inject constructor(
    val logger: Logger,
    val fileSavingBean: FileSavingBean,
    val config: ApplicationConfig,
    val companyRepository: CompanyRepository,
    val invoiceRepository: InvoiceRepository
) {

    fun processUpload(body: MultipartFormDataInput): BeanProcessingResult {
        val metadata = fileSavingBean.getIncomingFileMetadata<IncomingInvoiceMetadataVo>(
            body,
            IncomingInvoiceMetadataVo::class.java
        )
        val issuer = companyRepository.findById(metadata.issuerID)
        val fileWithMetadata = fileSavingBean.processUploadInvoice(body, config.fileStoragePath(), metadata, issuer)
        return if (fileWithMetadata != null) {
            val (issuer1, recipient) = findCompanies(fileWithMetadata)
            return processValidUpload(fileWithMetadata, issuer1, recipient)
        } else {
            BeanProcessingResult(true, "SAVE_FILE_FAILED")
        }
    }


    private fun processValidUpload(
        data: FileWithMetadata<IncomingInvoiceMetadataVo>,
        issuer: CompanyEntity,
        recipient: CompanyEntity
    ): BeanProcessingResult {
        val entity: InvoiceEntity =
            InvoiceMapper.convert(data.metadata, data.fileUploadResult.storePath, issuer, recipient)
        invoiceRepository.persist(entity)
        return if (entity.id == null) {
            Files.delete(Paths.get(config.fileStoragePath() + "/" + data.fileUploadResult.systemFileName))
            BeanProcessingResult(true, "SAVE_METADATA_FAILED")
        } else {
            BeanProcessingResult(false, "OK", entity)
        }
    }

    private fun findCompanies(data: FileWithMetadata<IncomingInvoiceMetadataVo>): Pair<CompanyEntity, CompanyEntity> {
        val issuer = companyRepository.find("id=?1", data.metadata.issuerID).firstResultOptional<CompanyEntity>()
        val recipient = companyRepository.find("id=?1", data.metadata.recipientID).firstResultOptional<CompanyEntity>()

        if (issuer.isEmpty) {
            logger.errorv("Invalid issuer. Company with ID {} not found!", data.metadata.issuerID)
            throw BadRequestException("Issuer with ID  ${data.metadata.issuerID} not found.")
        }

        if (recipient.isEmpty) {
            logger.errorv("Invalid recipient. Company with ID {} not found!", data.metadata.recipientID)
            throw BadRequestException("Recipient with ID  ${data.metadata.recipientID} not found.")
        }

        return Pair(issuer.get(), recipient.get())

    }

}