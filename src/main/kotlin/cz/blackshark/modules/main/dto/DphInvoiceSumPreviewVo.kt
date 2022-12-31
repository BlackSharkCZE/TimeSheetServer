package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DphInvoiceSumPreviewVo(

    val id: Long,
    val number: String,
    val issueDate: LocalDate,
    val vatPaymentDate: LocalDate,
    val paymentDate: LocalDate,

    val issuerId: Long,
    val issuerName: String,
    val issuerVatPayer: Boolean,

    val recipientId: Long,
    val recipientName: String,
    val recipientVatPayer: Boolean,

    val price: BigDecimal?,
    val totalPrice: BigDecimal?,
    val vatAmount: BigDecimal?

)
