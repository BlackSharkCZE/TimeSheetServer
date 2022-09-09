package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.modules.main.dto.EarningVo
import cz.blackshark.modules.main.dto.ReportVo
import cz.blackshark.modules.main.persistence.Sql
import java.math.BigDecimal
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ReportDao {

    @Inject
    lateinit var kotlinDbUtils: KotlinDbUtils

    fun getReport(year: Int?, month: Int?, companyId: Long?): ReportVo {
        val sYear: Int = year ?: LocalDate.now().year
        var nYear = sYear
        val sMonth: Int = month ?: LocalDate.now().monthValue

        val nMonth = if (sMonth + 1 > 12) {
            nYear++; 1
        }; else sMonth + 1 // next month

        val from = LocalDate.of(sYear, sMonth, 1).atStartOfDay()
        val to = LocalDate.of(nYear, nMonth, 1).atStartOfDay()

        val params = mutableMapOf<String, Any>("fromTime" to from, "toTime" to to)
        val query = when (companyId) {
            null -> {
                Sql.WorkTime.WORK_TIME
            }
            else -> {
                params.put("companyId", companyId)
                Sql.WorkTime.WORK_TIME_FOR_COMPANY
            }
        }

        val x = kotlinDbUtils.loadData(query, params) { rs ->
            var hd: BigDecimal? = rs.getBigDecimal(1)
            hd = hd ?: BigDecimal.ZERO
            ReportVo(hd!!)
        }

        return if (x.isEmpty()) {
            ReportVo(BigDecimal.ZERO)
        } else {
            x.first()
        }
    }

    fun getReport(fromDate: LocalDate, toDate: LocalDate, companyId: Long?): BigDecimal {

        val params = mutableMapOf<String, Any>("fromTime" to fromDate, "toTime" to toDate)
        val query = when (companyId) {
            null -> {
                Sql.WorkTime.WORK_TIME
            }
            else -> {
                params.put("companyId", companyId)
                Sql.WorkTime.WORK_TIME_FOR_COMPANY
            }
        }

        return kotlinDbUtils.loadData(query, params) { rs ->
            var hd: BigDecimal? = rs.getBigDecimal(1)
            hd ?: BigDecimal.ZERO

        }.getOrElse(0) { BigDecimal.ZERO }
    }

    fun getEarning(fromDate: LocalDate, toDate: LocalDate): List<EarningVo> {
        return kotlinDbUtils.loadData(Sql.WorkTime.EARNING, mapOf("fromTime" to fromDate, "toTime" to toDate)) { rs ->
            EarningVo(
                rs.getBigDecimal("price"),
                rs.getLong("company_id"),
                rs.getString("company_name"),
                rs.getBigDecimal("work_time")
            )
        }
    }

}
