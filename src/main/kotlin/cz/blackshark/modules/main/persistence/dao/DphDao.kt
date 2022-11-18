package cz.blackshark.modules.main.persistence.dao

import cz.blackshark.modules.main.dto.DphInvoiceSumPreviewVo
import org.jboss.logging.Logger
import java.sql.ResultSet
import java.text.MessageFormat
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@ApplicationScoped
class DphDao @Inject constructor(private val logger: Logger, private val dbUtils: KotlinDbUtils) {


    fun findIssuedInvoices(companyID: Long): List<DphInvoiceSumPreviewVo> = dbUtils.loadData(
        MessageFormat.format(SEARCH_INVOICE_OVERVIEW_TEMPLATE, "issuer_id"),
        mapOf("companyID" to companyID),
        this::dphInvoiceConverter
    )


    fun findReceivedInvoices(companyID: Long): List<DphInvoiceSumPreviewVo> = dbUtils.loadData(
        MessageFormat.format(SEARCH_INVOICE_OVERVIEW_TEMPLATE, "recipient_id"),
        mapOf("companyID" to companyID),
        this::dphInvoiceConverter
    )

    private fun dphInvoiceConverter(resultSet: ResultSet): DphInvoiceSumPreviewVo = DphInvoiceSumPreviewVo(
        KotlinDbUtils.getLong(resultSet, "invoice_id")!!,
        KotlinDbUtils.getString(resultSet, "i_number")!!,
        KotlinDbUtils.getLocalDateTime(resultSet, "issue_date")!!.toLocalDate(),
        KotlinDbUtils.getLocalDateTime(resultSet, "vat_payment_date")!!.toLocalDate(),
        KotlinDbUtils.getLocalDateTime(resultSet, "payment_date")!!.toLocalDate(),
        KotlinDbUtils.getLong(resultSet, "issuer_id")!!,
        KotlinDbUtils.getString(resultSet, "issuer")!!,
        KotlinDbUtils.getBoolean(resultSet, "issuer_vat_payer")!!,
        KotlinDbUtils.getLong(resultSet, "recipient_id")!!,
        KotlinDbUtils.getString(resultSet, "recipient")!!,
        KotlinDbUtils.getBoolean(resultSet, "recipient_vat_payer")!!,
        resultSet.getBigDecimal("price"),
        resultSet.getBigDecimal("total_price"),
        resultSet.getBigDecimal("vat_amount"),
    )


    companion object Queries {

        private const val SEARCH_INVOICE_OVERVIEW_TEMPLATE = """
       select i.i_number,
       i.issue_date,
       i.payment_date,
       i.vat_payment_date,
       i.id            as invoice_id,
       c.company_name  as issuer,
       c.id            as issuer_id,
       c.platce_dph    as issuer_vat_payer,
       c2.company_name as recipient,
       c2.id           as recipient_id,
       c2.platce_dph   as recipient_vat_payer,
       sum(ii.price) as price,
       sum(ii.total_price) as total_price,
       sum(ii.total_price) - sum(ii.price) as vat_amount
from invoice i
         join company c on i.issuer_id = c.id
         join company c2 on i.recipient_id = c2.id
         left join invoice_item ii on i.id = ii.invoice_int_id
where {0} = :companyID
group by i.i_number, i.issue_date, i.payment_date, i.vat_payment_date, i.id, c.company_name, c.id, c.platce_dph,
         c2.company_name, c2.id, c2.platce_dph
order by issue_date desc
"""


    }

}
