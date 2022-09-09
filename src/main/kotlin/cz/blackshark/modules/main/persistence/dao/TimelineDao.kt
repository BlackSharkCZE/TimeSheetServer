package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.model.TimeOverlapVo
import cz.blackshark.modules.main.persistence.Sql
import cz.blackshark.modules.main.persistence.entity.InvoiceItemEntity
import cz.blackshark.tools.DateTimeUtils
import io.vertx.core.json.JsonObject
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class TimelineDao @Inject constructor(val kotlinDbUtils: KotlinDbUtils) {

    fun minYearInTimeline(): String? {
        return kotlinDbUtils.loadData(Sql.Timeline.MIN_YEAR_IN_TIMELINE, mapOf())
        { r -> r.getString("min_year") }
            .firstOrNull()
    }

    fun maxTimeInDay(date: LocalDate): String? {
        return kotlinDbUtils.loadData(Sql.Timeline.MAX_TIME_IN_DATE, mapOf("date" to date)) { r ->
            val ts: Timestamp = r.getTimestamp("max_time")
            LocalTime.from(ts.toLocalDateTime().toLocalTime()).format(DateTimeFormatter.ofPattern("HH:mm"))
        }.firstOrNull()
    }

    fun dailyEarning(date: LocalDate, companyID: Long?): JsonObject? {
        return kotlinDbUtils.loadData(Sql.Timeline.DAILY_EARNING, mapOf("day" to date)) {
            val x = JsonObject()
            x.put("income", it.getBigDecimal("income").toDouble())
            x.put("worktime", it.getBigDecimal("work_time").toDouble())
        }.firstOrNull()
    }

    fun assignToInvoiceItem(
        fromDate: LocalDate,
        toDate: LocalDate,
        companyID: Long,
        invoiceItem: InvoiceItemEntity
    ): Int {
        val query = "update timeline set invoice_item_id = :invoiceItemID\n" +
                "where cast(from_time as date) >=:fromDate\n" +
                "  and cast(to_time as date) <=:toDate\n" +
                "and invoice_item_id is null\n" +
                "and project_id in (select id from project where company_id = :companyID)";
        return kotlinDbUtils.execute(
            query,
            mapOf(
                "fromDate" to fromDate,
                "toDate" to toDate,
                "companyID" to companyID,
                "invoiceItemID" to invoiceItem.id
            )
        )
    }


    fun getOverlapInfo(fromTime: LocalDateTime, toTime: LocalDateTime): TimeOverlapVo {
        val query =
            "select id, from_time, to_time from timeline where cast(from_time as date) = :date order by from_time asc"
        val idList = kotlinDbUtils.loadData(
            query,
            mapOf("date" to fromTime.toLocalDate())
        ) {
            Triple(
                it.getLong("id"),
                it.getTimestamp("from_time").toLocalDateTime(),
                it.getTimestamp("to_time").toLocalDateTime()
            )
        }.filter {
            DateTimeUtils.overlapping(fromTime, toTime, it.second, it.third) || DateTimeUtils.overlapping(
                it.second,
                it.third,
                fromTime,
                toTime
            )
        }.map { it.first }
        return TimeOverlapVo(idList.isNotEmpty(), idList)
    }

}
