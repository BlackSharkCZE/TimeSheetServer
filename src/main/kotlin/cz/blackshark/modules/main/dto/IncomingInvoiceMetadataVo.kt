package cz.blackshark.modules.main.dto

import java.time.LocalDate

data class IncomingInvoiceMetadataVo(
    val issuerID: Long,
    val recipientID: Long,
    val invoiceNumber: String,
    val issueDate: LocalDate,
    val paymentDate: LocalDate,
    val vatPaymentDate: LocalDate?
)
