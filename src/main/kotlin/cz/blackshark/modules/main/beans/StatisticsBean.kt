package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.entity.EarnHistoryView
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.EarnHistoryRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class StatisticsBean @Inject constructor(
    private val earnHistoryRepository: EarnHistoryRepository,
    private val companyRepository: CompanyRepository
) {

    fun findForPrimaryCompany(retrieveSubject: SubjectEntity): List<EarnHistoryView> {
        val company = companyRepository.findPrimaryCompany() ?: throw CompanyExcetption("Can not find primary company!")
        return company.id?.let { earnHistoryRepository.findByIssuer(it) }
            ?: throw CompanyExcetption("Can not get earn history for company")
    }
}
