package cz.blackshark.timesheet.commons.domain

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Describe invoide header
 */
data class InvoiceHeaderVo(
    val invoiceNumber: String,            // cislo faktury
    val orderNumber: String?,             // cislo objednavky
    val issueDate: LocalDate,            // datum vystaveni
    val paymentDate: LocalDate,           // datum splatnosti
    val vatPaymentDate: LocalDate?,      // datum uskutecneni zdanovaciho plneni
    val issuer: CompanyVo,                 // kdo fakturu vydal
    val recipient: CompanyVo,              // pro koho je faktura vydana
    val paymentSum: BigDecimal,             // celkova castka k fakturaci
    val paymentSumWithoutVat: BigDecimal, // celkova cena bez DPH
    val issuerNameString: String,
)
