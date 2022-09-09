package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonView
import cz.blackshark.modules.main.http.views.Views
import cz.blackshark.timesheet.commons.domain.InvoiceHeaderVo
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class VatReport(
    val invoices: List<InvoiceHeaderVo>,
    val paymentSumWithVat: BigDecimal,
    val paymentSumWithoutVat: BigDecimal
)
