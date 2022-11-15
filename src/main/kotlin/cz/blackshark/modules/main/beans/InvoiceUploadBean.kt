package cz.blackshark.modules.main.beans

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.converter.InvoiceMapper
import cz.blackshark.modules.main.domains.BeanProcessingResult
import cz.blackshark.modules.main.domains.FileWithMetadata
import cz.blackshark.modules.main.dto.IncomingInvoiceMetadataVo
import cz.blackshark.modules.main.dto.InvoiceRequest
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import org.jboss.logging.Logger
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.InternalServerErrorException

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

    @Throws(CompanyExcetption::class)
    fun processUpload2(invoiceRequest: InvoiceRequest): BeanProcessingResult {
        val issuer = companyRepository.find("id=?1", invoiceRequest.issuerId).firstResultOptional<CompanyEntity>()
        if (issuer.isEmpty) {
            logger.errorv("Invalid issuer. Company with ID {} not found!", invoiceRequest.issuerId)
            throw CompanyExcetption("Issuer not found for id ${invoiceRequest.issuerId}")
        }

        val recipient = companyRepository.find("id=?1", invoiceRequest.recipientId).firstResultOptional<CompanyEntity>()
        if (recipient.isEmpty) {
            logger.errorv("Invalid recipient. Company with ID {} not found!", invoiceRequest.recipientId)
            throw CompanyExcetption("Recipient not found for id ${invoiceRequest.recipientId}")
        }
        return processUpload2WithCompanies(issuer.get(), recipient.get(), invoiceRequest)
    }

    private fun processUpload2WithCompanies(
        issuer: CompanyEntity,
        recipient: CompanyEntity,
        request: InvoiceRequest
    ): BeanProcessingResult {
        // 1. save content
        val path = if (request.file != null) saveDataFile(request, issuer); else null
        // 2. create entity
        val invoiceEntity = InvoiceEntity().apply {
            this.number = request.invoiceNumber
            this.issuerCompany = issuer
            this.recipientCompany = recipient
            this.issueDate = LocalDate.parse(request.issueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            this.paymentDate = LocalDate.parse(request.paymentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            this.vatPaymentDate = LocalDate.parse(request.vatPaymentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            this.storePath = path
        }
        invoiceRepository.persist(invoiceEntity)
        return BeanProcessingResult(false, null, invoiceEntity)
    }

    // Save incoming file and return system path
    private fun saveDataFile(invoiceRequest: InvoiceRequest, issuer: CompanyEntity): String {

        val storePath = config.fileStoragePath()

        val filePartPath = with(LocalDate.parse(invoiceRequest.issueDate)) {
            "/${issuer.ic}/${this.format(DateTimeFormatter.ofPattern("yyyy"))}/${
                this.format(DateTimeFormatter.ofPattern("MM"))
            }"
        }
        val completePath = "$storePath/$filePartPath"
        val path = File(completePath)
        return with(path) {
            val dirOK = this.exists() || this.mkdirs()
            if (dirOK) {
                val filePath = "$completePath/${invoiceRequest.fileName}"
                FileInputStream(invoiceRequest.file).use {
                    Files.write(Paths.get(filePath), it.readBytes(), StandardOpenOption.CREATE_NEW)
                }
                filePartPath+"/${invoiceRequest.fileName}"
            } else {
                throw InternalServerErrorException("Can not create directory for the store!")
            }
        }
    }

}
