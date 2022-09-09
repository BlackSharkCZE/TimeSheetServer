package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.RequisitionEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequisitionRepository : PanacheRepositoryBase<RequisitionEntity, Long> {

	fun findActive(forDate: LocalDate, companyID: Long): RequisitionEntity? {
		return find("SELECT r FROM RequisitionEntity r WHERE r.company.id=:companyID AND :forDate between startDate AND endDate", mapOf("companyID" to companyID, "forDate" to forDate))
				.firstResult()
	}

	fun findNewest(companyID: Long): RequisitionEntity? {
		return find("SELECT r FROM RequisitionEntity r WHERE r.company.id=:companyID ORDER BY startDate DESC",mapOf("companyID" to companyID))
				.firstResult()
	}

}
