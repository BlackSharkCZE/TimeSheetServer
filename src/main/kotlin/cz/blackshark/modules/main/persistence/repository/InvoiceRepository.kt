package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.InvoiceEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InvoiceRepository : PanacheRepositoryBase<InvoiceEntity, Long> {

	fun findIssued(companyID: Long, fromDate: LocalDate, toDate: LocalDate): List<InvoiceEntity> {
		return find("issuerCompany.id=?1 and issueDate>=?2 and issueDate <= ?3", companyID, fromDate, toDate)
			.list<InvoiceEntity>()
	}

	fun findReceived(companyID: Long, fromDate: LocalDate, toDate: LocalDate): List<InvoiceEntity> {
		return find("recipientCompany.id=?1 and vatPaymentDate>=?2 and vatPaymentDate <= ?3", companyID, fromDate, toDate)
			.list<InvoiceEntity>()
	}

}
