package cz.blackshark.modules.main.beans

import cz.blackshark.config.ApplicationConfig
import cz.blackshark.modules.main.domains.InvoiceItem
import cz.blackshark.modules.main.persistence.dao.BillingDao
import cz.blackshark.modules.main.persistence.dao.InvoiceDao
import cz.blackshark.modules.main.persistence.dao.TimelineDao
import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceItemEntity
import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceItemRepository
import cz.blackshark.modules.main.persistence.repository.InvoiceRepository
import cz.blackshark.modules.main.persistence.repository.RequisitionRepository
import org.jboss.logging.Logger
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.InternalServerErrorException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class InvoiceBean @Inject constructor(
    private val invoiceNumberGenerator: InvoiceNumberGenerator,
    private val invoiceRepository: InvoiceRepository,
    private val invoiceItemRepository: InvoiceItemRepository,
    private val requisitionRepository: RequisitionRepository,
    private val timelineDao: TimelineDao,
    private val invoiceDao: InvoiceDao,
    private val logger: Logger,
    private val jasperReportGenerator: JasperReportGenerator,
    private val appConfig: ApplicationConfig,
    private val companyRepository: CompanyRepository,
    private val billingDao: BillingDao
) {

    fun generateInvoice(subject: SubjectEntity, companyId: Long, issueDate: LocalDate): InvoiceEntity {
        val recipient = companyRepository.findById(companyId) ?: throw BadRequestException("Company not found!")
        val issuer = companyRepository.findPrimaryCompany()
            ?: throw InternalServerErrorException("Primary company does not exists!")
        val invoiceNumber = invoiceNumberGenerator.getNextNumber(issuer.id!!)
            ?: throw InternalServerErrorException("Can not get new invoice number!")
        val requisition =
            requisitionRepository.findNewest(companyId) ?: throw InternalServerErrorException("Requisition not found.")

        val iEntity = InvoiceEntity()
        iEntity.issuerCompany = issuer
        iEntity.recipientCompany = recipient
        iEntity.issueDate = issueDate
        iEntity.paymentDate = issueDate.plusDays(14)
        iEntity.vatPaymentDate = issueDate
        iEntity.number = invoiceNumber

        invoiceRepository.persistAndFlush(iEntity)
        logger.infof("New invoice created: %s", iEntity)


        val billing = billingDao.getBillingList(subject, companyId, issueDate)
        billing.forEach {
            val calcVat =if (issuer.platceDph) BigDecimal(1.21); else BigDecimal.ONE
            val invoiceItemEntity = InvoiceItemEntity().apply {
                this.invoiceEntity = iEntity
                this.requisition = requisition
                if (issuer.platceDph) this.vat = BigDecimal(1.21); else BigDecimal.ZERO
                this.price = it.earn
                this.totalPrice = it.earn.times(calcVat)
                this.vat = this.totalPrice.minus(price)
            }
            invoiceItemRepository.persistAndFlush(invoiceItemEntity)
            billingDao.markTimeline(subject, companyId, invoiceItemEntity.id!!, issueDate)
        }

        return invoiceRepository.findById(iEntity.id!!)

    }

    fun generateInvoice(
        fromDate: LocalDate,
        toDate: LocalDate,
        receiverCompanyEntity: CompanyEntity,
        issuerEntity: CompanyEntity
    ): InvoiceEntity? {
        val invoiceNumber = invoiceNumberGenerator.getNextNumber(issuerEntity.id!!)
        if (invoiceNumber != null) {
            return process(fromDate, toDate, receiverCompanyEntity, issuerEntity, invoiceNumber)
        } else {
            logger.error("Can not obtain new InvoiceNumber!")
        }
        return null
    }

    fun generatePDF(invoiceID: Long): Pair<ByteArray, String> {
        val invoiceEntity = try {
            invoiceRepository.findById(invoiceID)
        } catch (e: Exception) {
            throw NotFoundException("Can not find Invoice with ID $invoiceID")
        }
        if (invoiceEntity.storePath == null) {
            return Pair(jasperReportGenerator.generateInvoicePDF(invoiceEntity), invoiceEntity.number!!)
        } else {
            return Pair(
                Files.readAllBytes(Paths.get(appConfig.fileStoragePath() + invoiceEntity.storePath)),
                invoiceEntity.number!!
            )
        }
    }

    @Transactional
    protected fun process(
        fromDate: LocalDate,
        toDate: LocalDate,
        receiverCompanyEntity: CompanyEntity,
        issuerEntity: CompanyEntity,
        invoiceNumber: String
    ): InvoiceEntity? {

        val iEntity = InvoiceEntity()
        iEntity.issuerCompany = issuerEntity
        iEntity.recipientCompany = receiverCompanyEntity
        iEntity.issueDate = toDate
        iEntity.paymentDate = toDate.plusDays(14)
        iEntity.vatPaymentDate = toDate
        iEntity.number = invoiceNumber

        val requisition: RequisitionEntity? =
            requisitionRepository.findActive(toDate, receiverCompanyEntity.id!!) ?: requisitionRepository.findNewest(
                receiverCompanyEntity.id!!
            )
        if (requisition == null) {
            logger.error("Can not find requisition for the company ${receiverCompanyEntity.id}")
            return null
        }

        try {
            invoiceRepository.persistAndFlush(iEntity)
            logger.info("Invoice $iEntity saved!")
            val requisitionItem = InvoiceItemEntity()
            requisitionItem.invoiceEntity = iEntity
            requisitionItem.requisition = requisition
            if (issuerEntity.platceDph) {
                requisitionItem.vat = BigDecimal.valueOf(1.21) //TODO vat from database config
            } else {
                requisitionItem.vat = BigDecimal.ZERO
            }
            invoiceItemRepository.persistAndFlush(requisitionItem)

            timelineDao.assignToInvoiceItem(fromDate, toDate, receiverCompanyEntity.id!!, requisitionItem)
            invoiceItemRepository.updatePriceFromTimeLine(requisitionItem, issuerEntity)

            return iEntity


        } catch (e: Exception) {
            logger.error("Can not save invoice!", e)
            throw e
        }
    }


}
