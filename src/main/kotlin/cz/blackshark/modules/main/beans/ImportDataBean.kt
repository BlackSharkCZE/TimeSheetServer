package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.dto.GenericImportRequest
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.PaymentRepository
import io.quarkus.logging.Log
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.BadRequestException

@ApplicationScoped
class ImportDataBean {

    @Inject
    private lateinit var paymentRepository: PaymentRepository

    @Inject
    private lateinit var companyRepository: CompanyRepository

    @Inject
    private lateinit var mBankCsvParser: MBankCsvParser

    fun importOutgoingPayments(importData: GenericImportRequest) {
        val company = companyRepository.findPrimaryCompany() ?: throw CompanyExcetption("PrimaryCompany not found")
        importData.file?.let { it ->
            val iData = mBankCsvParser.parseOperationList(it, company)
            Log.infof("Number of items to imports: %d", iData.size)
            paymentRepository.persist(iData)
            Log.infof("Number of items stored in database: %d", iData.count { it.id != null })
        } ?: throw BadRequestException("File not present in request!")
    }
}
