package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.entity.BalanceView
import cz.blackshark.modules.main.persistence.repository.BalanceRepository
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class BalanceBean {

    @Inject
    private lateinit var balanceRepository: BalanceRepository

    @Inject
    private lateinit var companyRepository: CompanyRepository

    fun getBalance(): List<BalanceView> {
        return companyRepository.findPrimaryCompany()?.let {
            balanceRepository.find("issuerId=?1 and companyId=?2", Sort.descending("month"), it.id, it.id).list()
        } ?: throw CompanyExcetption("Primary company not found!")
    }
}
