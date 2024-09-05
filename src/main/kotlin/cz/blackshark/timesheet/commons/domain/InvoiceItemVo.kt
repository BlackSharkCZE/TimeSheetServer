package cz.blackshark.timesheet.commons.domain

import java.math.BigDecimal

/**
 * Hold information about one line of the invoice
 */
data class InvoiceItemVo(
    val id: Long?,
    val description: String,
    val price: BigDecimal,
    val vatRate: BigDecimal,
    val vatAmount: BigDecimal,
    val priceWithVat: BigDecimal,
)
