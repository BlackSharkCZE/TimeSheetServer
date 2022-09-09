package cz.blackshark.modules.main.domains

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class InvoiceSourceData(
    val timelineID: Long,
    val companyName: String,
    val companyID: Long,
    val projectID: Long,
    val workDay: LocalDate,
    val workSince: LocalTime,
    val workUntil: LocalTime,
    val workTime: BigDecimal,
    val rate: BigDecimal
)
