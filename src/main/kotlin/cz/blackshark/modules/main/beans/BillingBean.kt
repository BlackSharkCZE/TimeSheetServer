package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.dto.BillingLineVo
import cz.blackshark.modules.main.persistence.dao.BillingDao
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class BillingBean @Inject constructor(private val billingDao: BillingDao) {

    fun getBillingList(subjectEntity: SubjectEntity, toDate: LocalDate): List<BillingLineVo> {
        return billingDao.getBillingList(subjectEntity, toDate)
    }

}
