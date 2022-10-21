package cz.blackshark.modules.main.dto

import java.time.LocalDate

data class RequisitionVo(
    val id: Long,
    val companyId: Long,
    val companyName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val note: String,
    val documentName: String
)
