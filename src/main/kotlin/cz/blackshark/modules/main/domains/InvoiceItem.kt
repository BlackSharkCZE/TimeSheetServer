package cz.blackshark.modules.main.domains

import java.math.BigDecimal

data class InvoiceItem(
    val description: String,
    val priceWithoutVat: BigDecimal,
    val vat: BigDecimal,
    val vatRate: BigDecimal,
    val priceWithVat: BigDecimal
)
