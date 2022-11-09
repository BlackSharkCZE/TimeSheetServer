package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.modules.main.dto.BillingLineVo
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager

@Suppress("UNCHECKED_CAST")
@ApplicationScoped
class BillingDao {

    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var entityManager: EntityManager

    fun getBillingList(subjectEntity: SubjectEntity): List<BillingLineVo> {
        try {
            val res = entityManager.createNativeQuery(BILLING_LIST, "BillingLineVoMapping")
                .setParameter("subjectID", subjectEntity.id)
                .resultList
            return res as List<BillingLineVo>
        } catch (e: Exception) {
            logger.error("Can not load billing", e)
            throw e
        }

    }

    companion object Queries {

        private const val BILLING_LIST = """
       select c.company_name,
        c.id                                                                            as company_id,
        sum(cast(extract(EPOCH from b.to_time - b.from_time) as int) / 60 - b.pause)            as work_time,
        sum((cast(extract(EPOCH from b.to_time - b.from_time) as int) / 60.0 - b.pause) / 60.0) * rate_limited.rate / 8.0 as earn,
        requisition_limited.note
            from v_billing b
            join project p on b.project_id = p.id
            join company c on p.company_id = c.id
            join (select distinct on (company_id) id, company_id, note
                    from requisition
                        order by company_id, start_date desc) requisition_limited 
               on requisition_limited.company_id = c.id
         join (select distinct on (company_id) rate, company_id
                    from rate
                        where subject_id = :subjectID
                            order by company_id, valid_since desc) rate_limited on rate_limited.company_id = c.id
        where b.subject_id = :subjectID
        group by c.company_name, c.id, requisition_limited.note, rate_limited.rate
        order by c.company_name
"""

    }

}
