package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.modules.main.dto.BillingLineVo
import cz.blackshark.modules.main.persistence.entity.SubjectEntity
import io.quarkus.logging.Log
import org.jboss.logging.Logger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    fun getBillingList(subjectEntity: SubjectEntity, toDate: LocalDate): List<BillingLineVo> {
        try {
            val res = entityManager.createNativeQuery(BILLING_LIST, "BillingLineVoMapping")
                .setParameter("subjectID", subjectEntity.id)
                .setParameter("toDate", toDate.plusDays(1))
                .resultList
            return res as List<BillingLineVo>
        } catch (e: Exception) {
            logger.error("Can not load billing", e)
            throw e
        }
    }

    fun getBillingList(subjectEntity: SubjectEntity, companyId: Long, toDate: LocalDate): List<BillingLineVo> {
        try {
            Log.infof(
                "Get billing for subject %d and company %d to date: %s",
                subjectEntity.id,
                companyId,
                toDate.plusDays(1).format(
                    DateTimeFormatter.ISO_LOCAL_DATE
                )
            )
            val res = entityManager.createNativeQuery(BILLING_LIST_FOR_COMPANY, "BillingLineVoMapping")
                .setParameter("subjectID", subjectEntity.id)
                .setParameter("companyID", companyId)
                .setParameter("toDate", toDate.plusDays(1))
                .resultList
            return res as List<BillingLineVo>
        } catch (e: Exception) {
            logger.error("Can not load billing", e)
            throw e
        }
    }

    fun markTimeline(subject: SubjectEntity, companyId: Long, invoiceItemId: Long, toDate: LocalDate): Boolean {

        try {
            return entityManager.createNativeQuery(UPDATE_TIMELINE_WITH_INVOICE_ITEM)
                .setParameter("subjectID", subject.id)
                .setParameter("companyID", companyId)
                .setParameter("invoiceItemID", invoiceItemId)
                .setParameter("toDate", toDate.plusDays(1))
                .executeUpdate() > 0
        } catch (e: Exception) {
            logger.error("Can not update timeline with invoice item", e)
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
        where b.subject_id = :subjectID and b.to_time < :toDate
        group by c.company_name, c.id, requisition_limited.note, rate_limited.rate
        order by c.company_name
"""

        private const val BILLING_LIST_FOR_COMPANY = """
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
                        where subject_id = :subjectID and company_id = :companyID
                            order by company_id, valid_since desc) rate_limited on rate_limited.company_id = c.id
        where b.subject_id = :subjectID and b.to_time < :toDate
        group by c.company_name, c.id, requisition_limited.note, rate_limited.rate
        order by c.company_name
"""

        private const val UPDATE_TIMELINE_WITH_INVOICE_ITEM = """
            update timeline
                set invoice_item_id = :invoiceItemID
                    where id in (select b.id
                    from v_billing b
                      join project p on b.project_id = p.id
                      join company c on p.company_id = c.id
                        where b.subject_id = :subjectID and b.to_time < :toDate
                        and c.id = :companyID)
        """


    }

}
