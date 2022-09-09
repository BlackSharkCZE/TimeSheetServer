package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.persistence.entity.InvoiceNumberEntity
import cz.blackshark.modules.main.persistence.repository.InvoiceNumberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class InvoiceNumberGenerator @Inject constructor(
    private val invoiceNumberRepository: InvoiceNumberRepository
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(InvoiceNumberGenerator::class.java)

        const val GEN_NUMBER_LENGTH = 4
    }

    fun createForCompany(companyID: Long, prefix: Long): InvoiceNumberEntity {
        val entity = InvoiceNumberEntity()
        entity.companyID = companyID
        entity.currentValue = 1
        entity.prefix = prefix
        invoiceNumberRepository.persistAndFlush(entity)
        return entity
    }

    fun getNextNumber(companyID: Long): String? {
        val iNum = invoiceNumberRepository.nextInvoiceNumber(companyID)
        return if (iNum != null) {
            val sb = StringBuilder()
            sb.append(iNum.prefix)
                .append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")))
                .append(iNum.currentValue.toString().padStart(GEN_NUMBER_LENGTH, '0'))
            sb.toString()
        } else {
            logger.error("Can not load invoiceNumber for the company $companyID. InvoiceNumber is null")
            null
        }


    }

}
