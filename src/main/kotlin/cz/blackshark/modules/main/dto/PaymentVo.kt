package cz.blackshark.modules.main.dto

import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class PaymentVo(
    val id: Long?,
    @NotNull
    val payment: BigDecimal,
    @NotNull
    @Min(1)
    val companyId: Long,
    @NotNull
    val paymentDate: LocalDate,
    @NotNull
    val note: String
)

