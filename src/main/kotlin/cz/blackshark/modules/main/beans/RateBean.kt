package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.dto.RateVo
import cz.blackshark.modules.main.exceptions.CompanyExcetption
import cz.blackshark.modules.main.persistence.entity.RateEntity
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import cz.blackshark.modules.main.persistence.repository.CompanyRepository
import cz.blackshark.modules.main.persistence.repository.RateRepository
import io.quarkus.panache.common.Sort
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class RateBean @Inject constructor(
    private val logger: Logger,
    private val subjectBean: SubjectBean,
    private val companyRepository: CompanyRepository,
    private val rateRepository: RateRepository
) {

    fun createRate(rateVo: RateVo, subjectEntity: SubjectEntity): RateVo {
        logger.info("Create rate for subject $subjectEntity with data $rateVo")
        val company = companyRepository.findByIdOptional(rateVo.companyId).orElseGet { null }
            ?: throw CompanyExcetption("Company not found!")

        val rate = RateEntity().apply {
            this.companyEntity = company
            this.subject = subjectEntity
            this.rate = rateVo.rate
            this.validSince = rateVo.validSince
        }

        rateRepository.persist(rate)

        return RateVo(rate.id, rateVo.rate, rateVo.validSince, company.id!!)
    }

    fun findForSubject(subjectEntity: SubjectEntity): List<RateVo> {
        logger.info("Search for Rate for subject $subjectEntity")
        return rateRepository
            .find(
                "subject.id",
                Sort.ascending("companyEntity.companyName", "validSince"),
                subjectEntity.id
            )
            .list<RateEntity?>()
            .map { RateVo(it.id, it.rate, it.validSince, it.companyEntity!!.id!!, it.companyEntity!!.companyName) }
    }


}
