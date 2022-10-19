package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
open class CompanyRepository : PanacheRepositoryBase<CompanyEntity, Long> {

	fun findPrimaryCompany(): CompanyEntity? {
		return find("#CompanyEntity.findPrimary").firstResult()
	}

	fun findWithPrimary(includePrimary: Boolean): List<CompanyEntity> {
		return if (includePrimary) {
			findAll().list()
		} else {
			find("primary!=true").list()
		}
	}


}
