package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.Hql
import cz.blackshark.modules.main.persistence.RepositoryResult
import cz.blackshark.modules.main.persistence.Sql
import cz.blackshark.modules.main.persistence.entity.TimelineEntity
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Page
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TimelineRepository : PanacheRepositoryBase<TimelineEntity, Long> {

    fun isTimeAlreadyUsed(time: ZonedDateTime, exceptionId: Long?, subjectId: Long): Boolean {
        val c = if (exceptionId == null) {
            count(Hql.COUNT_BY_USED_TIME, mapOf("time" to time, "subject" to subjectId))
        } else {
            count(
                Hql.COUNT_BY_USED_TIME_WITH_ID_EXCEPTION,
                mapOf("time" to time, "id" to exceptionId, "subject" to subjectId)
            )
        }
        return c > 0
    }

    @Deprecated(message = "Unused method")
    fun findByTimeRange(fromTime: ZonedDateTime, toTime: ZonedDateTime): List<TimelineEntity> {
        val pQuery: PanacheQuery<TimelineEntity> = find(
            Hql.FIND_TIMELINE_BETWEEN,
            mapOf(
                "fromDate" to fromTime,
                "toDate" to toTime
            )
        )
        return pQuery.list()
    }

    fun findAllWithDate(
        fromDate: LocalDate,
        toDate: LocalDate,
        page: Int,
        pageSize: Int
    ): RepositoryResult<TimelineEntity> {
        val pQuery: PanacheQuery<TimelineEntity> = find(
            Hql.FIND_TIMELINE_WITH_DATE,
            mapOf(
                "fromDate" to fromDate.atStartOfDay().atZone(ZoneId.systemDefault()),
                "toDate" to toDate.atStartOfDay().with(LocalTime.MAX).atZone(ZoneId.systemDefault())
            )
        )

        return RepositoryResult(
            pQuery.page<TimelineEntity>(Page.of(page, pageSize)).list(),
            pageSize,
            pQuery.pageCount()
        )
    }

    /**
     * Return current worktime for the given month
     */
    fun currentWorkTimeInMonth(date: LocalDate): BigDecimal {
        return BigDecimal.valueOf(
            entityManager
                .createNativeQuery(Sql.WorkTime.WORK_TIME)
                .setParameter("fromTime", date.withDayOfMonth(1))
                .setParameter("toTime", date.with(TemporalAdjusters.lastDayOfMonth()))
                .singleResult as Double
        )
    }


}
