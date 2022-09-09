package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.modules.main.domains.InvoiceItem
import cz.blackshark.modules.main.domains.InvoiceSourceData
import cz.blackshark.modules.main.dto.EarningVo
import cz.blackshark.modules.main.persistence.Sql
import java.math.BigDecimal
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class InvoiceDao {

    @Inject
    lateinit var kotlinDbUtils: KotlinDbUtils

    fun getInvoiceItem(companyID: Long, fromDate: LocalDate, toDate: LocalDate): InvoiceItem? {
        return kotlinDbUtils.loadData(
            Sql.Invoice.SELECT_INVOICE_HEADER,
            mapOf("fromDate" to fromDate, "toDate" to toDate, "companyID" to companyID)
        ) { rs ->
            val value = rs.getBigDecimal("amount")
            val vatRate = BigDecimal.valueOf(0.21)
            InvoiceItem(
                rs.getString("note"),
                value,
                value.times(vatRate),
                vatRate,
                value.times(vatRate).plus(value)
            )
        }.firstOrNull()

    }

    fun loadInvoiceSourceData(companyID: Long, fromDate: LocalDate, toDate: LocalDate): List<InvoiceSourceData> {
        return kotlinDbUtils.loadData(
            Sql.Invoice.SELECT_INVOICE_SOURCE,
            mapOf("fromDate" to fromDate, "toDate" to toDate, "companyID" to companyID)
        ) { rs ->
            InvoiceSourceData(
                rs.getLong("timeline_id"),
                rs.getString("project_name"),
                rs.getLong("company_id"),
                rs.getLong("project_id"),
                rs.getDate("work_day").toLocalDate(),
                rs.getTime("work_since").toLocalTime(),
                rs.getTime("work_until").toLocalTime(),
                rs.getBigDecimal("work_time"),
                rs.getBigDecimal("rate")
            )
        }
    }

    fun earningByInvoiceNumber(invoiceNumber: String): List<EarningVo> {
        return kotlinDbUtils.loadData(
            Sql.WorkTime.EARNING_BY_INVOICE_ITEM,
            mapOf("invoiceNumber" to invoiceNumber)
        ) { rs ->
            EarningVo(
                rs.getBigDecimal("price"),
                rs.getLong("company_id"),
                rs.getString("company_name"),
                rs.getBigDecimal("work_time")
            )
        }
    }


}
