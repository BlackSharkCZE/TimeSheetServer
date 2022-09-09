package cz.blackshark.modules.main.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InvoiceSummaryPreviewVo(
    val noOfDays: Int,
    val minDay: LocalDate?,
    val maxDay: LocalDate?,
    val workTime: BigDecimal?,
    val earning: BigDecimal?

)
